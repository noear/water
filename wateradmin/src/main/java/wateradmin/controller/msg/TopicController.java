package wateradmin.controller.msg;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.MethodType;
import wateradmin.controller.BaseController;
import wateradmin.dso.Session;
import wateradmin.dso.db.DbWaterMsgApi;
import wateradmin.models.water_msg.TopicModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;

@Controller
public class TopicController extends BaseController {
    //主题列表
    @Mapping("/msg/topic")
    public ModelAndView topic(String topic_name, String sort) throws SQLException {
        List<TopicModel> list = DbWaterMsgApi.getTopicList(topic_name, sort);
        viewModel.put("list",list);
        return view("msg/topic");
    }

    //跳转主题添加页面
    @Mapping("/msg/topic/add")
    public ModelAndView topicAdd() {
        viewModel.put("topic", new TopicModel());
        return view("msg/topic_edit");
    }

    //跳转主题编辑页面
    @Mapping("/msg/topic/edit")
    public ModelAndView topicEdit(Integer topic_id) throws SQLException{
        TopicModel topic = DbWaterMsgApi.getTopicByID(topic_id);
        viewModel.put("topic",topic);
        return view("msg/topic_edit");
    }

    //保存主题编辑
    @Mapping("/msg/topic/edit/ajax/save")
    public ViewModel topicEditSave(String topic_name, Integer topic_id, Integer max_msg_num, Integer max_distribution_num, Integer max_concurrency_num, Integer alarm_model) throws SQLException {
        int is_admin = Session.current().getIsAdmin();
        if (is_admin == 1) {
            boolean result = DbWaterMsgApi.updateTopic(topic_name, topic_id, max_msg_num, max_distribution_num, max_concurrency_num, alarm_model);
            if (result) {
                viewModel.code(1, "保存成功！");
            } else {
                viewModel.code(0, "保存失败！");
            }
        } else {
            viewModel.code(0, "没有权限！");
        }

        return viewModel;
    }

    //删除主题
    @Mapping(value = "/msg/topic/edit/ajax/del", method = MethodType.POST)
    public ViewModel topicEditDel(Integer topic_id) throws SQLException {
        int is_admin = Session.current().getIsAdmin();
        if (is_admin == 1) {
            boolean result = DbWaterMsgApi.deleteTopic(topic_id);

            if (result) {
                viewModel.code(1, "删除成功");
            } else {
                viewModel.code(0, "还有使用记录，拒绝删除");
            }
        } else {
            viewModel.code(0, "没有权限！");
        }

        return viewModel;
    }
}
