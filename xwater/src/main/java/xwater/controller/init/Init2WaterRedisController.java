package xwater.controller.init;

import org.noear.redisx.RedisClient;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Post;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.handle.Result;
import org.noear.water.WW;
import xwater.Config;
import xwater.dso.db.DbWaterCfgApi;

import java.util.Properties;

/**
 * @author noear 2021/11/2 created
 */
@Controller
public class Init2WaterRedisController {

    @Post
    @Mapping("/ajax/init/redis")
    public Result ajax_init_redis(String config)  {
        if (Config.water == null) {
            return Result.failure("未连接数据库，刷新再试...");
        }

        Properties props;
        try {
            props = Utils.buildProperties(config);
            RedisClient redisClient = new RedisClient(props, 0);
            redisClient.open(s -> {
                s.key("test").get();
            });
        } catch (Exception e) {
            EventBus.push(e);
            return Result.failure("出错，连接失败");
        }

        Properties waterPro = Config.getCfg(WW.water, WW.water).getProp();

        String cacheCfg = "driverType=redis\n" + config + "\ndb=9";

        StringBuilder bcfCfg = new StringBuilder();
        bcfCfg.append("grit.cache.driverType=").append("redis").append("\n");
        bcfCfg.append("grit.cache.server=").append(props.getProperty("server")).append("\n");
        bcfCfg.append("grit.cache.password=").append(props.getProperty("password")).append("\n");
        bcfCfg.append("grit.cache.db=").append("9").append("\n");
        bcfCfg.append("\n");

        bcfCfg.append("grit.db.schema=").append(waterPro.getProperty("schema")).append("\n");
        bcfCfg.append("grit.db.url=").append(waterPro.getProperty("url")).append("\n");
        bcfCfg.append("grit.db.password=").append(waterPro.getProperty("password")).append("\n");
        bcfCfg.append("grit.db.username=").append(waterPro.getProperty("username")).append("\n");
        bcfCfg.append("\n");
        bcfCfg.append("server.session.state.domain=").append("water.noear.org").append("\n");
        bcfCfg.append("server.session.timeout=").append("7200").append("\n");
        bcfCfg.append("\n");
        bcfCfg.append("grit.token=").append("DLAWrz90wAc2MIqHZ9o8b3RaPWiHebez").append("\n");

        //更新配置
        try {
            DbWaterCfgApi.updConfig(WW.water, WW.water_redis, config);
            DbWaterCfgApi.updConfig(WW.water, WW.water_cache, cacheCfg);
            DbWaterCfgApi.updConfig("grit", "grit.yml", bcfCfg.toString());
            DbWaterCfgApi.updConfig(WW.water, Config.water_setup_step, "2");
        }catch (Exception e){
            return Result.failure("出错，" + e.getLocalizedMessage());
        }

        //2.
        return Result.succeed(null, "配置成功");
    }
}
