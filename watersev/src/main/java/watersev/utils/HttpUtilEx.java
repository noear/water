package watersev.utils;

import org.noear.water.utils.RunUtils;
import watersev.utils.ext.Act3;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by noear on 2017/7/18.
 */
public class HttpUtilEx {

    /**
     * callback:
     * isOk:请求是否成功
     * code:如果成功，状态码为何?
     * hint:如果出错，提示信息?
     */
    public static void getStatusByAsync(String url, Act3<Boolean, Integer, String> callback)  {
        RunUtils.runAsyn(()->{
            getHttpStatusDo(url, (isOk, code, hint) -> {
                callback.run(isOk, code, hint);
            });
        });
    }

    private static void getHttpStatusDo(String url, Act3<Boolean, Integer, String> callback) {
        try {
            URL u = new URL(url);
            try {
                HttpURLConnection connection = (HttpURLConnection) u.openConnection();
                try {
                    connection.setRequestMethod("HEAD"); //HEAD
                    connection.setConnectTimeout(1000 * 2);
                    connection.setReadTimeout(1000 * 2);
                    connection.setUseCaches(false);
                    connection.connect();
                    int code = connection.getResponseCode();
                    connection.disconnect();

                    callback.run(true, code, "");
                } catch (Throwable e) {
                    callback.run(false, 0, e.getMessage());
                }

            } catch (IOException e) {
                callback.run(false, 0, e.getMessage());
            }

        } catch (MalformedURLException e) {
            callback.run(false, 0, "build url failed");
        }
    }
}
