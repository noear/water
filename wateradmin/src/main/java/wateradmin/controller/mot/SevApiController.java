package wateradmin.controller.mot;

import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.core.handle.Context;
import org.noear.water.WaterClient;


import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.TagChecker;
import wateradmin.dso.SettingUtils;
import wateradmin.dso.TagUtil;
import wateradmin.dso.db.DbWaterOpsApi;
import wateradmin.dso.db.DbWaterRegApi;
import wateradmin.models.ScaleType;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_sev.ServiceSpeedModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@Mapping("/mot/")
public class SevApiController extends BaseController {


    //性能监控
    @Mapping("speed")
    public ModelAndView speed(Context ctx, String tag_name) throws SQLException {
        if (SettingUtils.serviceScale().ordinal() < ScaleType.medium.ordinal()) {
            ctx.forward("/mot/speed/inner");
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

        return view("mot/speed");
    }

    //性能监控-列表
    @Mapping("speed/inner")
    public ModelAndView speedList(String tag_name, String serviceName, String name, String sort, String tag) throws SQLException {
        if (SettingUtils.serviceScale().ordinal() < ScaleType.medium.ordinal()) {
            tag_name = null;
        }

        if (tag == null) {
            tag = "";
        }

        if (serviceName == null) {
            serviceName = "";
        }

        if (name == null) {
            name = "";
        }

        List<TagCountsModel> services = DbWaterOpsApi.getSpeedServices(tag_name);
        services.removeIf(m -> m.tag.startsWith("_"));
        TagChecker.filter(services, m -> m.tag);

        viewModel.put("tabs", services);
        viewModel.put("tag_name", tag_name);

        if (Utils.isEmpty(serviceName)) {
            if (services.size() > 0) {
                serviceName = services.get(0).tag;
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

        List<String> otherList = Arrays.asList("_waterlog,_waterchk,_watersrt,_watermsg,watercfg".split(","));

        if (otherList.contains(serviceName) == false) {
            if (serviceName.endsWith("_")) {
                return view("mot/speed_inner_gauge");
            } else {
                return view("mot/speed_inner");
            }
        } else {
            if ("_watersrt".equals(serviceName)) {
                return view("mot/speed_inner3");
            } else if ("_waterlog".equals(serviceName)) {
                return view("mot/speed_inner_log");
            } else {
                return view("mot/speed_inner_count");
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

        List<String> otherList = Arrays.asList("_waterlog,_waterchk,_watersrt,_watermsg,watercfg".split(","));

        if (otherList.contains(service) == false) {
            if (service.endsWith("_")) {
                return view("mot/speed_charts_gauge");
            } else {
                return view("mot/speed_charts");
            }
        } else {
            if ("_watersrt".equals(service)) {
                return view("mot/speed_charts3");
            } else if ("_waterlog".equals(service)) {
                return view("mot/speed_charts_log");
            } else {
                return view("mot/speed_charts_count");
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
