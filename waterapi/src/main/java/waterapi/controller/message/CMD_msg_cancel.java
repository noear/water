package waterapi.controller.message;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Result;
import org.noear.solon.extend.validation.annotation.NotEmpty;
import org.noear.solon.extend.validation.annotation.Whitelist;
import org.noear.water.utils.TextUtils;
import waterapi.controller.UapiBase;
import waterapi.dso.db.DbWaterMsgApi;
import waterapi.dso.interceptor.Logging;

/**
 * 消息置为取消
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Logging
@Whitelist
@Controller
public class CMD_msg_cancel extends UapiBase {
    /**
     * @param key            消息key
     * @param subscriber_key 订阅者key
     */
    @NotEmpty("key")
    @Mapping("/msg/cancel/")
    public Result cmd_exec(String key, String subscriber_key) throws Exception {

        //如果不需要修改，检查是否已存在
        //
        if (TextUtils.isEmpty(subscriber_key)) {
            DbWaterMsgApi.cancelMessage(key);
        } else {
            DbWaterMsgApi.cancelMsgDistribution(key, subscriber_key);
        }

        return Result.succeed();
    }
}
