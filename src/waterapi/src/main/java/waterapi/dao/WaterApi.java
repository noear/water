package waterapi.dao;

import noear.snacks.ONode;
import noear.weed.DbContext;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by yuety on 2017/7/18.
 */
public final class WaterApi {
    private final static String water_api_ver = "0.0.2";
    private final static String water_api_url = "http://water.zmapi.cn/";
    private final static String water_api_tag = "water";

    public final static class Config {
        private static ONode _data = new ONode();

        private static void tryInit() {
            if (_data.count() > 0) {
                return;
            }

            try {
                String api_url = water_api_url + "cfg/?tag=" + water_api_tag;

                String temp = Util.getString(api_url);
                _data = ONode.tryLoad(temp);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public static ConfigModel get(String key) {
            tryInit();

            if (_data.contains(key)) {
                ONode n = _data.get(key);
                ConfigModel m = new ConfigModel();

                m.key = n.get("key").getString();
                m.url = n.get("url").getString();
                m.user = n.get("usr").getString();
                m.password = n.get("pwd").getString();
                m.explain = n.get("exp").getString();

                return m;
            } else {
                return null;
            }
        }
    }

    /*-------- /cfg/ end --------*/

    /*-------- /log/ end --------*/

    public final static class ConfigModel {
        public String key;
        public String url;
        public String user;
        public String password;
        public String explain;
    }

    //提供内部http工具服务
    private final static class Util {
        public static String getString(String url) throws IOException {
            HttpClient client = HttpClients.createDefault();

            HttpGet httpGet = new HttpGet(url);


            HttpResponse response = client.execute(httpGet);

            HttpEntity entity = response.getEntity();

            return EntityUtils.toString(entity, "utf-8");
        }
    }
}
