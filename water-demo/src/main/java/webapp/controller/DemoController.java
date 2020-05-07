package webapp.controller;

import lib.sponge.rock.models.AppModel;
import lib.sponge.rock.protocol.RockRpc;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.water.WaterProxy;
import org.noear.water.annotation.Water;
import org.noear.water.log.WaterLogger;
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

    @Water("water/paas_uri")
    String paas_uri;

    @Water("water/is_debug")
    Integer is_debug;

    //::日志服务
    @Water("water_log_admin")
    WaterLogger log;


    //::发现服务，集成负载平衡 和 容断
    @Water
    RockRpc rockRpc;

    @XMapping("/")
    public String test() throws Exception{
        //db access
        Map map = waterDb.table("bcf_user").limit(1).select("*").caching(cache).getMap();
        log.info("cfg db",map);

        //rpc 调用
        AppModel app = rockRpc.getAppByID(4);
        log.info("rpc",app);

        //paas 调用
        String text = WaterProxy.paas("/_demo/ali_oss_cfg_water ",null);
        log.info("paas",text);

        return "OK";
    }
}
