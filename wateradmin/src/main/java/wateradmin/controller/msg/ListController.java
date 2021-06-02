package wateradmin.controller.msg;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.Context;
import org.noear.solon.extend.auth.annotation.AuthRoles;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.model.message.DistributionModel;
import org.noear.water.protocol.model.message.MessageModel;
import org.noear.water.protocol.model.message.SubscriberModel;
import org.noear.water.utils.DisttimeUtils;
import wateradmin.controller.BaseController;
import wateradmin.dso.Session;
import wateradmin.dso.SessionRoles;
import wateradmin.dso.db.DbWaterMsgApi;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ListController extends BaseController {

    //消息异常记录
    @Mapping("/msg/list")
    public ModelAndView list(Context ctx, String key) throws Exception {
        Integer _m = ctx.paramAsInt("_m", 0);

        List<MessageModel> list = ProtocolHub.messageSource().getMessageList(_m, key);


        viewModel.put("key",key);
        viewModel.put("_m",_m);
        viewModel.put("list",list);
        viewModel.put("currTime", DisttimeUtils.currTime());
        return view("msg/list");
    }

    //派发功能ajax
    @AuthRoles(SessionRoles.role_admin)
    @Mapping("/msg/ajax/distribute")
    public ViewModel distribute(String ids) throws Exception {
        boolean result = ProtocolHub.messageSource().setMessageAsPending(idList(ids));

        if (result) {
            viewModel.code(1, "派发成功！");
        } else {
            viewModel.code(0, "派发失败！");
        }

        return viewModel;
    }

    //取消派发
    @AuthRoles(SessionRoles.role_admin)
    @Mapping("/msg/ajax/cancelSend")
    public ViewModel cancelSend(String ids) throws Exception {
        boolean result = ProtocolHub.messageSource().setMessageAsCancel(idList(ids));

        if (result) {
            viewModel.code(1, "取消成功");
        } else {
            viewModel.code(0, "取消失败");
        }

        return viewModel;
    }

    //异常记录中 修复订阅功能的ajax
    @AuthRoles(SessionRoles.role_admin)
    @Mapping("/msg/ajax/repair")
    public ViewModel repairSubs(String ids) throws Exception {
        boolean error = false;

        List<DistributionModel> list = ProtocolHub.messageSource().getDistributionListByMsgIds(idList(ids));
        if (!list.isEmpty()) {
            for (DistributionModel dis : list) {
                //查询subscriber的url
                SubscriberModel subs = DbWaterMsgApi.repairSubs2(dis.subscriber_id);
                boolean result = false;
                if (subs.subscriber_id > 0) {
                    //更新distribution的url
                    result = ProtocolHub.messageSource().setDistributionReceiveUrl(dis.dist_id, subs.receive_url);
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

    private List<Object> idList(String ids){
        return  Arrays.asList(ids.split(","))
                .stream()
                .map(s->Long.parseLong(s))
                .collect(Collectors.toList());
    }
}
