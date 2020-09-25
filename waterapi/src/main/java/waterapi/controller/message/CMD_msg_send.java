package waterapi.controller.message;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XResult;
import org.noear.solon.extend.validation.annotation.NotEmpty;
import org.noear.solon.extend.validation.annotation.Whitelist;
import org.noear.water.WW;
import org.noear.water.utils.DisttimeUtils;
import waterapi.controller.UapiBase;
import waterapi.dso.db.DbWaterMsgApi;
import waterapi.dso.interceptor.Logging;

import java.util.Date;

/**
 * 消息发送
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Logging
@Whitelist
@XController
public class CMD_msg_send extends UapiBase {
    /**
     * @param key       消息key（派发时会回传）
     * @param topic     消息主题
     * @param message   消息内容
     * @param plan_time 分发时间(yyyy-MM-dd HH:mm:ss)
     * @param tags      查询标签
     */
    @NotEmpty({"key", "topic", "message"})
    @XMapping("/msg/send/")
    public XResult cmd_exec(XContext ctx, String key, String topic, String message, String plan_time, String tags) throws Exception {

        //如果不需要修改，检查是否已存在
        //
        if (DbWaterMsgApi.hasMessage(key)) {
            return XResult.succeed();
        }

        Date plan_time2 = DisttimeUtils.parse(plan_time);
        String trace_id = ctx.header(WW.http_header_trace);

        if (DbWaterMsgApi.addMessage(key, trace_id, tags, topic, message, plan_time2) > 0) {
            return XResult.succeed();
        } else {
            return XResult.failure();
        }
    }
}
