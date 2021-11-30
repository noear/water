package wateradmin.controller.msg;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.auth.annotation.AuthPermissions;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.Context;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.model.message.DistributionModel;
import org.noear.water.protocol.model.message.MessageModel;
import org.noear.water.protocol.model.message.SubscriberModel;
import org.noear.water.utils.DisttimeUtils;
import org.noear.water.utils.TextUtils;
import wateradmin.controller.BaseController;
import wateradmin.dso.SessionPerms;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.dso.db.DbWaterMsgApi;
import wateradmin.models.TagCountsModel;
import wateradmin.viewModels.ViewModel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ListController extends BaseController {

    //消息异常记录
    @Mapping("/msg/list")
    public ModelAndView list(Context ctx, String broker, String key) throws Exception {
        Integer _m = ctx.paramAsInt("_m", 0);

        if (TextUtils.isNotEmpty(broker)) {
            broker = broker.trim();
        }

        if (TextUtils.isNotEmpty(key)) {
            key = key.trim();
        }

        if (TextUtils.isEmpty(broker)) {
            broker = ctx.cookie("wateradmin_msg__broker");
        } else {
            ctx.cookieSet("wateradmin_msg__broker", broker);
        }

        List<TagCountsModel> brokerList = DbWaterCfgApi.getBrokerNameTags();
        List<MessageModel> list = ProtocolHub.getMsgSource(broker).getMessageList(_m, key);


        viewModel.put("key", key);
        viewModel.put("_m", _m);
        viewModel.put("list", list);
        viewModel.put("broker", broker);
        viewModel.put("brokerList", brokerList);
        viewModel.put("currTime", DisttimeUtils.currTime());
        return view("msg/list");
    }

    //派发功能ajax
    @AuthPermissions(SessionPerms.admin)
    @Mapping("/msg/ajax/distribute")
    public ViewModel distribute(String broker, String ids) throws Exception {
        boolean result = ProtocolHub.getMsgSource(broker).setMessageAsPending(idList(ids));

        if (result) {
            viewModel.code(1, "派发成功！");
        } else {
            viewModel.code(0, "派发失败！");
        }

        return viewModel;
    }

    //取消派发
    @AuthPermissions(SessionPerms.admin)
    @Mapping("/msg/ajax/cancelSend")
    public ViewModel cancelSend(String broker, String ids) throws Exception {
        boolean result = ProtocolHub.getMsgSource(broker).setMessageAsCancel(idList(ids));

        if (result) {
            viewModel.code(1, "取消成功");
        } else {
            viewModel.code(0, "取消失败");
        }

        return viewModel;
    }

    //异常记录中 修复订阅功能的ajax
    @AuthPermissions(SessionPerms.admin)
    @Mapping("/msg/ajax/repair")
    public ViewModel repairSubs(String broker, String ids) throws Exception {
        boolean error = false;

        List<DistributionModel> list = ProtocolHub.getMsgSource(broker).getDistributionListByMsgIds(idList(ids));
        if (!list.isEmpty()) {
            for (DistributionModel dis : list) {
                //查询subscriber的url
                SubscriberModel subs = DbWaterMsgApi.repairSubs2(dis.subscriber_id);
                boolean result = false;
                if (subs.subscriber_id > 0) {
                    //更新distribution的url
                    result = ProtocolHub.getMsgSource(broker).setDistributionReceiveUrl(dis.dist_id, subs.receive_url);
                }

                if (!result) {
                    error = true;
                }
            }
        } else {
            error = true;
        }

        if (error == false) {
            viewModel.code(1, "修复成功！");
        } else {
            viewModel.code(0, "修复失败！");
        }

        return viewModel;
    }

    private List<Object> idList(String ids) {
        return Arrays.asList(ids.split(","))
                .stream()
                .map(s -> Long.parseLong(s))
                .collect(Collectors.toList());
    }
}
