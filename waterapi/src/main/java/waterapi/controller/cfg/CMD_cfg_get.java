package waterapi.controller.cfg;

import org.noear.snack.ONode;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XResult;
import org.noear.solon.extend.validation.annotation.NotEmpty;
import org.noear.solon.extend.validation.annotation.Whitelist;
import org.noear.water.utils.TextUtils;
import waterapi.controller.UapiBase;
import waterapi.dso.db.DbWaterCfgApi;
import waterapi.dso.interceptor.Logging;
import waterapi.models.ConfigModel;

import java.util.Date;
import java.util.List;

@Logging
@Whitelist
@XController
public class CMD_cfg_get extends UapiBase {
    @NotEmpty("tag")
    @XMapping("/cfg/get/")
    public XResult cmd_exec(XContext ctx, String tag) throws Throwable {
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
            return null;
        } else {
            return XResult.succeed(nList);
        }
    }
}
