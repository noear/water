package waterapi.controller.config;

import org.noear.snack.ONode;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.extend.validation.annotation.NotEmpty;
import org.noear.solon.extend.validation.annotation.Whitelist;
import org.noear.water.utils.TextUtils;
import waterapi.controller.UapiBase;
import waterapi.dso.db.DbWaterCfgApi;
import waterapi.dso.interceptor.Logging;
import waterapi.models.ConfigModel;

import java.util.Date;
import java.util.List;

/**
 * 获取配置
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Logging
@Whitelist
@Controller
public class CMD_cfg_get extends UapiBase {

    @NotEmpty("tag")
    @Mapping("/cfg/get/")
    public Result cmd_exec(Context ctx, String tag) throws Throwable {
        ONode nList = new ONode().asObject();

        if (TextUtils.isEmpty(tag) == false) {
            List<ConfigModel> list = DbWaterCfgApi.getConfigByTag(tag);

            for (ConfigModel m1 : list) {
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
            return Result.succeed(nList);
        }
    }
}
