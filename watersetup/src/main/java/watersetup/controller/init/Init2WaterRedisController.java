package watersetup.controller.init;

import org.noear.redisx.RedisClient;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Post;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.handle.Result;
import org.noear.water.WW;
import watersetup.Config;
import watersetup.dso.db.DbWaterCfgApi;

import java.util.Properties;

/**
 * @author noear 2021/11/2 created
 */
@Controller
public class Init2WaterRedisController {

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

        String cacheCfg = "driverType=redis\n" + config + "\ndb=9";

        //更新配置
        DbWaterCfgApi.updConfig(WW.water, WW.water_redis, config);
        DbWaterCfgApi.updConfig(WW.water, WW.water_cache, cacheCfg);
        DbWaterCfgApi.updConfig(WW.water, Config.water_setup_step, "2");

        //2.
        return Result.succeed(null, "配置成功");
    }
}
