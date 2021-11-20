package wateradmin.controller.mot;

import com.alibaba.fastjson.JSONObject;
import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.core.handle.Context;
import org.noear.water.WaterClient;
import org.noear.water.utils.TextUtils;


import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.BcfServiceChecker;
import wateradmin.dso.BcfTagChecker;
import wateradmin.dso.SetsUtils;
import wateradmin.dso.TagUtil;
import wateradmin.dso.db.DbWaterOpsApi;
import wateradmin.dso.db.DbWaterRegApi;
import wateradmin.models.ScaleType;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_reg.ServiceSpeedModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Controller
@Mapping("/mot/")
public class SpeedController extends BaseController {


    //性能监控
    @Mapping("speed")
    public ModelAndView speed(Context ctx, String tag_name) throws SQLException {
        if (SetsUtils.waterSettingScale().ordinal() < ScaleType.medium.ordinal()) {
            ctx.redirect("/mot/speed/inner");
            return null;
        }


        List<TagCountsModel> tags = DbWaterRegApi.getServiceTagList();

        //权限过滤
        BcfTagChecker.filter(tags, m -> m.tag);

        tag_name = TagUtil.build(tag_name, tags);

        viewModel.put("tag_name", tag_name);
        viewModel.put("tags", tags);

        return view("mot/speed");
    }

    //性能监控-列表
    @Mapping("speed/inner")
    public ModelAndView speedList(String tag_name,String serviceName, String name, String sort, String tag) throws SQLException {
        if (tag == null) {
            tag = "";
        }

        List<TagCountsModel> tabs = DbWaterOpsApi.getSpeedServices(tag_name);
        tabs.removeIf(m -> m.tag.startsWith("_"));
        BcfServiceChecker.filter(tabs, m -> m.tag);
        viewModel.put("tabs", tabs);
        viewModel.put("tag_name", tag_name);


        if (Utils.isEmpty(serviceName)) {
            if (tabs.size() > 0) {
                serviceName = tabs.get(0).tag;
            }
        }

        if (Utils.isEmpty(serviceName)) {
            return null;
        }

        /////////////

        List<ServiceSpeedModel> speeds = DbWaterOpsApi.getSpeedsByServiceAndName(serviceName, tag, name, sort);
        List<TagCountsModel> tags = DbWaterOpsApi.getSpeedsServiceTags(serviceName);


        viewModel.put("speeds", speeds);
        viewModel.put("tags", tags);
        viewModel.put("tag", tag);
        viewModel.put("serviceName", serviceName);

        if ("_waterlog,_waterchk,_watersrt,_watermsg,watercfg".indexOf(serviceName) < 0) {
            return view("mot/speed_inner");
        } else {
            if ("_watersrt".equals(serviceName)) {
                return view("mot/speed_inner3");
            } else {
                return view("mot/speed_inner2");
            }
        }
    }

    //性能监控图标统计
    @Mapping("speed/charts")
    public ModelAndView speedCharts(String tag, String name_md5, String service) throws SQLException {
        if (service == null) {
            service = "";
        }

        Map<String, List> speedReqTate = DbWaterOpsApi.getSpeedForDate(tag, name_md5, service, "total_num");
        Map<String, List> speeds = DbWaterOpsApi.getSpeedForMonth(tag, name_md5, service);
        viewModel.put("speedReqTate", ONode.stringify(speedReqTate));
        viewModel.put("speeds", ONode.stringify(speeds));
        viewModel.put("tag", tag);
        viewModel.put("name", WaterClient.Track.getName(name_md5));
        viewModel.put("name_md5", name_md5);
        viewModel.put("service", service);

        if ("_waterlog,_waterchk,_watersrt,_watermsg,watercfg".indexOf(service) < 0) {
            return view("mot/speed_charts");
        } else {
            if ("_watersrt".equals(service)) {
                return view("mot/speed_charts3");
            } else {
                return view("mot/speed_charts2");
            }
        }
    }

    @Mapping("speed/charts/ajax/reqtate")
    public ViewModel speedCharts_reqtate(String tag, String name_md5, String service, Integer type) throws SQLException {
        String valField = "total_num";
        if (type == null) {
            type = 0;
        }
        switch (type) {
            case 0:
                valField = "total_num";
                break;
            case 1:
                valField = "total_num_slow1";
                break;
            case 2:
                valField = "total_num_slow2";
                break;
            case 3:
                valField = "total_num_slow5";
                break;
            case 4:
                valField = "average";
                break;
            case 5:
                valField = "fastest";
                break;
            case 6:
                valField = "slowest";
                break;
        }

        Map<String, List> speedReqTate = DbWaterOpsApi.getSpeedForDate(tag, name_md5, service, valField);
        viewModel.put("speedReqTate", speedReqTate);
        viewModel.put("tag", tag);
        viewModel.put("name", WaterClient.Track.getName(name_md5));
        viewModel.put("name_md5", name_md5);
        viewModel.put("service", service);
        return viewModel;
    }
}
