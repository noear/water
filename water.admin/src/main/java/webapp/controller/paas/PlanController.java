package webapp.controller.paas;

import org.noear.water.admin.tools.controller.BaseController;
import org.noear.water.admin.tools.viewModels.ViewModel;
import org.noear.water.tools.TextUtils;
import org.noear.weed.DataItem;
import org.noear.weed.DataList;
import org.noear.weed.DbContext;


import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import webapp.Config;
import webapp.dao.BcfTagChecker;
import org.noear.water.admin.tools.dso.Session;
import webapp.dao.db.DbPaaSApi;
import webapp.dao.db.DbWaterApi;
import webapp.models.water.ConfigModel;
import webapp.models.water_paas.PaasPlanModel;
import webapp.utils.TimeUtil;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@XController
@XMapping("/paas/")
public class PlanController extends BaseController {
    //plan视图跳转
    @XMapping("plan")
    public ModelAndView plan(String tag,String plan_name) throws SQLException {
        List<PaasPlanModel> tags = DbPaaSApi.getPlanTags();

        BcfTagChecker.filter(tags, m -> m.tag);

        viewModel.put("tags", tags);
        if (TextUtils.isEmpty(tag) == false) {
            viewModel.put("tag", tag);
        } else {
            if (tags.isEmpty() == false) {
                viewModel.put("tag", tags.get(0).tag);
            } else {
                viewModel.put("tag", null);
            }
        }
        viewModel.put("plan_name",plan_name);
        return view("paas/plan");
    }

    //iframe 的inner视图。
    @XMapping("plan/inner")
    public ModelAndView planInner(String tag, String plan_name, Integer _state) throws SQLException {
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
        List<PaasPlanModel> list = DbPaaSApi.getPlanList(tag, plan_name, _state);
        viewModel.put("plans", list);
        viewModel.put("tag", tag);
        viewModel.put("plan_name",plan_name);
        return view("paas/plan_inner");
    }

    //编辑计划跳转
    @XMapping("plan/edit")
    public ModelAndView editCode(int plan_id) throws SQLException {
        PaasPlanModel plan = DbPaaSApi.getPlanById(plan_id);
        String begin_time = TimeUtil.liveTimeFormat(plan.begin_time).replaceAll(" ", "T");
        String last_exec_time = "";
        if (plan.last_exec_time != null && !plan.last_exec_time.equals("")) {
            last_exec_time = TimeUtil.liveTimeFormat(plan.last_exec_time).replaceAll(" ", "T");
        }
        List<ConfigModel> configs = DbWaterApi.getDbConfigs();
        List<String> option_sources = new ArrayList<>();
        for (ConfigModel config : configs) {
            option_sources.add(config.tag + "." + config.key);
        }
        viewModel.put("tagName", plan.tag);
        viewModel.put("option_sources", option_sources);
        viewModel.put("begin_time", begin_time);
        viewModel.put("last_exec_time", last_exec_time);
        viewModel.put("plan", plan);
        viewModel.put("url_start",Config.paas_uri);

        return view("paas/plan_edit");
    }

    //新增计划跳转
    @XMapping("plan/add")
    public ModelAndView addPlan() throws SQLException {
        String begin_time = TimeUtil.liveTimeFormat(new Date()).replaceAll(" ", "T");
        List<ConfigModel> configs = DbWaterApi.getDbConfigs();
        List<String> option_sources = new ArrayList<>();
        for (ConfigModel config : configs) {
            option_sources.add(config.tag + "." + config.key);
        }
        viewModel.put("option_sources", option_sources);
        viewModel.put("begin_time", begin_time);
        viewModel.put("plan", new PaasPlanModel());
        viewModel.put("url_start",Config.paas_uri);

        return view("paas/plan_edit");
    }

    //ajax编辑保存功能
    @XMapping("plan/edit/ajax/save")
    public ViewModel topicAddSave(Integer plan_id, String code, String tag, String plan_name, String source, String begin_time, String repeat_interval, Integer repeat_max, String last_exec_time, Integer is_enabled) throws SQLException, ParseException {
        if (begin_time.equals("")) {
            begin_time = null;
        }
        if (last_exec_time.equals("")) {
            last_exec_time = null;
        }
        boolean result = DbPaaSApi.editPlan(plan_id, code, tag, plan_name, source, begin_time, repeat_interval,repeat_max, last_exec_time, is_enabled);
        if (result) {
            viewModel.code(1, "保存成功！");
        } else {
            viewModel.code(0, "保存失败!");
        }

        return viewModel;
    }

    @XMapping("plan/ajax/reset")
    public ViewModel plan_reset(Integer plan_id) throws SQLException {
        int is_admin = Session.current().getIsAdmin();
        if (is_admin == 1) {
            boolean result = DbPaaSApi.resetPlanState(plan_id);
            if (result) {
                viewModel.code(1, "重置成功！");
            } else {
                viewModel.code(0, "重置失败！");
            }
        } else {
            viewModel.code(0, "没有权限！");
        }


        return viewModel;
    }
}
