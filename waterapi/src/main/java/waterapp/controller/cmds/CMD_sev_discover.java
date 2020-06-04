package waterapp.controller.cmds;

import org.noear.snack.ONode;
import org.noear.water.utils.TextUtils;
import waterapp.dso.db.DbWaterCfgApi;
import waterapp.dso.db.DbWaterRegApi;
import waterapp.models.ConfigModel;
import waterapp.models.ServiceModel;

import java.util.List;

/**
 * Created by noear on 2017/7/19.
 */
public class CMD_sev_discover extends CMDBase {
    @Override
    protected void cmd_exec() throws Exception {

        String consumer = get("consumer", "");
        String consumer_address = get("consumer_address", "");
        String service = get("service", "");

        if (checkParamsIsOk(service, consumer, consumer_address) == false) {
            return;
        }

        DbWaterRegApi.logConsume(service, consumer, consumer_address);

        List<ServiceModel> list = DbWaterRegApi.getServiceList(service);

        ConfigModel cfg = DbWaterCfgApi.getConfigNoCache("_gateway", service);

        ONode prop = cfg.getNode();

        String url = prop.get("url").getString();
        String policy = prop.get("policy").getString();

        if(TextUtils.isEmpty(policy)){
            policy = "default";
        }


        data.set("code", 1);
        data.set("msg", "success");

        ONode rst = data.get("data");

        rst.set("url", url);
        rst.set("policy", policy); //default(轮询),weight(权重),ip_hash(IP哈希),url_hash(URL哈希) //default=polling

        rst.get("list").addAll(list, (n, m) -> {
            n.set("protocol", "http");
            n.set("address", m.address);
            n.set("weight", 1);
        });
    }
}
