package wateradmin.dso;


import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;
import org.noear.water.utils.Base64Utils;
import wateradmin.models.water_cfg.ConfigModel;

import java.io.IOException;
import java.net.URI;
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

        HttpBodyRequest request = new HttpBodyRequest(url, method.toUpperCase(), json);

        if (TextUtils.isEmpty(username) == false) {
            String token = Base64Utils.encode(username + ":" + password);
            String auth = "Basic " + token;

            request.addHeader("Authorization", auth);
        }

        return call(request);
    }

    private static String call(HttpBodyRequest request) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        CloseableHttpResponse response = null;

        try {
            response = client.execute(request);

            return EntityUtils.toString(response.getEntity(), "utf-8");
        } finally {
            if (response != null) {
                response.close();
            }

            if (client != null) {
                client.close();
            }
        }
    }

    public static class HttpBodyRequest extends HttpEntityEnclosingRequestBase{

        String method;
        public HttpBodyRequest(String uri,String method, String body){
            super();
            this.method =  method;
            setURI(URI.create(uri));
            setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
        }

        @Override
        public String getMethod() {
            return method;
        }
    }
}
