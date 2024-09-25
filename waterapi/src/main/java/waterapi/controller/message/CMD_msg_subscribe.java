package waterapi.controller.message;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Whitelist;
import waterapi.controller.UapiBase;
import waterapi.controller.UapiCodes;
import waterapi.dso.db.DbWaterMsgApi;
import waterapi.dso.interceptor.Logging;

/**
 * 消息订阅
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Logging
@Whitelist
@Controller
public class CMD_msg_subscribe extends UapiBase {

    /**
     * @param subscriber_key  订阅者 key
     * @param subscriber_name 订阅者 name
     * @param subscriber_tag  订阅者 tag
     * @param is_unstable     是否为不稳定订阅者
     * @param topic           消息主题(多个主题以","隔开)
     * @param receive_url     接收地址url
     * @param receive_way     接收方式(1,2,3)
     * @param receive_key     接收密钥（消息包签名用）
     */
    @NotEmpty({"topic"})
    @Mapping("/msg/subscribe/")
    public Result cmd_exec(Context ctx, String subscriber_key, String subscriber_name, String subscriber_tag, int is_unstable, String topic, String receive_url, int receive_way, String receive_key) throws Exception {
        if (receive_url == null) {
            receive_url = ctx.param("receiver_url"); //**兼容旧版变量名。by 2020.09
        }

        //验证接收地址
        if (Utils.isEmpty(receive_url)) {
            throw UapiCodes.CODE_13("receive_url");
        }

        if (Utils.isEmpty(receive_key)) {
            receive_key = ctx.param("access_key");  //**兼容旧版变量名。by 2020.09
        }

        //验证接收签名密钥
        if (Utils.isEmpty(receive_key)) {
            throw UapiCodes.CODE_13("receive_key");
        }


        if (Utils.isEmpty(subscriber_key)) {
            subscriber_key = ctx.param("key");  //**兼容旧版变量名。by 2020.09
        }

        if (Utils.isEmpty(subscriber_name)) {
            subscriber_name = ctx.param("note");  //**兼容旧版变量名。by 2020.09
        }


        String alarm_mobile = ctx.paramOrDefault("alarm_mobile", "");//订阅者的报敬接收手机号

        boolean isOk = true;
        for (String topic2 : topic.split(",")) {//多个主题以","隔开
            topic2 = topic2.trim();
            if (topic2.length() > 0) {
                isOk = isOk & subscribeDo(subscriber_key, subscriber_name, subscriber_tag, alarm_mobile, topic2, receive_url, receive_key, receive_way, is_unstable > 0);
            }
        }

        if (isOk) {
            return Result.succeed();
        } else {
            return Result.failure();
        }
    }

    private boolean subscribeDo(String key, String name, String tag, String alarm_mobile, String topic, String receive_url, String receive_key, int receive_way, boolean is_unstable) throws Exception {
        if (receive_url.contains("://") || receive_url.startsWith("@")) {
            //如果不是url 或不是服务名 不能订阅
            if (DbWaterMsgApi.addSubscriber(key, name, tag, alarm_mobile, topic, receive_url, receive_key, receive_way, is_unstable) > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
