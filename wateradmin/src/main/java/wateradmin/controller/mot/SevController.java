package wateradmin.controller.mot;

import org.noear.snack.ONode;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.auth.annotation.AuthRoles;
import org.noear.water.utils.HttpUtils;
import org.noear.water.utils.TextUtils;
import wateradmin.controller.BaseController;
import wateradmin.dso.BcfTagChecker;
import wateradmin.dso.SessionRoles;
import wateradmin.dso.SetsUtils;
import wateradmin.dso.TagUtil;
import wateradmin.dso.db.DbWaterRegApi;
import wateradmin.models.ScaleType;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_reg.ServiceModel;
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
        if(SetsUtils.waterSettingScale().ordinal() < ScaleType.medium.ordinal()){
            ctx.redirect("/mot/service/inner");
            return null;
        }

        List<TagCountsModel> tags = DbWaterRegApi.getServiceTagList();

        //权限过滤
        BcfTagChecker.filter(tags, m -> m.tag);

        tag_name = TagUtil.build(tag_name, tags);

        viewModel.put("tag_name", tag_name);
        viewModel.put("tags", tags);

        return view("mot/sev");
    }

    //服务状态
    @Mapping("/service/inner")
    public ModelAndView inner(String tag_name, String name,Integer _state) throws SQLException {
        if (_state != null) {
            viewModel.put("_state", _state);
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

        List<ServiceModel> services = DbWaterRegApi.getServices(tag_name, name, _state);

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

        String ca = s.split("@")[1];

        String url = "http://" + ca + "/run/status/";

        try {
            return HttpUtils.http(url).get();
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
            return HttpUtils.http(url).get();
        } catch (Throwable ex) {
            return "The service unsupported";
        }
    }

    //页面自动刷新获取表单数据
    @Mapping("/service/ajax/service_table")
    public ModelAndView manageS_table(String tag_name,String name, Integer _state, String _type) throws SQLException {
        if (_state != null) {
            viewModel.put("_state", _state);
            int state = _state;
            if (state == 0) {
                _state = 1;
            } else if (state == 1) {
                _state = 0;
            }
        }
        if (_state == null)
            _state = 1;
        List<ServiceModel> services = DbWaterRegApi.getServices(tag_name, name, _state);
        viewModel.put("services", services);
        return view("mot/sev_inner_table");
    }

    //删除服务
    @AuthRoles(SessionRoles.role_admin)
    @Mapping("/service/ajax/deleteService")
    public ViewModel deleteServiceById(Integer service_id) throws SQLException {
        ServiceModel sev = DbWaterRegApi.getServiceById(service_id);
        boolean result = DbWaterRegApi.deleteServiceById(service_id);

        //删除消费者记录
        DbWaterRegApi.delConsumer(sev.address);

        if (result) {
            viewModel.code(1, "删除成功！");
        } else {
            viewModel.code(0, "删除失败！");
        }

        return viewModel;
    }

    //启用 | 禁用 服务
    @AuthRoles(SessionRoles.role_admin)
    @Mapping("/service/ajax/disable")
    public ViewModel disable(Integer service_id,Integer is_enabled) throws SQLException {

        boolean result = DbWaterRegApi.disableService(service_id,is_enabled);
        if (result){
            viewModel.code(1,"操作成功！");
        }else{
            viewModel.code(0,"操作失败！");
        }

        return viewModel;
    }

    //服务状态
    @Mapping("/service/edit")
    public ModelAndView service_edit(Integer service_id) throws SQLException {
        ServiceModel model = new ServiceModel();
        if(service_id!=null) {
            model = DbWaterRegApi.getServiceById(service_id);
        }

        viewModel.put("model", model);
        viewModel.put("service_id",service_id);

        return view("mot/sev_edit");
    }

    @AuthRoles(SessionRoles.role_admin)
    @Mapping("/service/edit/ajax/save")
    public ViewModel service_edit_ajax_save(Integer service_id,String name,String address,String note,Integer check_type,String check_url) throws SQLException {
        boolean result = DbWaterRegApi.udpService(service_id, name, address, note, check_type, check_url);

        if (result) {
            viewModel.code(1, "保存成功！");
        } else {
            viewModel.code(0, "保存失败！");
        }

        return viewModel;
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
    public ViewModel speedCharts_reqtate(String key, Integer type) throws SQLException{
        String valField = "memory_used";
        if(type == null){type=0;}
        switch (type){
            case 0:valField ="memory_used"; break;
            case 1:valField ="memory_total"; break;
            case 2:valField ="memory_max"; break;
            case 3:valField ="thread_peak_count"; break;
            case 4:valField ="thread_count"; break;
            case 5:valField ="thread_daemon_count"; break;
        }

        Map<String,List> speedReqTate = DbWaterRegApi.getChartsForDate(key, valField);
        viewModel.put("speedReqTate",speedReqTate);
        viewModel.put("key",key);
        viewModel.put("typeName",valField);
        return viewModel;
    }
}
