package wateradmin.controller.tool;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.auth.annotation.AuthPermissions;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.water.utils.TextUtils;
import wateradmin.controller.BaseController;
import wateradmin.dso.SessionPerms;
import wateradmin.dso.TagChecker;
import wateradmin.dso.TagUtil;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.dso.db.DbWaterToolApi;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_cfg.ConfigModel;
import wateradmin.models.water_tool.CertificationModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;

@Controller
@Mapping("/tool/certification")
public class CertificationController extends BaseController {

    //certification视图跳转。
    @Mapping("")
    public ModelAndView home(String tag_name, int _state) throws SQLException {
        List<TagCountsModel> tags = DbWaterToolApi.certificationGetTags();

        TagChecker.filter(tags, m -> m.tag);

        tag_name = TagUtil.build(tag_name, tags);

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
        return view("tool/certification");
    }

    //Monitor的 iframe inner视图。
    @Mapping("inner")
    public ModelAndView inner(String tag_name,String url, int _state, String sort) throws SQLException {
        TagUtil.cookieSet(tag_name);

        viewModel.put("_state", _state);

        boolean is_enabled = (_state == 0);

        List<CertificationModel> list = DbWaterToolApi.certificationGetList(tag_name, url, is_enabled);
        viewModel.put("list", list);
        viewModel.put("tag_name", tag_name);
        return view("tool/certification_inner");
    }

    @Mapping("edit")
    public ModelAndView edit(String tag, int certification_id) throws SQLException {
        CertificationModel certification = DbWaterToolApi.certificationGet(certification_id);

        if(certification.certification_id == 0){
            certification.tag = tag;
            certification.url = "";
            certification.is_enabled = 1;
            certification.check_interval = 0;
        }

        viewModel.put("model", certification);

        return view("tool/certification_edit");
    }


    @AuthPermissions(SessionPerms.admin)
    @Mapping("edit/ajax/save")
    public ViewModel save(int certification_id, String tag, String url, String note, int is_enabled) throws Exception {

        boolean result = DbWaterToolApi.certificationSave(certification_id, tag, url, note, is_enabled);

        if (result) {
            viewModel.code(1, "保存成功");
        } else {
            viewModel.code(0, "保存失败");
        }

        return viewModel;
    }

    @AuthPermissions(SessionPerms.admin)
    @Mapping("edit/ajax/del")
    public ViewModel del(int certification_id) throws SQLException {
        boolean result = DbWaterToolApi.certificationDel(certification_id);

        if (result) {
            viewModel.code(1, "删除成功");
        } else {
            viewModel.code(0, "删除失败");
        }

        return viewModel;
    }

    //批量处理
    @AuthPermissions(SessionPerms.admin)
    @Mapping("ajax/batch")
    public ViewModel batchDo(int act, String ids) throws Exception {
        DbWaterToolApi.certificationDelByIds(act, ids);

        return viewModel.code(1, "ok");
    }
}
