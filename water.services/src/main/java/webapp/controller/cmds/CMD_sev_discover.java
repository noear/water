package webapp.controller.cmds;

import org.noear.snack.ONode;
import org.noear.water.tools.TextUtils;
import webapp.dso.db.DbApi;
import webapp.dso.db.DbServiceApi;
import webapp.model.ConfigModel;
import webapp.model.ServiceModel;

import java.util.List;
import java.util.Properties;

public class CMD_sev_discover extends CMDBase {
    @Override
    protected void cmd_exec() throws Exception {

        String consumer = get("consumer", "");
        String consumer_address = get("consumer_address", "");
        String service = get("service", "");

        if (checkParamsIsOk(service, consumer, consumer_address) == false) {
            return;
        }

        DbServiceApi.logConsume(service, consumer, consumer_address);

        List<ServiceModel> list = DbServiceApi.getServiceList(service);

        ConfigModel cfg = DbApi.getConfigNoCache("_service", service);

        Properties prop = cfg.toProp();


        data.set("code", 1);
        data.set("msg", "success");

        ONode rst = data.get("data");

        //代理url
        rst.set("url", prop.getProperty("url"));

        //策略： default(轮询),weight(权重),ip_hash(IP哈希),url_hash(URL哈希)
        //
        rst.set("policy", prop.getProperty("policy"));

        rst.get("list").addAll(list, (n, m) -> {
            n.set("protocol", "http");
            n.set("address", m.address);
            n.set("weight", 1);
        });
    }
}
