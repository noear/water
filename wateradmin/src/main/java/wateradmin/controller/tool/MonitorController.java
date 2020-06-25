package wateradmin.controller.tool;

import org.noear.water.utils.TextUtils;


import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.BcfTagChecker;
import wateradmin.dso.Session;
import wateradmin.dso.db.DbWaterApi;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water.MonitorModel;
import wateradmin.models.water_cfg.ConfigModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@XController
@XMapping("/tool/")
public class MonitorController extends BaseController {

    //monitor视图跳转。
    @XMapping("monitor")
    public ModelAndView MonitorIndex(String tag_name) throws SQLException {
        List<TagCountsModel> tags = DbWaterApi.monitorGetTags();

        BcfTagChecker.filter(tags, m -> m.tag);

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
    @XMapping("monitor/inner")
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

    @XMapping("monitor/edit")
    public ModelAndView editMonitor(String tag,Integer monitor_id) throws SQLException {
        if(monitor_id == null){
            monitor_id = 0;
        }

        List<ConfigModel> cfgs = DbWaterCfgApi.getDbConfigs();

        MonitorModel monitor = DbWaterApi.monitorGet(monitor_id);

        List<String> option_sources = new ArrayList<>();
        for(ConfigModel config : cfgs){
            option_sources.add(config.tag+"/"+config.key);
        }

        if(cfgs == null){
            cfgs = new ArrayList<>();
        }

        viewModel.put("cfgs",cfgs);
        viewModel.put("option_sources",option_sources);

        viewModel.put("monitor",monitor);
        return view("tool/monitor_edit");
    }


    @XMapping("monitor/edit/ajax/save")
    public ViewModel save(Integer monitor_id, String tag, String name, Integer type, String source_query, String rule, String task_tag_exp,
                              String alarm_mobile, String alarm_sign, String alarm_exp, Integer is_enabled) throws SQLException {
        if (alarm_mobile.endsWith(",")) {
            alarm_mobile = alarm_mobile.substring(0, alarm_mobile.length() - 1);
        }

        int is_admin = Session.current().getIsAdmin();
        if (is_admin == 1) {
            boolean result = DbWaterApi.monitorSave(monitor_id, tag, name, type, source_query, rule, task_tag_exp, alarm_mobile, alarm_sign, alarm_exp, is_enabled);
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

    @XMapping("monitor/edit/ajax/del")
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
