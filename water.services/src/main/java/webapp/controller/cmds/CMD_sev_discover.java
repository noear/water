package webapp.controller.cmds;

import org.noear.snack.ONode;
import waterapi.dao.db.DbApi;
import waterapi.dao.db.DbSevApi;
import waterapi.models.ConfigModel;
import waterapi.models.ServiceModel;
import waterapi.utils.TextUtils;

import java.util.List;

/**
 * Created by yuety on 2017/7/19.
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

        DbSevApi.logConsume(service, consumer, consumer_address);

        List<ServiceModel> list = DbSevApi.getServiceList(service);

        ConfigModel cfg = DbApi.getConfigNoCache("_service", service);

        if (TextUtils.isEmpty(cfg.explain)) {
            cfg.explain = "default";
        }

        data.set("code", 1);
        data.set("msg", "success");

        ONode rst = data.get("data");

        rst.set("url", cfg.url);
        rst.set("policy", cfg.explain); //default(轮询),weight(权重),ip_hash(IP哈希),url_hash(URL哈希)

        rst.get("list").addAll(list, (n, m) -> {
            n.set("protocol", "http");
            n.set("address", m.address);
            n.set("weight", 1);
        });
    }
}
