package wateradmin.controller.cfg;

import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.auth.annotation.AuthPermissions;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.UploadedFile;
import org.noear.water.utils.*;
import wateradmin.controller.BaseController;
import wateradmin.dso.*;
import wateradmin.dso.db.DbWaterCfgI18nApi;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_cfg.EnumModel;
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
    public ModelAndView innerDo(Context ctx, String tag_name,String bundle, String name, String lang) throws Exception {
        List<TagCountsModel> bundles = DbWaterCfgI18nApi.getI18nBundles(tag_name);
        if (TextUtils.isEmpty(bundle)) {
            if (bundles.size() > 0) {
                bundle = bundles.get(0).tag;
            }
        }


        List<TagCountsModel> langs = DbWaterCfgI18nApi.getI18nLangsByBundle(tag_name, bundle);
        for (TagCountsModel m : langs) {
            if (TextUtils.isEmpty(m.tag)) {
                m.tag = "default";
            }
        }

        if (Utils.isEmpty(lang) || "default".equals(lang)) {
            lang = ctx.cookie("lang");
        } else {
            String lang1 = lang;
            TagCountsModel lang2 = langs.stream().filter(m -> m.tag.equals(lang1)).findFirst().get();
            if (lang2 == null) {
                lang = null;
            }
        }

        if (Utils.isEmpty(lang) || "default".equals(lang)) {
            if (langs.size() > 0) {
                lang = langs.get(0).tag;
            }
        }

        List<I18nModel> list = DbWaterCfgI18nApi.getI18nListByTag(tag_name, bundle, name, lang);

        if (TextUtils.isEmpty(lang)) {
            lang = "default";
        }

        ctx.cookieSet("lang", lang);


        TagChecker.filter(list, m -> m.tag);


        viewModel.put("lang", lang);
        viewModel.put("langs", langs);
        viewModel.put("bundles", bundles);
        viewModel.put("list", list);
        viewModel.put("tag_name", tag_name);
        viewModel.put("bundle", bundle);
        viewModel.put("name", name);
        viewModel.put("tag_name", tag_name);

        return view("cfg/i18n_inner");
    }

    @Mapping("edit")
    public ModelAndView edit(Integer id, String tag_name) throws SQLException {
        I18nModel model = null;
        if (id != null) {
            model = DbWaterCfgI18nApi.getI18n(id);
            viewModel.put("model", model);
        } else {
            model = new I18nModel();
            viewModel.put("model", model);
        }

        if (model.tag != null) {
            tag_name = model.tag;
        }

        List<I18nModel> langs = DbWaterCfgI18nApi.getI18nByName(tag_name, model.bundle, model.name);
        if (langs.size() == 0) {
            langs.add(new I18nModel());
        }

        List<EnumModel> lang_type = EnumUtil.get("lang_type");

        viewModel.put("lang_type", lang_type);
        viewModel.put("langs", ONode.stringify(langs));
        viewModel.put("tag_name", tag_name);
        return view("cfg/i18n_edit");
    }

    @AuthPermissions(SessionPerms.admin)
    @Mapping("edit/ajax/save")
    public ViewModel saveDo( String tag, String bundle, String name, String nameOld, String items) throws Exception {
        List<I18nModel> itemList = ONode.loadStr(items).toObjectList(I18nModel.class);

        boolean result = true;

        for (I18nModel m : itemList) {
            if (TextUtils.isEmpty(m.lang) && TextUtils.isEmpty(m.value)) {
                continue;
            }

            result = result && DbWaterCfgI18nApi.setI18n(tag, bundle, name, nameOld, m.lang, m.value);
        }

        if (result) {
            return viewModel.code(1, "操作成功");
        } else {
            return viewModel.code(0, "操作失败");
        }
    }


    @AuthPermissions(SessionPerms.admin)
    @Mapping("ajax/del")
    public ViewModel delDo(String tag ,String bundle, String name,  String nameOld) throws Exception {
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限");
        }

        boolean result = DbWaterCfgI18nApi.delI18n(tag, bundle, nameOld);
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
