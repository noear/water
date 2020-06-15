package waterapp.controller;

import org.noear.solon.annotation.XBean;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.TextUtils;
import waterapp.dso.db.DbWaterMsgApi;
import waterapp.models.water_msg.SubscriberModel;
import waterapp.utils.HttpUtilEx;

import java.util.List;

/**
 * 消息订阅检查（已支持 is_unstable）
 */
@XBean
public final class MsgSubController implements IJob {
    @Override
    public String getName() {
        return "sub";
    }

    @Override
    public int getInterval() {
        return 1000 * 30;
    }

    @Override
    public void exec() throws Exception {
        //半夜不做事
        Datetime time = Datetime.Now();
        int hours = time.getHours();
        if (hours > 1 && hours < 6) {
            return;
        }

        //取出待处理的服务
        List<SubscriberModel> list = DbWaterMsgApi.getSubscriberList();

        for (SubscriberModel sev : list) {
            check(sev);
        }
    }

    private void check(SubscriberModel subs) {
        check_type0(subs);
    }

    private void check_type0(SubscriberModel subs) {
        if (TextUtils.isEmpty(subs.receive_url) || TextUtils.isEmpty(subs.subscriber_note)) {
            return;
        }

        String url = subs.receive_url;
        if (url.startsWith("http://")) {
            try {
                /**
                 * callback:
                 * isOk:请求是否成功
                 * code:如果成功，状态码为何?
                 * hint:如果出错，提示信息?
                 */
                HttpUtilEx.getStatusByAsync(url, (isOk, code, hint) -> {
                    if (code >= 200 && code < 400) {
                        //成功
                        DbWaterMsgApi.setSubscriberState(subs.subscriber_id, code);
                    } else {
                        //出错
                        if (subs.is_unstable && subs.check_error_num >= 2 && !isOk) {
                            //
                            // 如果为非稳定服务，且出错2次以上，且是网络错误；删掉
                            //
                            DbWaterMsgApi.delSubscriber(subs.subscriber_id);
                        } else {
                            DbWaterMsgApi.setSubscriberState(subs.subscriber_id, code);
                        }
                    }

//                    ONode n = new ONode();
//                    n.set("isOk", isOk);
//                    n.set("code", code);
//                    n.set("text", hint);
//
//                    LogUtil.debug(this, url, n.toJson());
                });
            } catch (Exception ex) { //出错
                DbWaterMsgApi.setSubscriberState(subs.subscriber_id, 1);
            }
        }
    }
}
