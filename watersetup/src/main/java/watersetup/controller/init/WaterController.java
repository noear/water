package watersetup.controller.init;

import org.noear.redisx.RedisClient;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Post;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.water.WW;
import org.noear.water.utils.Base64Utils;
import org.noear.weed.DbContext;
import watersetup.Config;
import watersetup.dso.InitUtils;
import watersetup.dso.db.DbWaterCfgApi;

import java.util.Properties;

/**
 * @author noear 2021/11/2 created
 */
@Controller
public class WaterController {

    @Post
    @Mapping("/ajax/connect")
    public Result ajax_connect(Context ctx, String config) throws Exception {
        if (Utils.isEmpty(config)) {
            return Result.failure("配置不能为空");
        }

        Properties props = Config.getProp(config);

        if (props.size() > 3) {
            Config.water = Config.getDb(props);

            if (Config.water != null) {
                //连接成功
                String token = Base64Utils.encode(config);
                ctx.cookieSet("TOKEN", token);
            }
        } else {
            return Result.failure("配置有问题...");
        }


        if (Config.water == null) {
            return Result.failure("连接失败");
        }

        //0.
        return Result.succeed(null, "连接成功");
    }

    @Post
    @Mapping("/ajax/init/water")
    public Result ajax_init(String config) throws Exception {
        if (Config.water == null) {
            return Result.failure("未连接数据库，刷新再试...");
        }

        tryInitSchema(Config.water);

        //1.
        return Result.succeed(null, "初始化成功");
    }

    @Post
    @Mapping("/ajax/init/redis")
    public Result ajax_init_redis(String config) throws Exception {
        if (Config.water == null) {
            return Result.failure("未连接数据库，刷新再试...");
        }

        try {
            Properties props = Utils.buildProperties(config);
            RedisClient redisClient = new RedisClient(props, 0);
            redisClient.open(s -> {
                s.key("test").get();
            });
        } catch (Exception e) {
            EventBus.push(e);
            return Result.failure("连接失败");
        }

        DbWaterCfgApi.updConfig(WW.water, Config.water_setup_step, "2");

        //2.
        return Result.succeed(null, "配置成功");
    }


    private void tryInitSchema(DbContext db) throws Exception {
        if (InitUtils.allowWaterInit(db) == false) {
            DbWaterCfgApi.updConfig(WW.water, Config.water_setup_step, "1");
            return;
        }


        db.setAllowMultiQueries(true);

        InitUtils.tryInitWater(db);
        InitUtils.tryInitWaterBcf(db);

//        String schema = props.getProperty("schema");
//        String password = props.getProperty("password");
//        String username = props.getProperty("username");
//        String url = props.getProperty("url");
//
//
//        //更新配置
//        StringBuilder cfgBuf = new StringBuilder();
//        cfgBuf.append("schema=").append(schema).append("\n");
//        cfgBuf.append("url=").append(url).append("\n");
//        cfgBuf.append("password=").append(password).append("\n");
//        cfgBuf.append("username=").append(username).append("\n");
//        cfgBuf.append("jdbcUrl=${url}\n");
//
//        DbWaterCfgApi.updConfig(WW.water, WW.water, cfgBuf.toString());
        DbWaterCfgApi.updConfig(WW.water, Config.water_setup_step, "1");
    }
}