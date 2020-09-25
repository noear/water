package waterapi.controller.msg;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XResult;
import org.noear.solon.extend.validation.annotation.NotEmpty;
import org.noear.solon.extend.validation.annotation.Whitelist;
import org.noear.water.utils.TextUtils;
import waterapi.controller.UapiBase;
import waterapi.dso.db.DbWaterMsgApi;

/**
 * 消息置为取消
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Whitelist
@XController
public class CMD_msg_cancel extends UapiBase {
    /**
     * @param key            消息key
     * @param subscriber_key 订阅者key
     */
    @NotEmpty("key")
    @XMapping("/msg/cancel/")
    public XResult cmd_exec(String key, String subscriber_key) throws Exception {

        //如果不需要修改，检查是否已存在
        //
        if (TextUtils.isEmpty(subscriber_key)) {
            DbWaterMsgApi.cancelMessage(key);
        } else {
            DbWaterMsgApi.cancelMsgDistribution(key, subscriber_key);
        }

        return XResult.succeed();
    }
}
