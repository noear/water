package wateradmin.controller.msg;



import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.auth.annotation.AuthPermissions;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.water.protocol.model.message.SubscriberModel;
import wateradmin.controller.BaseController;
import wateradmin.dso.*;
import wateradmin.dso.db.DbWaterMsgApi;
import wateradmin.models.ScaleType;
import wateradmin.models.TagCountsModel;
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
    public ModelAndView subscriber(Context ctx, String tag_name) throws SQLException{
        if(SettingUtils.topicScale().ordinal() < ScaleType.medium.ordinal()){
            ctx.forward("/msg/subs/inner");
            return null;
        }

        List<TagCountsModel> tags = DbWaterMsgApi.getSubscriberTagList();

        //权限过滤
        TagChecker.filter(tags, m -> m.tag);

        //处理空标签
        tags.forEach(t -> {
            if (Utils.isEmpty(t.tag)) {
                t.tag = "_";
            }
        });

        tag_name = TagUtil.build(tag_name, tags);

        viewModel.put("tag_name", tag_name);
        viewModel.put("tags", tags);


        return view("msg/subs");
    }

    //订阅列表
    @Mapping("/msg/subs/inner")
    public ModelAndView subs_inner(String tag_name, String name, String topic_name,Integer _state) throws SQLException {
        if (SettingUtils.topicScale().ordinal() < ScaleType.medium.ordinal()) {
            tag_name = null;
        }

        //如果服务的规模很大，则用服务分
        if(SettingUtils.serviceScale() == ScaleType.large){
            List<TagCountsModel> nameList = DbWaterMsgApi.getSubscriberNameList(tag_name);

            if(Utils.isEmpty(name)){
                if(nameList.size() > 0){
                    name = nameList.get(0).tag;
                }
            }

            viewModel.put("tag_name",tag_name);
            viewModel.set("tabs", nameList);
            viewModel.put("tabs_visible",true);
            viewModel.set("name", name);
        }else{
            viewModel.put("tabs_visible",false);
        }


        if (_state != null) {
            int state = _state;
            if (state == 0) {
                _state = 1;
            } else if (state == 1) {
                _state = 0;
            }
        }
        if (_state == null) {
            _state = 1;
        }

        List<SubscriberModel> list2 = DbWaterMsgApi.getSubscriberList(tag_name, name, topic_name, _state);
        List<SubscriberModel> list = new ArrayList<>(list2.size());
        for (SubscriberModel m : list2) {
            if ("".equals(m.trClass())) {
                list.add(m);
            } else {
                list.add(0, m);
            }
        }

        viewModel.put("tag_name", tag_name);
        viewModel.put("list", list);
        viewModel.put("_state", _state);
        return view("msg/subs_inner");
    }

    //ajax删除功能。
    @AuthPermissions(SessionPerms.admin)
    @Mapping("/msg/subs/ajax/delete")
    public ViewModel deleteSubscriber(String ids) throws SQLException {
        boolean result = DbWaterMsgApi.deleteSubs(idList(ids));

        if (result) {
            viewModel.code(1, "删除成功！");
        } else {
            viewModel.code(0, "删除失败！");
        }

        return viewModel;
    }

    //订阅主题 启用/禁用功能。
    @AuthPermissions(SessionPerms.admin)
    @Mapping("/msg/subs/ajax/enabled")
    public ViewModel enabledSubscriber(String ids,Integer is_enabled) throws SQLException {
        boolean result = DbWaterMsgApi.enabledSubs(idList(ids), is_enabled);

        if (result) {
            viewModel.code(1, "操作成功！");
        } else {
            viewModel.code(0, "操作失败！");
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
