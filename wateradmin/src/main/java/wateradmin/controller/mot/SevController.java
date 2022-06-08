package wateradmin.controller.mot;

import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.water.WaterProxy;
import org.noear.water.utils.HttpUtils;
import org.noear.water.utils.TextUtils;
import wateradmin.controller.BaseController;
import wateradmin.dso.TagChecker;
import wateradmin.dso.SettingUtils;
import wateradmin.dso.TagUtil;
import wateradmin.dso.db.DbWaterRegApi;
import wateradmin.models.ScaleType;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_sev.ServiceModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Controller
@Mapping("/mot/")
public class SevController extends BaseController {

    //服务状态
    @Mapping("/service")
    public ModelAndView sev(Context ctx, String tag_name) throws SQLException {
        if (SettingUtils.serviceScale().ordinal() < ScaleType.medium.ordinal()) {
            ctx.forward("/mot/service/inner");
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

        return view("mot/sev");
    }

    //服务状态
    @Mapping("/service/inner")
    public ModelAndView inner(String tag_name, String name, int _state) throws SQLException {
        if(SettingUtils.serviceScale().ordinal() < ScaleType.medium.ordinal()){
            tag_name = null;
        }


        if(SettingUtils.serviceScale() == ScaleType.large){
            List<TagCountsModel> nameList = DbWaterRegApi.getServiceNameList(tag_name);

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


        List<ServiceModel> services = DbWaterRegApi.getServices(tag_name, name, _state == 0);

        viewModel.put("_state", _state);
        viewModel.put("tag_name", tag_name);
        viewModel.put("services", services);
        viewModel.put("name", name);

        return view("mot/sev_inner");
    }

    @Mapping("/service/status")
    public String service_status(String s) throws Exception {
        if (TextUtils.isEmpty(s)) {
            return "Not supported";
        }

        if (s.indexOf("@") < 0 || s.indexOf(":") < 0) {
            return "Not supported";
        }

        String addrees = s.split("@")[1];

        try {
            return WaterProxy.runStatus(addrees);
        } catch (Throwable ex) {
            return "The service unsupported";
        }
    }

    @Mapping("/service/check")
    public String service_check(String s) throws Exception {
        if (TextUtils.isEmpty(s)) {
            return "Not supported";
        }

        String[] ss = s.split("@");

        if (ss.length < 3) {
            return "Not supported";
        }

        String sip = ss[1];
        String sid = ss[2];

        ServiceModel sev = DbWaterRegApi.getServiceById(Long.parseLong(sid));

        if (TextUtils.isEmpty(sev.check_url)) {
            return "Not supported";
        }

        String url = null;
        if (sev.check_url.startsWith("/")) {
            url = "http://" + sip + sev.check_url;
        } else {
            url = "http://" + sip + "/" + sev.check_url;
        }

        try {
            return HttpUtils.shortHttp(url).get();
        } catch (Throwable ex) {
            return "The service unsupported";
        }
    }

    //页面自动刷新获取表单数据
    @Mapping("/service/ajax/service_table")
    public ModelAndView service_table(String tag_name, String name, int _state, String _type) throws SQLException {

        List<ServiceModel> services = DbWaterRegApi.getServices(tag_name, name, _state == 0);

        viewModel.put("_state", _state);
        viewModel.put("services", services);
        return view("mot/sev_inner_table");
    }

    //性能监控图标统计
    // todo: 未完成
    @Mapping("/service/charts")
    public ModelAndView speedCharts(String key) throws SQLException {
        if (key == null) {
            key = "";
        }

        ServiceModel sev = DbWaterRegApi.getServiceByKey(key);

        Map<String, List> speedReqTate = DbWaterRegApi.getChartsForDate(key, "memory_used"); //total_num
        Map<String, List> speeds = DbWaterRegApi.getChartsForMonth(key);
        viewModel.put("speedReqTate", ONode.stringify(speedReqTate));
        viewModel.put("speeds", ONode.stringify(speeds));
        viewModel.put("key", key);
        viewModel.put("service", sev.name);
        viewModel.put("address", sev.getAddress());

        return view("mot/sev_charts");
    }

    // todo: 未完成
    @Mapping("/service/charts/ajax/reqtate")
    public ViewModel speedCharts_reqtate(String key, Integer type) throws SQLException {
        String valField = "memory_used";
        if (type == null) {
            type = 0;
        }
        switch (type) {
            case 0:
                valField = "memory_used";
                break;
            case 1:
                valField = "memory_total";
                break;
            case 2:
                valField = "memory_max";
                break;
            case 3:
                valField = "thread_peak_count";
                break;
            case 4:
                valField = "thread_count";
                break;
            case 5:
                valField = "thread_daemon_count";
                break;
        }

        Map<String, List> speedReqTate = DbWaterRegApi.getChartsForDate(key, valField);
        viewModel.put("speedReqTate", speedReqTate);
        viewModel.put("key", key);
        viewModel.put("typeName", valField);
        return viewModel;
    }
}
