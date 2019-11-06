package webapp.controller.msg;



import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import webapp.controller.BaseController;
import webapp.dao.Session;
import webapp.dao.db.DbWaterMsgApi;
import webapp.models.water_msg.SubscriberModel;
import webapp.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:Fei.chu
 * @Description:消息相关
 */

@XController
public class SubsController extends BaseController {
    //订阅列表
    @XMapping("/msg/subs")
    public ModelAndView subscriber(String topic_name,Integer _state) throws SQLException{

        if (_state != null) {
            int state = _state;
            if (state == 0) {
                _state = 1;
            } else if (state == 1) {
                _state = 0;
            }
        }
        if (_state == null)
            _state = 1;

        List<SubscriberModel> list2 = DbWaterMsgApi.getSubscriberList(topic_name,_state);
        List<SubscriberModel> list = new ArrayList<>(list2.size());
        for(SubscriberModel m: list2) {
            if ("".equals(m.trClass())) {
                list.add(m);
            } else {
                list.add(0, m);
            }
        }

        viewModel.put("list",list);
        return view("msg/subs");
    }

    //ajax删除功能。
    @XMapping("/msg/subs/ajax/delete")
    public ViewModel deleteSubscriber(Integer subscriber_id) throws SQLException {
        int is_admin = Session.current().getIsAdmin();
        if (is_admin==1){
            boolean result = DbWaterMsgApi.deleteSubs(subscriber_id);
            if (result){
                viewModel.code(1,"删除成功！");
            }else{
                viewModel.code(0,"删除失败！");
            }
        }else{
            viewModel.code(0,"没有权限！");
        }

        return viewModel;
    }

    //订阅主题 启用/禁用功能。
    @XMapping("/msg/subs/ajax/enabled")
    public ViewModel deleteSubscriber(Integer subscriber_id,Integer is_enabled) throws SQLException {
        int is_admin = Session.current().getIsAdmin();
        if (is_admin==1){
            boolean result = DbWaterMsgApi.enabledSubs(subscriber_id,is_enabled);
            if (result){
                viewModel.code(1,"操作成功！");
            }else{
                viewModel.code(0,"操作失败！");
            }
        }else{
            viewModel.code(0,"没有权限！");
        }

        return viewModel;
    }
}
