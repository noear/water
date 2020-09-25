package waterapi.controller.sev;

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
import waterapi.dso.db.DbWaterRegApi;
import waterapi.dso.interceptor.Logging;
import waterapi.models.ConfigModel;
import waterapi.models.ServiceModel;

import java.util.List;

/**
 * Created by noear on 2017/7/19.
 */
@Logging
@Whitelist
@XController
public class CMD_sev_discover extends UapiBase {
    @NotEmpty("service")
    @XMapping("/sev/discover/")
    public XResult cmd_exec(XContext ctx, String service) throws Exception {
        String consumer = ctx.param("consumer", "");
        String consumer_address = ctx.param("consumer_address", "");

        if(TextUtils.isNotEmpty(consumer)) {
            DbWaterRegApi.logConsume(service, consumer, consumer_address);
        }

        List<ServiceModel> list = DbWaterRegApi.getServiceList(service);

        ConfigModel cfg = DbWaterCfgApi.getConfigNoCache("_gateway", service);
        String url = null;
        String policy = null;

        if(TextUtils.isEmpty(cfg.value)){
            url = "";
            policy = "default";
        }else {
            if (cfg.is_enabled == false) {
                return XResult.failure("No gateway is available");
            }

            ONode prop = cfg.getNode();

            url = prop.get("url").getString();
            policy = prop.get("policy").getString();

            if (TextUtils.isEmpty(policy)) {
                policy = "default";
            }
        }


        ONode data  =new ONode();

        data.set("url", url);
        data.set("policy", policy); //default(轮询),weight(权重),ip_hash(IP哈希),url_hash(URL哈希) //default=polling

        data.get("list").addAll(list, (n, m) -> {
            n.set("protocol", "http");
            n.set("address", m.address);
            n.set("meta", m.note);
            n.set("weight", 1);
        });

        return XResult.succeed(data);
    }
}
