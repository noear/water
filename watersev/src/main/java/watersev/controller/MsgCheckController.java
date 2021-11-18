package watersev.controller;

import org.noear.solon.annotation.Component;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.WW;
import org.noear.water.protocol.model.message.SubscriberModel;
import org.noear.water.utils.LockUtils;
import org.noear.water.utils.TextUtils;
import watersev.dso.MsgUtils;
import watersev.dso.RegUtil;
import watersev.dso.db.DbWaterMsgApi;
import watersev.utils.HttpUtilEx;

import java.sql.SQLException;
import java.util.*;

/**
 * 消息订阅地址有效性检查（已支持 is_unstable）（可集群，可多实例运行。同时间，只会有一个节点有效）
 *
 * @author noear
 */
@Component
public final class MsgCheckController implements IJob {
    @Override
    public String getName() {
        return "msgchk";
    }

    @Override
    public int getInterval() {
        return 1000 * 5;
    }

    @Override
    public void exec() throws Exception {
        RegUtil.checkin("watersev-" + getName());

        //尝试获取锁（4秒内只能调度一次），避免集群切换时，多次运行
        //
        if (LockUtils.tryLock(WW.watersev_msgchk, WW.watersev_msgchk, 4)) {
            exec0();
        }
    }

    private void exec0() throws SQLException {
        //取出待处理的服务
        List<SubscriberModel> list = DbWaterMsgApi.getSubscriberListNoCache();
        Set<String> subs = new HashSet<>();

        for (SubscriberModel sev : list) {
            //只检查IP的订阅（subs.subscriber_note 必不为空）
            //
            if (TextUtils.isEmpty(sev.receive_url)) {
                continue;
            }

            subs.add(sev.receive_url);
        }

        for (String url : subs) {
            check_type0(url);
        }
    }

    /**
     * 基于异常http检测
     * */
    private void check_type0(String receive_url) {
        try {
            String checkUrl = MsgUtils.getReceiveUrl2(receive_url); //可能会异常

            if (checkUrl.startsWith("http://")) {

                /**
                 * callback:
                 * isOk:请求是否成功
                 * code:如果成功，状态码为何?
                 * hint:如果出错，提示信息?
                 */
                HttpUtilEx.getStatusByAsync(checkUrl, (isOk, code, hint) -> {
                    if (code >= 200 && code < 400) {
                        //成功
                        DbWaterMsgApi.setSubscriberState(receive_url, code);
                    } else {
                        //设置出错状态
                        DbWaterMsgApi.setSubscriberState(receive_url, code);
                        //尝试删除不稳定的出错订阅
                        DbWaterMsgApi.delSubscriberByError(receive_url);
                    }
                });

            }
        } catch (Exception e) {
            //设置出错状态
            DbWaterMsgApi.setSubscriberState(receive_url, 1);
            //尝试删除不稳定的出错订阅
            DbWaterMsgApi.delSubscriberByError(receive_url);
        }
    }
}
