package watersev.utils;

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
        CallUtil.asynCall(()->{
            getHttpStatusDo(url, (isOk, code, hint) -> {
                callback.run(isOk, code, hint);
            });
        });
    }

    private static void getHttpStatusDo(String url, Act3<Boolean, Integer, String> callback) {
        try {
            URL u = new URL(url);
            try {
                HttpURLConnection uConnection = (HttpURLConnection) u.openConnection();
                try {
                    uConnection.setRequestMethod("HEAD"); //HEAD
                    uConnection.setConnectTimeout(1000 * 3);//3秒超时
                    uConnection.setReadTimeout(1000 * 3);//3秒超时
                    uConnection.connect();
                    int code = uConnection.getResponseCode();

                    callback.run(true, code, "");
                } catch (Throwable e) {
                    //e.printStackTrace();
                    callback.run(false, 0, e.getMessage());
                }

            } catch (IOException e) {
                //e.printStackTrace();
                callback.run(false, 0, e.getMessage());
            }

        } catch (MalformedURLException e) {
            //e.printStackTrace();
            callback.run(false, 0, "build url failed");
        }
    }
}
