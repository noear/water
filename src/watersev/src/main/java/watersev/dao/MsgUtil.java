package watersev.dao;

import watersev.models.water.ServiceModel;
import watersev.models.water_msg.MessageModel;

/**
 * Created by yuety on 2017/7/27.
 */
public class MsgUtil {
    private static String Url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
    private static String sms_appid="C03302224";
    private static String sms_appkey="92ef3f7a1fb186a6fe22bdaf06fc8375";

    public static boolean sendAlarmCode(String phone, MessageModel msg) {
//        List<NameValuePair> params = new ArrayList<>();
//        params.add(new BasicNameValuePair("account", sms_appid));
//        params.add(new BasicNameValuePair("password", sms_appkey));
//        params.add(new BasicNameValuePair("mobile", phone));

        StringBuilder sb = new StringBuilder();
        sb.append("报警提示：有异常消息=").append(msg.topic_name).append("，").append("#")
                .append(msg.msg_id).append("已派发").append(msg.dist_count).append("次失败");

        HeiheiApi.push(phone, sb.toString());

//        params.add(new BasicNameValuePair("content", sb.toString()));
//
//        try {
//            HttpUtil.postStringByAsync(Url, params, (isOk, code, text) -> {
//
//            });
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }


        return true;
    }

    public static boolean sendAlarmCode(String phone, ServiceModel sev) {
//        List<NameValuePair> params = new ArrayList<>();
//        params.add(new BasicNameValuePair("account", sms_appid));
//        params.add(new BasicNameValuePair("password", sms_appkey));
//        params.add(new BasicNameValuePair("mobile", phone));

        StringBuilder sb = new StringBuilder();
        sb.append("报警提示：有异常服务=").append(sev.name).append("@").append(sev.address).append("，").append(sev.note);

        HeiheiApi.push(phone, sb.toString());

//        params.add(new BasicNameValuePair("content", sb.toString()));
//
//        try {
//            HttpUtil.postStringByAsync(Url, params, (isOk, code, text) -> {
//
//            });
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }


        return true;
    }
}
