package watersev.utils;

import noear.weed.ext.Act2;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;
import watersev.utils.ext.Act3;

import java.io.IOException;
import java.util.List;

/**
 * Created by yuety on 2017/7/18.
 */
public class HttpUtil {
    public static String postString(String url, List<NameValuePair> params) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
        HttpResponse response = httpClient.execute(httppost);
        HttpEntity entity = response.getEntity();

        return EntityUtils.toString(entity, "utf-8");
    }

    //可用于写日志
    public static void postStringByAsync(String url, List<NameValuePair> params, Act3<Boolean, Integer, String> callback) throws IOException {


        CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault();


        httpClient.start();

        final HttpPost request = new HttpPost(url);
        request.setEntity(new UrlEncodedFormEntity(params, "utf-8"));

        httpClient.execute(request, new FutureCallback<HttpResponse>() {
            @Override
            public void failed(Exception ex) {
                closeAsyncHttpclient(httpClient);
                callback.run(false, 0, null);
            }

            @Override
            public void completed(HttpResponse resp) {
                try {
                    HttpEntity entity = resp.getEntity();
                    String rst = EntityUtils.toString(entity, "utf-8");

                    closeAsyncHttpclient(httpClient);
                    callback.run(true, 1, rst);
                } catch (Exception ex) {
                    closeAsyncHttpclient(httpClient);
                    callback.run(false, 1, null);
                }
            }

            @Override
            public void cancelled() {
                closeAsyncHttpclient(httpClient);
                callback.run(false, -1, null);
            }
        });
    }

    public static void getStatusByAsync(String url, int timeout, Act2<Boolean, Integer> callback) throws IOException {

        HttpAsyncClientBuilder builder = HttpAsyncClients.custom();

        if (timeout > 0) {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(timeout)
                    .setSocketTimeout(timeout).build();

            builder.setDefaultRequestConfig(requestConfig);
        }


        CloseableHttpAsyncClient httpClient = builder.build();

        httpClient.start();

        final HttpGet request = new HttpGet(url);

        httpClient.execute(request, new FutureCallback<HttpResponse>() {
            @Override
            public void failed(Exception ex) {
                //closeAsyncHttpclient(httpClient);
                callback.run(false, 0);
            }

            @Override
            public void completed(HttpResponse resp) {
                try {
                    int code = resp.getStatusLine().getStatusCode();

                    closeAsyncHttpclient(httpClient);
                    callback.run(true, code);
                } catch (Exception ex) {
                    closeAsyncHttpclient(httpClient);
                    callback.run(false, 0);
                }
            }

            @Override
            public void cancelled() {
                closeAsyncHttpclient(httpClient);
                callback.run(false, 0);
            }
        });
    }


    private static void closeAsyncHttpclient(CloseableHttpAsyncClient client) {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
