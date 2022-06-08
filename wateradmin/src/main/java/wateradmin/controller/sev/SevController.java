package wateradmin.controller.sev;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.auth.annotation.AuthPermissions;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.water.WaterProxy;
import org.noear.water.utils.HttpUtils;
import org.noear.water.utils.TextUtils;
import wateradmin.controller.BaseController;
import wateradmin.dso.SessionPerms;
import wateradmin.dso.SettingUtils;
import wateradmin.dso.TagChecker;
import wateradmin.dso.TagUtil;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.dso.db.DbWaterRegApi;
import wateradmin.models.ScaleType;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_sev.ServiceModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;

@Controller
@Mapping("/sev/service")
public class SevController extends BaseController {

    //服务状态
    @Mapping("")
    public ModelAndView sev(String tag_name) throws SQLException {
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

        return view("sev/sev");
    }

    //服务状态
    @Mapping("inner")
    public ModelAndView inner(String tag_name, String name, int _state) throws SQLException {
        if (SettingUtils.serviceScale() == ScaleType.large) {
            List<TagCountsModel> nameList = DbWaterRegApi.getServiceNameList(tag_name);

            if (Utils.isEmpty(name)) {
                if (nameList.size() > 0) {
                    name = nameList.get(0).tag;
                }
            }

            viewModel.set("tabs", nameList);
            viewModel.put("tabs_visible", true);
            viewModel.set("name", name);
        } else {
            viewModel.put("tabs_visible", false);
        }


        List<ServiceModel> services = DbWaterRegApi.getServices(tag_name, name, _state == 0);


        viewModel.put("_state", _state);
        viewModel.put("tag_name", tag_name);
        viewModel.put("services", services);
        viewModel.put("name", name);

        return view("sev/sev_inner");
    }

    @Mapping("status")
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

    @Mapping("check")
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

    //服务状态
    @Mapping("edit")
    public ModelAndView service_edit(Integer service_id) throws SQLException {
        ServiceModel model = new ServiceModel();
        if (service_id != null) {
            model = DbWaterRegApi.getServiceById(service_id);
        }

        viewModel.put("model", model);
        viewModel.put("service_id", service_id);

        return view("sev/sev_edit");
    }

    @AuthPermissions(SessionPerms.admin)
    @Mapping("edit/ajax/save")
    public ViewModel service_edit_ajax_save(Integer service_id, String tag, String name, String address, String note, Integer check_type, String check_url) throws SQLException {
        boolean result = DbWaterRegApi.udpService(service_id, tag, name, address, note, check_type, check_url);

        if (result) {
            viewModel.code(1, "保存成功！");
        } else {
            viewModel.code(0, "保存失败！");
        }

        return viewModel;
    }

    @AuthPermissions(SessionPerms.admin)
    @Mapping("ajax/batch")
    public ViewModel service_ajax_batch(int act, String ids) throws SQLException {

        DbWaterRegApi.deleteServiceByIds(act, ids);

        return viewModel.code(1, "ok");
    }
}
