package waterapp.controller;

import org.noear.snack.ONode;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XHandler;
import org.noear.water.utils.TextUtils;
import waterapp.Config;
import waterapp.dso.LockUtils;
import waterapp.dso.db.DbWaterCfgApi;
import waterapp.models.ConfigModel;
import waterapp.utils.IPUtils;

import java.util.Date;
import java.util.List;


@XMapping("/cfg/*/")
@XController
public class CfgController implements XHandler {
    @Override
    public void handle(XContext ctx) throws Throwable {
        //检查IP安全
        String ip = IPUtils.getIP(ctx);

        if (DbWaterCfgApi.isWhitelist(ip) == false) {
            ONode data = new ONode();
            data.set("code", 2);
            data.set("msg", ip + ",not is whitelist!");
            ctx.outputAsJson(data.toJson());
            return;
        }

        //设置处理
        String path = ctx.path();

        if (path.equals("/cfg/get/")) {
            do_get(ctx);
        } else if (path.equals("/cfg/set/")) {
            do_set(ctx);
        }
    }

    private void do_get(XContext ctx) throws Throwable {
        String tag = ctx.param("tag");

        ONode nList = new ONode().asObject();

        if (TextUtils.isEmpty(tag) == false) {
            List<ConfigModel> list = DbWaterCfgApi.getConfigByTag(tag);

            Date def_time = new Date();

            for (ConfigModel m1 : list) {
                if (m1.update_fulltime == null) {
                    m1.update_fulltime = def_time;
                }

                ONode n = nList.getNew(m1.key);
                n.set("key", m1.key);
                n.set("value", m1.value);

                if (m1.update_fulltime == null) {
                    n.set("lastModified", 0);
                } else {
                    n.set("lastModified", m1.update_fulltime.getTime());
                }
            }
        }

        if (ctx.param("v") == null) {
            ctx.outputAsJson(nList.toJson());
        } else {
            ONode n = new ONode();
            n.set("code", 1);
            n.set("msg", "succeed");
            n.set("data", nList);
            ctx.outputAsJson(n.toJson());
        }
    }

    private void do_set(XContext ctx) throws Throwable {
        String tag = ctx.param("tag");
        String key = ctx.param("key");
        String value = ctx.param("value");

        if(LockUtils.tryLock(Config.water_service_name, tag+"/"+key)){
            ONode n = new ONode();
            n.set("code", 0);
            n.set("msg", "Too many requests");
            ctx.outputAsJson(n.toJson());
            return;
        }

        //
        // 此处非线程安全
        //
        ConfigModel cfg = DbWaterCfgApi.getConfig(tag, key);
        if (key.equals(cfg.key)) {
            if (cfg.is_editable) {
                DbWaterCfgApi.setConfig(tag, key, value);
            }
            return;
        }

        DbWaterCfgApi.addConfig(tag, key, value);

        ONode n = new ONode();
        n.set("code", 1);
        n.set("msg", "succeed");
        ctx.outputAsJson(n.toJson());
    }
}