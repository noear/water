package wateradmin.controller.tool;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.auth.annotation.AuthPermissions;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.water.utils.TextUtils;
import wateradmin.controller.BaseController;
import wateradmin.dso.SessionPerms;
import wateradmin.dso.TagChecker;
import wateradmin.dso.db.DbWaterToolApi;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water.DetectionModel;
import wateradmin.models.water_cfg.ConfigModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Mapping("/tool/")
public class DetectionController extends BaseController {

    //detection视图跳转。
    @Mapping("detection")
    public ModelAndView MonitorIndex(String tag_name, int _state) throws SQLException {
        List<TagCountsModel> tags = DbWaterToolApi.detectionGetTags();

        TagChecker.filter(tags, m -> m.tag);

        viewModel.put("tags",tags);
        viewModel.put("_state", _state);

        if (TextUtils.isEmpty(tag_name) == false) {
            viewModel.put("tag_name",tag_name);
        } else {
            if (tags.isEmpty() == false) {
                viewModel.put("tag_name",tags.get(0).tag);
            } else {
                viewModel.put("tag_name",null);
            }
        }
        return view("tool/detection");
    }

    //Monitor的 iframe inner视图。
    @Mapping("detection/inner")
    public ModelAndView detectionInner(String tag_name,String detection_name, int _state) throws SQLException {
        viewModel.put("_state", _state);

        boolean is_enabled = (_state == 0);

        List<DetectionModel> list = DbWaterToolApi.detectionGetList(tag_name, detection_name, is_enabled);
        viewModel.put("list", list);
        viewModel.put("tag_name", tag_name);
        return view("tool/detection_inner");
    }

    @Mapping("detection/edit")
    public ModelAndView editMonitor(String tag, int detection_id) throws SQLException {
        List<ConfigModel> cfgs = DbWaterCfgApi.getDbConfigs();

        DetectionModel detection = DbWaterToolApi.detectionGet(detection_id);

        List<String> option_sources = new ArrayList<>();
        for (ConfigModel config : cfgs) {
            option_sources.add(config.tag + "/" + config.key);
        }

        if (cfgs == null) {
            cfgs = new ArrayList<>();
        }

        viewModel.put("cfgs", cfgs);
        viewModel.put("option_sources", option_sources);
        viewModel.put("model", detection);

        return view("tool/detection_edit");
    }


    @AuthPermissions(SessionPerms.admin)
    @Mapping("detection/edit/ajax/save")
    public ViewModel save(int detection_id, String tag, String name, String source_query, String rule, String task_tag_exp,
                              String alarm_mobile, String alarm_sign, String alarm_exp, int is_enabled) throws SQLException {
        if (alarm_mobile.endsWith(",")) {
            alarm_mobile = alarm_mobile.substring(0, alarm_mobile.length() - 1);
        }

        boolean result = DbWaterToolApi.detectionSave(detection_id, tag, name, source_query, rule, task_tag_exp, alarm_mobile, alarm_sign, alarm_exp, is_enabled);
        if (result) {
            viewModel.code(1, "保存成功");
        } else {
            viewModel.code(0, "保存失败");
        }

        return viewModel;
    }

    @AuthPermissions(SessionPerms.admin)
    @Mapping("detection/edit/ajax/del")
    public ViewModel del(int detection_id) throws SQLException {
        boolean result = DbWaterToolApi.detectionDel(detection_id);

        if (result) {
            viewModel.code(1, "删除成功");
        } else {
            viewModel.code(0, "删除失败");
        }

        return viewModel;
    }
}
