package watersev.controller;

import org.noear.solon.annotation.Component;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.protocol.model.message.SubscriberModel;
import org.noear.water.utils.TextUtils;
import watersev.dso.db.DbWaterMsgApi;
import watersev.utils.HttpUtilEx;

import java.util.*;

/**
 * 消息订阅检查（已支持 is_unstable）
 */
@Component
public final class MsgCheckController implements IJob {
    @Override
    public String getName() {
        return "sub";
    }

    @Override
    public int getInterval() {
        return 1000 * 5;
    }

    @Override
    public void exec() throws Exception {
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

    private void check_type0(String url) {
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
                        DbWaterMsgApi.setSubscriberState(url, code);
                    } else {
                        //出错
//                        if (subs.is_unstable && subs.check_error_num >= 2 && !isOk) {
//                            //
//                            // 如果是IP订阅，且出错2次以上，且是网络错误；删掉
//                            //
//                            DbWaterMsgApi.delSubscriber(subs.subscriber_id);
//                        } else {
                        DbWaterMsgApi.setSubscriberState(url, code);
//                        }
                    }
                });
            } catch (Exception ex) { //出错
                DbWaterMsgApi.setSubscriberState(url, 1);
            }
        }
    }
}
