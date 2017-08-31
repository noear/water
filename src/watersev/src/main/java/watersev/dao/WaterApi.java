package watersev.dao;

import noear.snacks.ONode;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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

    /*-------- /msg/ begin --------*/

    public final static class Message {
        public static ONode subscribeTopic(String key, String topic, String receiver_url,String access_key, boolean isSync) throws Exception {
            String api_url = water_api_url + "msg/subscribe/";
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("key", key));
            params.add(new BasicNameValuePair("topic", topic));
            params.add(new BasicNameValuePair("receiver_url", receiver_url));
            params.add(new BasicNameValuePair("access_key", access_key));
            params.add(new BasicNameValuePair("sync", isSync ? "1" : "0"));

            String txt = Util.postString(api_url, params);

            return ONode.tryLoad(txt);
        }

        public static ONode sendMessage(String key, String topic, String message) throws Exception {
            String api_url = water_api_url + "msg/send/";
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("key", key));
            params.add(new BasicNameValuePair("topic", topic));
            params.add(new BasicNameValuePair("message", message));


            String txt = Util.postString(api_url, params);

            return ONode.tryLoad(txt);
        }

        public static boolean checkMessage(MessageModel msg, String access_key){

            if(msg.id<1){
                return false;
            }

            StringBuilder sb = new StringBuilder(200);
            sb.append(msg.id).append("#");
            sb.append(msg.key).append("#");
            sb.append(msg.topic).append("#");
            sb.append(msg.message).append("#");
            sb.append(access_key);

            String sgin_slf = Util.md5(sb.toString());

            return sgin_slf.equals(msg.sgin);
        }
    }


    /*-------- /msg/ end --------*/

    /*-------- /log/ begin --------*/

    public final static class Logger {
        public static void append(String logger, int level, String tag, long tag1,  String label, String content)  {
            String api_url = water_api_url + "log/add/";
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("logger", logger));
            params.add(new BasicNameValuePair("level", level + ""));
            params.add(new BasicNameValuePair("tag", tag));
            params.add(new BasicNameValuePair("tag1", tag1+""));
            params.add(new BasicNameValuePair("label", label));
            params.add(new BasicNameValuePair("content", content));

            try {
                Util.postStringByAsync(api_url, params);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    /*-------- /log/ end --------*/

    /*-------- /reg/ begin --------*/

    public final static class Registry{
        public static void add(String service, String address, String check_url) {
            add(service,address,"",check_url,0);
        }

        //@parme checkType: 0通过check_url检查，1自己定时签到
        //
        public static void add(String service, String address, String note, String check_url, int check_type){
            String api_url = water_api_url + "sev/reg/";
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("service", service));
            params.add(new BasicNameValuePair("address", address));
            params.add(new BasicNameValuePair("note", note));

            params.add(new BasicNameValuePair("check_url", check_url));
            params.add(new BasicNameValuePair("check_type", check_type+""));

            try {
                Util.postStringByAsync(api_url, params);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    /*-------- /reg/ end --------*/

    public final static class ConfigModel {
        public String key;
        public String url;
        public String user;
        public String password;
        public String explain;
    }

    public final static class MessageModel {
        public long id;
        public String key;
        public String topic;
        public String message;
        public String sgin;

        public MessageModel(){

        }

        public String toJson(){
            ONode d = new ONode();

            d.set("id",id);
            d.set("key",key);
            d.set("topic",topic);
            d.set("message",message);
            d.set("sgin",sgin);

            return d.toJson();
        }
    }


    //提供内部http工具服务
    private final static class Util {

        public static String encodeBase46(String str){
            byte[] btys = str.getBytes(Charset.forName("UTF-8"));
            return new String(Base64.getEncoder().encode(btys));
        }

        public static String decodeBase46(String str){
            byte[] btys = str.getBytes(Charset.forName("UTF-8"));
            return new String(Base64.getDecoder().decode(btys));
        }

        public static String md5(String str)
        {
            String s = null;
            try {
                byte[] data = str.getBytes("UTF-8");

                s = DigestUtils.md5Hex(data);
            }catch (Exception ex){
                ex.printStackTrace();
            }

            return s;
        }

        public static String getString(String url) throws IOException {
            HttpClient client = HttpClients.createDefault();

            HttpGet httpGet = new HttpGet(url);


            HttpResponse response = client.execute(httpGet);

            HttpEntity entity = response.getEntity();

            return EntityUtils.toString(entity, "utf-8");
        }

        public static String postString(String url, List<NameValuePair> params) throws IOException {
            HttpClient client = HttpClients.createDefault();

            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));

            HttpResponse response = client.execute(httppost);

            HttpEntity entity = response.getEntity();

            return EntityUtils.toString(entity, "utf-8");
        }

        //可用于写日志
        public static void postStringByAsync(String url, List<NameValuePair> params) throws IOException {
            CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();

            httpclient.start();

            final HttpPost request = new HttpPost(url);
            request.setEntity(new UrlEncodedFormEntity(params, "utf-8"));

            httpclient.execute(request, new FutureCallback<HttpResponse>() {
                @Override
                public void failed(Exception ex) {
                    ex.printStackTrace();
                    closeHttpclient(httpclient);
                }

                @Override
                public void completed(HttpResponse resp) {
                    try {
                        HttpEntity entity = resp.getEntity();
                        String rst = EntityUtils.toString(entity, "utf-8");

                        System.out.print(rst);

                        closeHttpclient(httpclient);
                    }catch (Exception ex){
                        closeHttpclient(httpclient);
                    }
                }

                @Override
                public void cancelled() {
                    closeHttpclient(httpclient);
                }
            });

        }

        private static void closeHttpclient(CloseableHttpAsyncClient client) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
