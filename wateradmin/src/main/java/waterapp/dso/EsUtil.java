package waterapp.dso;


import org.apache.http.util.TextUtils;
import org.noear.water.utils.Base64Utils;
import org.noear.water.utils.HttpUtils;
import waterapp.models.water_cfg.ConfigModel;

import java.util.Properties;

public class EsUtil {
    public static String search(ConfigModel cfg, String method, String path, String json) throws Exception {
        Properties prop = cfg.getProp();


        String url = prop.getProperty("url");
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");

        if(url.endsWith("/")){
            if(path.startsWith("/")){
                url = url + path.substring(1);
            }else{
                url = url + path;
            }
        }else {
            if (path.startsWith("/")) {
                url = url + path;
            } else {
                url = url + "/" + path;
            }
        }

        HttpUtils http = HttpUtils.http(url);

        if (TextUtils.isEmpty(username) == false) {
            String token = Base64Utils.encode(username + ":" + password);
            String auth = "Basic " + token;

            http.header("Authorization", auth);
        }

        http.bodyTxt(json, "application/json");

        return http.exec2(method.toUpperCase());
    }
}
