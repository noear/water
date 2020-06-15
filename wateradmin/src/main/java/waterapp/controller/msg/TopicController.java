package waterapp.controller.msg;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import waterapp.controller.BaseController;
import waterapp.dso.Session;
import waterapp.dso.db.DbWaterMsgApi;
import waterapp.models.water_msg.TopicModel;
import waterapp.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;

@XController
public class TopicController extends BaseController {
    //主题列表
    @XMapping("/msg/topic")
    public ModelAndView topic(String topic_name) throws SQLException {
        List<TopicModel> list = DbWaterMsgApi.getTopicList(topic_name);
        viewModel.put("list",list);
        return view("msg/topic");
    }

    //跳转主题添加页面
    @XMapping("/msg/topic/add")
    public ModelAndView topicAdd() {
        viewModel.put("topic", new TopicModel());
        return view("msg/topic_edit");
    }

    //跳转主题编辑页面
    @XMapping("/msg/topic/edit")
    public ModelAndView topicEdit(Integer topic_id) throws SQLException{
        TopicModel topic = DbWaterMsgApi.getTopicByID(topic_id);
        viewModel.put("topic",topic);
        return view("msg/topic_edit");
    }

    //保存主题编辑
    @XMapping("/msg/topic/edit/ajax/save")
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
}
