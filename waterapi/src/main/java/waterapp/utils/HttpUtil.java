package waterapp.utils;

import org.noear.weed.ext.Fun0;
import okhttp3.*;
import waterapp.utils.ext.Act3;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpUtil {
    /** 可用于被重定义 */
    public static Fun0<Dispatcher> okhttp_dispatcher = ()->{
        Dispatcher temp = new Dispatcher();
        temp.setMaxRequests(3000);
        temp.setMaxRequestsPerHost(300);
        return temp;
    };

    private static final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .dispatcher(okhttp_dispatcher.run())
            .build();

    public static String getString(String url) throws IOException {
        return getString(url, null);
    }

    public static String getString(String url, Map<String, String> headers) throws IOException {
        Request.Builder builder = new Request.Builder()
                .url(url);

        if (headers != null) {
            headers.forEach((k, v) -> builder.header(k, v));
        }

        return execCall(builder);
    }

    public static String putString(String url, Map<String, String> params) throws IOException {
        return putString(url, params, null);
    }


    public static String putString(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        FormBody.Builder form = new FormBody.Builder();
        if (params != null) {
            params.forEach((k, v) -> {
                form.add(k, v);
            });
        }

        Request.Builder builder = new Request.Builder()
                .url(url)
                .put(form.build());

        if (headers != null) {
            headers.forEach((k, v) -> builder.header(k, v));
        }

        return execCall(builder);
    }

    public static String putString(String url, String rawParams, Map<String, String> headers) throws IOException {
        String content_type = "text/plain";
        if(headers.containsKey("Content-Type")){
            content_type = headers.get("Content-Type");
        }

        RequestBody body = FormBody.create(MediaType.parse(content_type), rawParams);

        Request.Builder builder = new Request.Builder()
                .url(url)
                .put(body);

        if (headers != null) {
            headers.forEach((k, v) -> builder.header(k, v));
        }

        return execCall(builder);
    }


    public static String postString(String url, Map<String, String> params) throws IOException {
        return postString(url, params, null);
    }

    public static String postString(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        FormBody.Builder form = new FormBody.Builder();
        if (params != null) {
            params.forEach((k, v) -> {
                form.add(k, v);
            });
        }

        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(form.build());

        if (headers != null) {
            headers.forEach((k, v) -> builder.header(k, v));
        }

        return execCall(builder);
    }

    public static String postString(String url, String rawParams, Map<String, String> headers) throws IOException {
        String content_type = "text/plain";
        if(headers.containsKey("Content-Type")){
            content_type = headers.get("Content-Type");
        }

        RequestBody body = FormBody.create(MediaType.parse(content_type), rawParams);

        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);

        if (headers != null) {
            headers.forEach((k, v) -> builder.header(k, v));
        }

        return execCall(builder);
    }

    //可用于写日志
    public static void postStringByAsync(String url, Map<String, String> params) throws IOException {
        postStringByAsync(url, params, null);
    }


    public static void postStringByAsync(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        postStringByAsync(url, params, headers, null);
    }

    public static void postStringByAsync(String url, Map<String, String> params, Map<String, String> headers, Act3<Boolean, String, Exception> callback) throws IOException {


        FormBody.Builder form = new FormBody.Builder();
        if (params != null) {
            params.forEach((k, v) -> {
                form.add(k, v);
            });
        }

        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(form.build());

        if (headers != null) {
            headers.forEach((k, v) -> builder.header(k, v));
        }

        httpClient.newCall(builder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                execCallback(callback, false, null, e);
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                execCallback(callback, true, response, null);
                call.cancel();
            }
        });
    }

    private static String execCall(Request.Builder builder) throws IOException {
        Call call = httpClient.newCall(builder.build());
        Response response = call.execute();
        try {
            if (!response.isSuccessful()) {
                throw new IOException("服务器端错误: " + response);
            }
            return response.body().string();
        }finally {
            //call.cancel();
            response.close();
        }
    }

    private static void execCallback(Act3<Boolean, String, Exception> callback, boolean isOk, Response response, Exception exception) {
        try {
            if (callback == null) {
                return;
            }

            if (isOk) {
                callback.run(true, response.body().string(), exception);
            } else {
                callback.run(false, null, exception);
            }
        } catch (Exception error) {
            error.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}
