package wateradmin.controller.mot;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.TagChecker;
import wateradmin.dso.SettingUtils;
import wateradmin.dso.TagUtil;
import wateradmin.dso.db.DbWaterOpsApi;
import wateradmin.dso.db.DbWaterRegApi;
import wateradmin.models.ScaleType;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_reg.ServiceSpeedModel;

import java.sql.SQLException;
import java.util.List;

@Controller
@Mapping("/mot/")
public class SevNodeController extends BaseController {

    //性能监控
    @Mapping("node")
    public ModelAndView node(Context ctx, String tag_name) throws SQLException {
        if (SettingUtils.serviceScale().ordinal() < ScaleType.medium.ordinal()) {
            ctx.forward("/mot/node/inner");
            return null;
        }


        List<TagCountsModel> tags = DbWaterRegApi.getServiceTagList();

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

        return view("mot/node");
    }


    @Mapping("node/inner")
    public ModelAndView inner(String tag_name, String name, String sort) throws SQLException {
        if (SettingUtils.serviceScale().ordinal() < ScaleType.medium.ordinal()) {
            tag_name = null;
        }

        if (name == null) {
            name = "";
        }

        if (SettingUtils.serviceScale() == ScaleType.large) {
            List<TagCountsModel> nameList = DbWaterOpsApi.getNodeServiceNameList(tag_name);

            if(nameList.size() > 0){
                if (Utils.isEmpty(name)) {
                    name = nameList.get(0).tag;
                }

                viewModel.put("tag_name", tag_name);
                viewModel.set("tabs", nameList);
                viewModel.put("tabs_visible", true);
                viewModel.set("name", name);
            }else{
                viewModel.put("tabs_visible", false);
            }
        } else {
            viewModel.put("tabs_visible", false);
        }

        List<ServiceSpeedModel> speeds = DbWaterOpsApi.getNodeSpeedsByName(tag_name, name, sort);

        viewModel.put("tag_name", tag_name);
        viewModel.put("speeds", speeds);
        viewModel.put("serviceName", "_service");
        return view("mot/node_inner");
    }
}
