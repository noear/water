package waterapi.controller.register;

import org.noear.snack.ONode;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Whitelist;
import org.noear.water.utils.TextUtils;
import waterapi.controller.UapiBase;
import waterapi.dso.db.DbWaterCfgUpstreamApi;
import waterapi.dso.db.DbWaterRegApi;
import waterapi.dso.interceptor.Logging;
import waterapi.models.GatewayModel;
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
    public Result cmd_exec(Context ctx, String tag, String service, String consumer, String consumer_address) throws Exception {

        if (TextUtils.isNotEmpty(consumer) && TextUtils.isNotEmpty(consumer_address)) {
            //记录消费关系
            DbWaterRegApi.logConsume(service, consumer, consumer_address);
        }

        List<ServiceModel> list = DbWaterRegApi.getServiceList(service);

        GatewayModel cfg = DbWaterCfgUpstreamApi.getGatewayByName(service);
        String url = null;
        String policy = null;

        if (cfg.gateway_id > 0 && cfg.is_enabled > 0) {
            url = cfg.agent;
            policy = cfg.policy;
        }

        if(url == null){
            url = "";
        }

        if (TextUtils.isEmpty(policy)) {
            policy = "default";
        }


        ONode data = new ONode();

        data.set("url", url); //以后不再支持
        data.set("agent", url);
        data.set("policy", policy); //default(轮询),weight(权重),ip_hash(IP哈希),url_hash(URL哈希) //default=polling

        data.getOrNew("list").addAll(list, (n, m) -> {
            n.set("protocol", "http");
            n.set("address", m.address);
            n.set("meta", m.meta);
            n.set("weight", 1);
        });

        return Result.succeed(data);
    }
}
