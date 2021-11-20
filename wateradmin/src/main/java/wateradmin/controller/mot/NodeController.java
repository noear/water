package wateradmin.controller.mot;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.BcfTagChecker;
import wateradmin.dso.SetsUtils;
import wateradmin.dso.TagUtil;
import wateradmin.dso.db.DbWaterOpsApi;
import wateradmin.models.ScaleType;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_reg.ServiceSpeedModel;

import java.sql.SQLException;
import java.util.List;

@Controller
@Mapping("/mot/")
public class NodeController extends BaseController {

    private static final String SEV_SERVER_TAG = "_service";

    //性能监控
    @Mapping("node")
    public ModelAndView node(Context ctx, String tag_name) throws SQLException {
        if (SetsUtils.waterSettingScale().ordinal() < ScaleType.medium.ordinal()) {
            ctx.redirect("/mot/node/inner");
            return null;
        }


        List<TagCountsModel> tags = DbWaterOpsApi.getSpeedsServiceTags(SEV_SERVER_TAG);
        //权限过滤
        BcfTagChecker.filter(tags, m -> m.tag);

        tag_name = TagUtil.build(tag_name, tags);

        viewModel.put("tag_name", tag_name);
        viewModel.put("tags", tags);

        return view("mot/node");
    }


    @Mapping("node/inner")
    public ModelAndView inner(String tag_name, String name, String sort) throws SQLException {
        List<ServiceSpeedModel> speeds = DbWaterOpsApi.getSpeedsByServiceAndName(SEV_SERVER_TAG, name, null, sort);
        viewModel.put("speeds", speeds);
        viewModel.put("serviceName", "_service");
        return view("mot/node_inner");
    }
}
