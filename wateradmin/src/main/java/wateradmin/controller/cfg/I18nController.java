package wateradmin.controller.cfg;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.auth.annotation.AuthPermissions;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.UploadedFile;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.IOUtils;
import org.noear.water.utils.JsondEntity;
import org.noear.water.utils.JsondUtils;
import wateradmin.controller.BaseController;
import wateradmin.dso.Session;
import wateradmin.dso.SessionPerms;
import wateradmin.dso.TagChecker;
import wateradmin.dso.TagUtil;
import wateradmin.dso.db.DbWaterCfgI18nApi;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_cfg.I18nModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;


@Controller
@Mapping("/cfg/i18n")
public class I18nController extends BaseController {

    @Mapping("")
    public ModelAndView home(String tag_name) throws Exception {
        List<TagCountsModel> tags = DbWaterCfgI18nApi.getI18nTags();


        TagChecker.filter(tags, m -> m.tag);


        tag_name = TagUtil.build(tag_name, tags);

        viewModel.put("tag_name", tag_name);
        viewModel.put("tags", tags);
        return view("cfg/i18n");
    }

    @Mapping("inner")
    public ModelAndView innerDo(Context ctx, String tag_name, String key) throws Exception {
        int state = ctx.paramAsInt("state", 1);

        List<I18nModel> list = DbWaterCfgI18nApi.getI18nListByTag(tag_name, key, state);


        TagChecker.filter(list, m -> m.tag);


        viewModel.put("list", list);
        viewModel.put("tag_name", tag_name);
        viewModel.put("state", state);
        viewModel.put("key", key);

        return view("cfg/i18n_inner");
    }

    @Mapping("edit")
    public ModelAndView edit(Integer id, String tag_name) throws SQLException {
        I18nModel model = null;
        if (id != null) {
            model = DbWaterCfgI18nApi.getI18n(id);
            viewModel.put("m", model);
        } else {
            model = new I18nModel();
            viewModel.put("m", model);
        }

        if (model.tag != null) {
            tag_name = model.tag;
        }

        viewModel.put("tag_name", tag_name);
        return view("cfg/i18n_edit");
    }

    @AuthPermissions(SessionPerms.admin)
    @Mapping("edit/ajax/save")
    public ViewModel saveDo(Integer row_id, String tag, String bundle, String lang ,String name, String value) throws Exception {
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限");
        }

        boolean result = DbWaterCfgI18nApi.setI18n(row_id, tag, bundle, lang, name, value);
        if (result) {
            viewModel.code(1, "操作成功");
        } else {
            viewModel.code(0, "操作失败");
        }

        return viewModel;
    }


    @AuthPermissions(SessionPerms.admin)
    @Mapping("ajax/del")
    public ViewModel delDo(Integer row_id) throws Exception {
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限");
        }

        boolean result = DbWaterCfgI18nApi.delI18n(row_id);
        if (result) {
            viewModel.code(1, "删除成功");
        } else {
            viewModel.code(0, "删除失败");
        }

        return viewModel;
    }

    @Mapping("ajax/export")
    public void exportDo(Context ctx, String tag, String ids) throws Exception {
        List<I18nModel> list = DbWaterCfgI18nApi.getI18nByIds(ids);

        String jsonD = JsondUtils.encode("water_cfg_i18n", list);

        String filename2 = "water_cfg_i18n_" + tag + "_" + Datetime.Now().getDate() + ".jsond";

        ctx.headerSet("Content-Disposition", "attachment; filename=\"" + filename2 + "\"");

        ctx.output(jsonD);
    }

    @Mapping("ajax/import")
    public ViewModel importDo(String tag, UploadedFile file) throws Exception {
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限！");
        }

        String jsonD = IOUtils.toString(file.content);
        JsondEntity entity = JsondUtils.decode(jsonD);

        if (entity == null || "water_cfg_i18n".equals(entity.table) == false) {
            return viewModel.code(0, "数据不对！");
        }

        List<I18nModel> list = entity.data.toObjectList(I18nModel.class);

        for (I18nModel m : list) {
            DbWaterCfgI18nApi.impI18n(tag, m);
        }

        return viewModel.code(1, "ok");
    }

    @Mapping("ajax/batch")
    public ViewModel batchDo(Context ctx, String tag, Integer act, String ids) throws Exception {
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限！");
        }

        if (act == null) {
            act = 0;
        }

        DbWaterCfgI18nApi.delI18nByIds(act, ids);

        return viewModel.code(1, "ok");
    }
}
