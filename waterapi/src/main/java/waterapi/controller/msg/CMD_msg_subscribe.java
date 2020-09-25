package waterapi.controller.msg;

import org.noear.solon.XUtil;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XResult;
import org.noear.solon.extend.validation.annotation.NotEmpty;
import waterapi.controller.UapiBase;
import waterapi.controller.UapiCodes;
import waterapi.dso.db.DbWaterMsgApi;

/**
 * 消息订阅
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@XController
public class CMD_msg_subscribe extends UapiBase {

    /**
     * @param key         订阅者唯一key
     * @param note        订阅者描述
     * @param is_unstable 是否为不稳定订阅者
     * @param topic       消息主题(多个主题以","隔开)
     * @param receive_url 接收地址url
     * @param receive_way 接收方式(0,1,2)
     * @param access_key  访问密钥（消息包签名用）
     */
    @NotEmpty({"key", "topic", "access_key"})
    @XMapping("/msg/subscribe/")
    public XResult cmd_exec(XContext ctx, String key, String note, int is_unstable, String topic, String receive_url, int receive_way, String access_key) throws Exception {
        if(receive_url == null){
            receive_url = ctx.param("receiver_url"); //**兼容旧版变量名。by 2020.09
        }

        if(XUtil.isEmpty(receive_url)){
            throw UapiCodes.CODE_13("receive_url");
        }

        String alarm_mobile = ctx.param("alarm_mobile", "");//订阅者的报敬接收手机号

        boolean isOk = true;
        for (String topic2 : topic.split(",")) {//多个主题以","隔开
            if (topic2.length() > 0) {
                isOk = isOk & do_subscprebe(key, note, alarm_mobile, topic2, receive_url, access_key, receive_way, is_unstable > 0);
            }
        }

        if (isOk) {
            return XResult.succeed();
        } else {
            return XResult.failure();
        }
    }

    private boolean do_subscprebe(String key, String note, String alarm_mobile, String topic, String receive_url, String access_key, int receive_way, boolean is_unstable) throws Exception {
        if (receive_url.indexOf("://") < 0) { //如果不是url不能订阅
            return false;
        }

        if (DbWaterMsgApi.hasSubscriber(key, topic)) {
            DbWaterMsgApi.udpSubscriber(key, note, alarm_mobile, topic, receive_url, access_key, receive_way, is_unstable);
            return true;
        }

        if (DbWaterMsgApi.addSubscriber(key, note, alarm_mobile, topic, receive_url, access_key, receive_way, is_unstable) > 0) {
            return true;
        }

        return false;
    }
}
