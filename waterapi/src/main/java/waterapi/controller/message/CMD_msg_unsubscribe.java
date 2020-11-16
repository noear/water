package waterapi.controller.message;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.extend.validation.annotation.NotEmpty;
import org.noear.solon.extend.validation.annotation.Whitelist;
import waterapi.controller.UapiBase;
import waterapi.controller.UapiCodes;
import waterapi.dso.db.DbWaterMsgApi;
import waterapi.dso.interceptor.Logging;

/**
 * 取消订阅
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Logging
@Whitelist
@Controller
public class CMD_msg_unsubscribe extends UapiBase {

    /**
     * @param subscriber_key 订阅者key
     * @param topic 消息主题
     * */
    @NotEmpty("topic")
    @Mapping("/msg/unsubscribe/")
    public Result cmd_exec(Context ctx, String subscriber_key, String topic) throws Exception {
        if(Utils.isEmpty(subscriber_key)){
            subscriber_key = ctx.param("key");//**兼容旧版变量名。by 2020.09
        }

        if (Utils.isEmpty(subscriber_key)) {
            throw UapiCodes.CODE_13("subscriber_key");
        }

        boolean isOk = true;
        for (String topic2 : topic.split(",")) {//多个主题以","隔开
            if (topic2.length() > 0) {
                isOk = isOk & do_unsubscprebe(subscriber_key, topic2);
            }
        }

        if(isOk) {
            return Result.succeed();
        }else{
            return Result.failure();
        }
    }

    private boolean do_unsubscprebe(String key, String topic) throws Exception {
        return DbWaterMsgApi.removeSubscriber(key, topic);
    }
}
