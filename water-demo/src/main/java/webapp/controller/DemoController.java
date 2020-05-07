package webapp.controller;

import lib.sponge.rock.models.AppModel;
import lib.sponge.rock.protocol.RockRpc;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.water.WaterProxy;
import org.noear.water.annotation.Water;
import org.noear.water.log.WaterLogger;
import org.noear.water.utils.RedisX;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;

import java.util.Map;

@XController
public class DemoController {
    //::配置服务
    @Water("water/water")
    DbContext waterDb;

    @Water("water/water_cache")
    ICacheServiceEx cache;

    @Water("water/water_redis::1")
    RedisX redisX;

    @Water("water/is_debug")
    Integer is_debug;

    //::日志服务
    @Water("water_log_admin")
    WaterLogger log;


    //::发现服务，集成负载平衡 和 容断
    @Water
    RockRpc rockRpc;

    @XMapping("/")
    public String test() throws Exception {
        //db access
        Map dbmap = waterDb.table("bcf_user").limit(1).select("*").caching(cache).getMap();
        log.info("cfg db", dbmap);

        //redis access
        Map rdmap = redisX.open1(ru-> ru.key("ID").hashGetAll());
        log.info("cfg redis", rdmap);

        //rpc 调用
        AppModel app = rockRpc.getAppByID(4);
        log.info("rpc", app);

        //paas 调用
        String text = WaterProxy.paas("/_demo/ali_oss_cfg_water ", null);
        log.info("paas", text);

        return "OK - " + is_debug;
    }
}
