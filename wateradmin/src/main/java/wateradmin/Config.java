package wateradmin;

import org.noear.redisx.RedisClient;
import org.noear.snack.ONode;
import org.noear.solon.SolonApp;
import org.noear.solon.auth.AuthUtil;
import org.noear.solon.health.HealthHandler;
import org.noear.water.WaterClient;
import org.noear.water.WW;
import org.noear.water.WaterSetting;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.DsCacheUtils;
import org.noear.wood.DbContext;
import org.noear.wood.WoodConfig;
import wateradmin.dso.SessionPerms;
import wateradmin.dso.auth.AuthProcessorImpl;

public class Config {
    public static final DbContext water;
    public static final DbContext water_paas;
    public static final DbContext water_paas_request;

    public static RedisClient rd_track; //db:5

    public static ConfigM water_log_store = cfg(WW.water_log_store);
    public static ConfigM water_msg_store = cfg(WW.water_msg_store);


    //paas 根地址
    public static String faas_uri() {
        return cfg("faas_uri").getString();
    }

    //raas 根地址
    public static String raas_uri() {
        return cfg("raas_uri").getString();
    }

    //是否使用标答检查器？
    public static boolean is_use_tag_checker() {
        return "1".equals(cfg("enable_tag_checker").getString());
    }

    public static String waterfaas_secretKey;

    //================================
    //
    //获取一个数据库配置

    static {
        WoodConfig.isDebug = false;
        WoodConfig.isUsingValueExpression = false;

        water = DsCacheUtils.getDb(cfg(WW.water).value, true);
        water_paas = DsCacheUtils.getDb(cfg(WW.water_paas).value, true, water);
        water_paas_request = DsCacheUtils.getDb(cfg(WW.water_paas_request).value, true, water_paas);
    }

    public static void tryInit(SolonApp app) {
        waterfaas_secretKey = app.cfg().get("waterfaas.secretKey");

        rd_track = WaterSetting.redis_track_cfg().getRd(5);

        //适配认证框架
        AuthUtil.adapter()
                .loginUrl("/login")
                .addRule(r -> r.include("**").verifyIp().failure((c, t) -> c.output(c.realIp() + ", not safelist!")))
                .addRule(r -> r.exclude("/login**").exclude(HealthHandler.HANDLER_PATH).exclude("/_**").verifyPath())
                .addRule(r -> r.include("/grit/ui/**").verifyPermissions(SessionPerms.admin))
                .processor(new AuthProcessorImpl())
                .failure((ctx, rst) -> {
                    ctx.outputAsJson(new ONode().set("code", 403).set("msg", "没有权限").toJson());
                });
    }

    //================================
    //

    public static ConfigM cfg(String key) {
        if (key.indexOf("/") < 0) {
            return WaterClient.Config.get(WW.water, key);
        } else {
            return WaterClient.Config.getByTagKey(key);
        }
    }
}
