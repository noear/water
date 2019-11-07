package webapp.controller.msg;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.admin.tools.controller.BaseController;
import org.noear.water.admin.tools.viewModels.ViewModel;
import org.noear.water.tools.DisttimeUtil;
import org.noear.water.tools.TextUtils;
import org.noear.water.admin.tools.dso.Session;
import webapp.dao.db.DbWaterMsgApi;
import webapp.models.water_msg.DistributionModel;
import webapp.models.water_msg.MessageModel;
import webapp.models.water_msg.SubscriberModel;

import java.sql.SQLException;
import java.util.List;

@XController
public class WarmController extends BaseController {

    //消息异常记录
    @XMapping("/msg/warm")
    public ModelAndView warm() throws SQLException {
        int topic_id = 0;
        String t = "";
        int dist_count = 0;

        if(TextUtils.isEmpty(t) == false){
            topic_id = DbWaterMsgApi.getTopicID(t).topic_id;
        }

        if(dist_count==0 && topic_id == 0){
            dist_count=4;
        }
        List<MessageModel> list = DbWaterMsgApi.getMessageList(dist_count, topic_id);
        viewModel.put("list",list);
        viewModel.put("currTime", DisttimeUtil.currTime());
        return view("msg/warm");
    }
    //派发功能ajax
    @XMapping("/msg/warm/ajax/distribute")
    public ViewModel distribute(Long msg_id) throws SQLException {
        int is_admin = Session.current().getIsAdmin();
        if (is_admin == 1) {
            boolean result = DbWaterMsgApi.msgDistribute(msg_id);
            if (result) {
                viewModel.code(1, "派发成功！");
            } else {
                viewModel.code(0, "派发失败！");
            }
        } else {
            viewModel.code(0, "没有权限！");
        }

        return viewModel;
    }

    //取消派发
    @XMapping("/msg/warm/ajax/cancelSend")
    public ViewModel cancelSend(Long msg_id) throws SQLException{

        int is_admin = Session.current().getIsAdmin();

        if (is_admin == 1) {
            boolean result = DbWaterMsgApi.cancelSend(msg_id);

            if (result) {
                viewModel.code(1, "取消成功");
            }  else {
                viewModel.code(0, "取消失败");
            }
        } else {
            viewModel.code(0, "没有权限！");
        }

        return viewModel;
    }

    //异常记录中 修复订阅功能的ajax
    @XMapping("/msg/warm/ajax/repair")
    public ViewModel repairSubs(Integer msg_id) throws SQLException {
        int is_admin = Session.current().getIsAdmin();
        if (is_admin == 1) {
            boolean error = false;

            List<DistributionModel> list = DbWaterMsgApi.repairSubs1(msg_id);
            if (!list.isEmpty()) {
                for (DistributionModel dis : list) {
                    //查询subscriber的url
                    SubscriberModel subs = DbWaterMsgApi.repairSubs2(dis.subscriber_id);
                    boolean result = false;
                    if (subs.subscriber_id > 0) {
                        //更新distribution的url
                        result = DbWaterMsgApi.repairSubs3(dis.dist_id, subs.receive_url);
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
        } else {
            viewModel.code(0, "没有权限！");
        }

        return viewModel;
    }
}
