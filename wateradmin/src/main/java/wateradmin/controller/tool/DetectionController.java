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
import java.util.List;

@Controller
@Mapping("/tool/")
public class DetectionController extends BaseController {

    //detection视图跳转。
    @Mapping("detection")
    public ModelAndView home(String tag_name, int _state) throws SQLException {
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
    public ModelAndView inner(String tag_name,String detection_name, int _state) throws SQLException {
        viewModel.put("_state", _state);

        boolean is_enabled = (_state == 0);

        List<DetectionModel> list = DbWaterToolApi.detectionGetList(tag_name, detection_name, is_enabled);
        viewModel.put("list", list);
        viewModel.put("tag_name", tag_name);
        return view("tool/detection_inner");
    }

    @Mapping("detection/edit")
    public ModelAndView edit(String tag, int detection_id) throws SQLException {
        List<ConfigModel> cfgs = DbWaterCfgApi.getDbConfigs();

        DetectionModel detection = DbWaterToolApi.detectionGet(detection_id);

        if(detection.detection_id == 0){
            detection.tag = tag;
            detection.protocol = "http";
            detection.is_enabled = 1;
            detection.check_interval = 10;
        }

        if(detection.check_interval == 0){
            detection.check_interval = 10;
        }

        viewModel.put("model", detection);

        return view("tool/detection_edit");
    }


    @AuthPermissions(SessionPerms.admin)
    @Mapping("detection/edit/ajax/save")
    public ViewModel save(int detection_id, String tag, String name, String protocol, String address, int check_interval, int is_enabled) throws SQLException {
        if (check_interval == 0) {
            check_interval = 10;
        }

        if (check_interval < 5) {
            check_interval = 5;
        }

        boolean result = DbWaterToolApi.detectionSave(detection_id, tag, name, protocol, address, check_interval, is_enabled);

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