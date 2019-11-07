package webapp.controller.smp;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.admin.tools.controller.BaseController;
import org.noear.water.admin.tools.viewModels.ViewModel;
import org.noear.water.tools.TextUtils;
import webapp.dao.BcfTagChecker;
import org.noear.water.admin.tools.dso.Session;
import webapp.dao.db.DbWaterApi;
import webapp.dao.db.DbWaterMotApi;
import webapp.models.water.ConfigModel;
import webapp.models.water.MonitorModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@XController
@XMapping("/smp/")
public class MonitorController extends BaseController {

    //monitor视图跳转。
    @XMapping("monitor")
    public ModelAndView MonitorIndex(String tag) throws SQLException {
        List<MonitorModel> tags = DbWaterMotApi.getMonitorTags();

        BcfTagChecker.filter(tags, m -> m.tag);

        viewModel.put("tags",tags);
        if (TextUtils.isEmpty(tag) == false) {
            viewModel.put("tag",tag);
        } else {
            if (tags.isEmpty() == false) {
                viewModel.put("tag",tags.get(0).tag);
            } else {
                viewModel.put("tag",null);
            }
        }
        return view("smp/monitor");
    }

    //Monitor的 iframe inner视图。
    @XMapping("monitor/inner")
    public ModelAndView monitorInner(String tag,String monitor_name,Integer _state) throws SQLException {
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
        List<MonitorModel> list = DbWaterMotApi.getMonitorList(tag,monitor_name, _state);
        viewModel.put("monitors",list);
        viewModel.put("tag",tag);
        return view("smp/monitor_inner");
    }

    @XMapping("monitor/edit")
    public ModelAndView editMonitor(Integer monitor_id) throws SQLException {
        MonitorModel monitor = DbWaterMotApi.getMonitorById(monitor_id);

        List<ConfigModel> configs= DbWaterApi.getDbConfigs();
        List<String> option_sources = new ArrayList<>();
        for(ConfigModel config : configs){
            option_sources.add(config.tag+"."+config.key);
        }
        viewModel.put("option_sources",option_sources);

        viewModel.put("monitor",monitor);
        return view("smp/monitor_edit");
    }

    @XMapping("monitor/add")
    public ModelAndView addMonitor() throws SQLException {
        List<ConfigModel> configs= DbWaterApi.getDbConfigs();
        List<String> option_sources = new ArrayList<>();
        for(ConfigModel config : configs){
            option_sources.add(config.tag+"."+config.key);
        }
        viewModel.put("option_sources",option_sources);
        viewModel.put("monitor",new MonitorModel());
        return view("smp/monitor_edit");
    }

    @XMapping("monitor/edit/ajax/save")
    public ViewModel saveEdit(Integer monitor_id, String tag, String name, Integer type, String source, String source_model, String rule, String task_tag_exp,
                              String alarm_mobile, String alarm_sign, String alarm_exp, Integer is_enabled) throws SQLException {
        if (alarm_mobile.endsWith(",")) {
            alarm_mobile = alarm_mobile.substring(0, alarm_mobile.length() - 1);
        }

        int is_admin = Session.current().getIsAdmin();
        if (is_admin == 1) {
            boolean result = DbWaterMotApi.setMonitor(monitor_id, tag, name, type, source, source_model, rule, task_tag_exp, alarm_mobile, alarm_sign, alarm_exp, is_enabled);
            if (result) {
                viewModel.code(1, "保存成功!");
            } else {
                viewModel.code(0, "保存失败!");
            }
        } else {
            viewModel.code(0, "没有权限!");
        }

        return viewModel;
    }
}
