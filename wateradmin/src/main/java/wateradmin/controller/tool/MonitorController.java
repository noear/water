package wateradmin.controller.tool;

import org.noear.solon.auth.annotation.AuthPermissions;
import org.noear.water.utils.TextUtils;


import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.TagChecker;
import wateradmin.dso.Session;
import wateradmin.dso.SessionPerms;
import wateradmin.dso.db.DbWaterApi;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water.MonitorModel;
import wateradmin.models.water_cfg.ConfigModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Mapping("/tool/")
public class MonitorController extends BaseController {

    //monitor视图跳转。
    @Mapping("monitor")
    public ModelAndView MonitorIndex(String tag_name) throws SQLException {
        List<TagCountsModel> tags = DbWaterApi.monitorGetTags();

        TagChecker.filter(tags, m -> m.tag);

        viewModel.put("tags",tags);
        if (TextUtils.isEmpty(tag_name) == false) {
            viewModel.put("tag_name",tag_name);
        } else {
            if (tags.isEmpty() == false) {
                viewModel.put("tag_name",tags.get(0).tag);
            } else {
                viewModel.put("tag_name",null);
            }
        }
        return view("tool/monitor");
    }

    //Monitor的 iframe inner视图。
    @Mapping("monitor/inner")
    public ModelAndView monitorInner(String tag_name,String monitor_name,Integer _state) throws SQLException {
        if (_state!=null) {
            viewModel.put("_state", _state);
            int state = _state;
            if (state == 0) {
                _state = 1;
            } else if (state == 1) {
                _state = 0;
            }
        }
        if(_state==null)
            _state = 1;
        List<MonitorModel> list = DbWaterApi.monitorGetList(tag_name,monitor_name, _state);
        viewModel.put("monitors",list);
        viewModel.put("tag_name",tag_name);
        return view("tool/monitor_inner");
    }

    @Mapping("monitor/edit")
    public ModelAndView editMonitor(String tag,Integer monitor_id) throws SQLException {
        if (monitor_id == null) {
            monitor_id = 0;
        }

        List<ConfigModel> cfgs = DbWaterCfgApi.getDbConfigs();

        MonitorModel monitor = DbWaterApi.monitorGet(monitor_id);

        List<String> option_sources = new ArrayList<>();
        for (ConfigModel config : cfgs) {
            option_sources.add(config.tag + "/" + config.key);
        }

        if (cfgs == null) {
            cfgs = new ArrayList<>();
        }

        viewModel.put("cfgs", cfgs);
        viewModel.put("option_sources", option_sources);

        viewModel.put("monitor", monitor);
        if (Session.current().isAdmin()) {
            return view("tool/monitor_edit");
        } else {
            return view("tool/monitor_view");
        }
    }


    @AuthPermissions(SessionPerms.admin)
    @Mapping("monitor/edit/ajax/save")
    public ViewModel save(Integer monitor_id, String tag, String name, Integer type, String source_query, String rule, String task_tag_exp,
                              String alarm_mobile, String alarm_sign, String alarm_exp, Integer is_enabled) throws SQLException {
        if (alarm_mobile.endsWith(",")) {
            alarm_mobile = alarm_mobile.substring(0, alarm_mobile.length() - 1);
        }

        boolean result = DbWaterApi.monitorSave(monitor_id, tag, name, type, source_query, rule, task_tag_exp, alarm_mobile, alarm_sign, alarm_exp, is_enabled);
        if (result) {
            viewModel.code(1, "保存成功!");
        } else {
            viewModel.code(0, "保存失败!");
        }

        return viewModel;
    }

    @Mapping("monitor/edit/ajax/del")
    public ViewModel del(Integer monitor_id) throws SQLException {
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限");
        }

        boolean result = DbWaterApi.monitorDel(monitor_id);

        if (result) {
            viewModel.code(1, "删除成功!");
        } else {
            viewModel.code(0, "删除失败!");
        }

        return viewModel;
    }
}
