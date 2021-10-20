package wateradmin.dso;

import org.noear.water.WW;
import org.noear.water.utils.Base64Utils;
import org.noear.water.utils.HttpUtils;
import org.noear.water.utils.TextUtils;
import wateradmin.models.water_cfg.ConfigModel;

import java.util.Properties;

public class EsUtil {
    public static String search(ConfigModel cfg, String method, String path, String json) throws Exception {
        Properties prop = cfg.getProp();


        String url = prop.getProperty("url");
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");

        if (url.endsWith("/")) {
            if (path.startsWith("/")) {
                url = url + path.substring(1);
            } else {
                url = url + path;
            }
        } else {
            if (path.startsWith("/")) {
                url = url + path;
            } else {
                url = url + "/" + path;
            }
        }

        HttpUtils httpUtils = HttpUtils.http(url).bodyTxt(json, WW.mime_json);

        if (TextUtils.isEmpty(username) == false) {
            String token = Base64Utils.encode(username + ":" + password);
            String auth = "Basic " + token;

            httpUtils.header("Authorization", auth);
        }

        if(method.equals("GET")){
            method = "POST";
        }

        return httpUtils.exec2(method);
    }
}
