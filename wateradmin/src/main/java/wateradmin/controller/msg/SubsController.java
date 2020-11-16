package wateradmin.controller.msg;



import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.Session;
import wateradmin.dso.db.DbWaterMsgApi;
import wateradmin.models.water_msg.SubscriberModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SubsController extends BaseController {
    //订阅列表
    @Mapping("/msg/subs")
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
        viewModel.put("_state",_state);
        return view("msg/subs");
    }

    //ajax删除功能。
    @Mapping("/msg/subs/ajax/delete")
    public ViewModel deleteSubscriber(String ids) throws SQLException {
        int is_admin = Session.current().getIsAdmin();
        if (is_admin==1){
            boolean result = DbWaterMsgApi.deleteSubs(idList(ids));
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
    @Mapping("/msg/subs/ajax/enabled")
    public ViewModel enabledSubscriber(String ids,Integer is_enabled) throws SQLException {
        int is_admin = Session.current().getIsAdmin();
        if (is_admin==1){
            boolean result = DbWaterMsgApi.enabledSubs(idList(ids),is_enabled);
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

    private List<Object> idList(String ids){
        return  Arrays.asList(ids.split(","))
                .stream()
                .map(s->Long.parseLong(s))
                .collect(Collectors.toList());
    }
}
