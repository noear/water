package webapp.dso;

import org.noear.snack.ONode;
import org.noear.water.tools.Base64Utils;
import org.noear.water.tools.HttpUtils;

import java.util.*;

public class HeiheiApi {

    private static final String apiUrl = "https://api.jpush.cn/v3/push";
    private static final String masterSecret = "4a8cd168ca71dabcca306cac";
    private static final String appKey ="af9a9da3c73d23aa30ea4af1";

    public static String push(String alias_str, String text) {

        List<String> ary = new ArrayList<>();
        ary.add(alias_str);
        return push(ary, text);
    }

    public static String push(Collection<String> alias_ary, String text)  {
        ONode data = new ONode().build((d)->{
            d.get("platform").val("all");

            d.get("audience").get("alias").addAll(alias_ary);

            d.get("options")
                    .set("apns_production",true);

            d.get("notification").build(n->{
                n.get("android")
                        .set("alert",text);

                n.get("ios")
                        .set("alert",text)
                        .set("badge",0)
                        .set("sound","happy");
            });

            d.get("message").build(n->{
                n.set("msg_content", text);
            });
        });



        String message = data.toJson();
        String author = Base64Utils.encode(appKey+":"+masterSecret);

        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        headers.put("Authorization","Basic "+author);

        try {
            return HttpUtils.http(apiUrl)
                    .headers(headers)
                    .bodyTxt(message,"text/plain")
                    .post();

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return null;
    }
}