package waterapi.controller.message;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Result;
import org.noear.solon.extend.validation.annotation.NotEmpty;
import org.noear.solon.extend.validation.annotation.Whitelist;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.utils.TextUtils;
import waterapi.controller.UapiBase;
import waterapi.dso.interceptor.Logging;

/**
 * 消息置为成功
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Logging
@Whitelist
@Controller
public class CMD_msg_succeed extends UapiBase {

    /**
     * @param key            消息key
     * @param subscriber_key 订阅者key
     */
    @NotEmpty("key")
    @Mapping("/msg/succeed/")
    protected Result cmd_exec(String key, String subscriber_key) throws Exception {

        if (TextUtils.isEmpty(subscriber_key)) {
            ProtocolHub.messageSource().setMessageAsSucceed(key);
        } else {
            ProtocolHub.messageSource().setDistributionAsSucceed(key, subscriber_key);
        }

        return Result.succeed();
    }
}