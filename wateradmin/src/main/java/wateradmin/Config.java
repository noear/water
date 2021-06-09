package wateradmin;

import org.noear.snack.ONode;
import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.extend.auth.AuthUtil;
import org.noear.solon.extend.validation.ValidatorManager;
import org.noear.water.WaterClient;
import org.noear.water.WW;
import org.noear.water.model.ConfigM;
import org.noear.weed.DbContext;
import org.noear.weed.WeedConfig;
import wateradmin.dso.auth.AuthProcessorImpl;
import wateradmin.setup.Setup;

public class Config {

    public static final DbContext water_log = cfg(WW.water_log).getDb(true);
    public static final DbContext water_msg = cfg(WW.water_msg).getDb(true);
    public static final DbContext water_paas = cfg(WW.water_paas).getDb(true);

    public static ConfigM water_log_store = cfg(WW.water_log_store);
    public static ConfigM water_msg_store = cfg(WW.water_msg_store);

    public static ConfigM water_redis = cfg(WW.water_redis);

    //paas 根地址
    public static String paas_uri() {
        return cfg("paas_uri").getString();
    }

    //raas 根地址
    public static String raas_uri() {
        return cfg("raas_uri").getString();
    }

    //是否使用标答检查器？
    public static boolean is_use_tag_checker() {
        return "1".equals(cfg("enable_tag_checker").getString());
    }

    public static String waterpaas_secretKey;

    //================================
    //
    //获取一个数据库配置

    static {
        WeedConfig.isDebug = false;
        WeedConfig.isUsingValueExpression = false;
    }

    public static void tryInit(SolonApp app) {
        waterpaas_secretKey = app.cfg().get("waterpaas.secretKey");

        Setup.water = cfg(WW.water).getDb(true);

        WaterClient.Config.get(WW.water_bcf, "bcf.yml").getProp().forEach((k, v) -> {
            if (Solon.cfg().isDebugMode()) {
                String key = k.toString();
                if (key.indexOf(".session.") < 0) {
                    Solon.cfg().put(k, v);
                }
            } else {
                Solon.cfg().put(k, v);
            }
        });

        //适配认证框架
        AuthUtil.adapter()
                .loginUrl("/login")
                .addRule(r -> r.include("**").verifyIp().failure((c, t) -> c.output(", not")))
                .addRule(r -> r.exclude("/login**").exclude("/run/**").exclude("/msg/**").exclude("/_session/**").verifyPath())
                .processor(new AuthProcessorImpl())
                .failure((ctx, rst) -> {
                    ctx.outputAsJson(new ONode().set("code", 403).set("msg", "没有权限").toJson());
                });

        ValidatorManager.global().onFailure((ctx, ano, result, message) -> {
            ctx.setHandled(true);
            ctx.setRendered(true);
            ctx.outputAsJson(new ONode().set("code", result.getCode()).set("msg", result.getDescription()).toJson());
            return true;
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
