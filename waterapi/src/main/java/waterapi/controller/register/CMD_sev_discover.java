package waterapi.controller.register;

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
import waterapi.dso.db.DbWaterRegApi;
import waterapi.dso.interceptor.Logging;
import waterapi.models.ConfigModel;
import waterapi.models.ServiceModel;

import java.util.List;

/**
 * 服务发现
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Logging
@Whitelist
@Controller
public class CMD_sev_discover extends UapiBase {
    /**
     * @param service          服务名
     * @param consumer         消费者服务名
     * @param consumer_address 消费者地址
     */
    @NotEmpty("service")
    @Mapping("/sev/discover/")
    public Result cmd_exec(Context ctx, String service, String consumer, String consumer_address) throws Exception {

        if (TextUtils.isNotEmpty(consumer) && TextUtils.isNotEmpty(consumer_address)) {
            //记录消费者
            //
            DbWaterRegApi.logConsume(service, consumer, consumer_address);
        }

        List<ServiceModel> list = DbWaterRegApi.getServiceList(service);

        ConfigModel cfg = DbWaterCfgApi.getConfigNoCache("_gateway", service);
        String url = null;
        String policy = null;

        if (TextUtils.isEmpty(cfg.value)) {
            url = "";
            policy = "default";
        } else {
            if (cfg.is_enabled == false) {
                return Result.failure("No gateway is available");
            }

            ONode prop = cfg.getNode();

            url = prop.get("url").getString();
            policy = prop.get("policy").getString();

            if (TextUtils.isEmpty(policy)) {
                policy = "default";
            }
        }


        ONode data = new ONode();

        data.set("url", url);
        data.set("policy", policy); //default(轮询),weight(权重),ip_hash(IP哈希),url_hash(URL哈希) //default=polling

        data.get("list").addAll(list, (n, m) -> {
            n.set("protocol", "http");
            n.set("address", m.address);
            n.set("meta", m.note);
            n.set("weight", 1);
        });

        return Result.succeed(data);
    }
}
