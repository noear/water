package org.noear.water.demo_api.controller;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.water.WaterProxy;
import org.noear.water.annotation.Water;
import org.noear.water.demo_rpc_client.BcfUserRpcService;
import org.noear.water.demo_rpc_client.UserModel;
import org.noear.water.log.WaterLogger;
import org.noear.water.utils.RedisX;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;

import java.util.Map;

@Controller
public class TestController {
    //::配置服务
    @Water("water/water")
    DbContext waterDb;

    @Water("water/water_cache")
    ICacheServiceEx cache;

    @Water("water/water_redis::1")
    RedisX redisX;

    @Water("water/is_use_tag_checker")
    Integer is_debug;

    //::日志服务
    @Water("water_log_admin")
    WaterLogger log;


    //::发现服务，集成负载平衡 和 容断

    @Water //要启动：water-demo-rpc-service-impl 项目
    BcfUserRpcService userRpcService;

    @Mapping("/test/")
    public String test() throws Exception {
        //db access
        Map dbmap = waterDb.table("bcf_user").limit(1).select("*").caching(cache).getMap();
        log.info("cfg db", dbmap);

        //redis access
        Map rdmap = redisX.open1(ru-> ru.key("ID").hashGetAll());
        log.info("cfg redis", rdmap);

        //rpc 调用
        UserModel app = userRpcService.getUser(12);
        log.info("rpc", app);

        //paas 调用
        String text = WaterProxy.paas("/_demo/ali_oss_cfg_water ", null);
        log.info("paas", text);

        return "OK - " + is_debug;
    }
}
