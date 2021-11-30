package wateradmin.controller.msg;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.auth.annotation.AuthPermissions;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.MethodType;
import wateradmin.controller.BaseController;
import wateradmin.dso.*;
import wateradmin.dso.db.DbWaterMsgApi;
import wateradmin.models.ScaleType;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_msg.TopicModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;

@Controller
public class TopicController extends BaseController {
    //主题列表
    @Mapping("/msg/topic")
    public ModelAndView topic(Context ctx, String tag_name) throws SQLException {
        if(SettingUtils.topicScale().ordinal() < ScaleType.medium.ordinal()){
            ctx.redirect("/msg/topic/inner");
            return null;
        }
        List<TagCountsModel> tags = DbWaterMsgApi.getTopicTagList();

        //权限过滤
        TagChecker.filter(tags, m -> m.tag);

        tag_name = TagUtil.build(tag_name, tags);

        viewModel.put("tag_name", tag_name);
        viewModel.put("tags", tags);

        return view("msg/topic");
    }

    //主题列表
    @Mapping("/msg/topic/inner")
    public ModelAndView topic_inner(String tag_name, String topic_name, String sort) throws SQLException {
        if(SettingUtils.topicScale().ordinal() < ScaleType.medium.ordinal()){
            tag_name = null;
        }

        List<TopicModel> list = DbWaterMsgApi.getTopicList(tag_name,topic_name, sort);

        viewModel.put("tag_name", tag_name);
        viewModel.put("list",list);
        return view("msg/topic_inner");
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
    @AuthPermissions(SessionPerms.admin)
    @Mapping("/msg/topic/edit/ajax/save")
    public ViewModel topicEditSave(String tag,String topic_name, Integer topic_id, Integer max_msg_num, Integer max_distribution_num, Integer max_concurrency_num, Integer alarm_model) throws SQLException {
        int is_admin = Session.current().getIsAdmin();
        if (is_admin == 1) {
            boolean result = DbWaterMsgApi.updateTopic(tag,topic_name, topic_id, max_msg_num, max_distribution_num, max_concurrency_num, alarm_model);
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
    @AuthPermissions(SessionPerms.admin)
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
