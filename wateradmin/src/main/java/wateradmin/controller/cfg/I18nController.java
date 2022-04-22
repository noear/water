package wateradmin.controller.cfg;

import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.auth.annotation.AuthPermissions;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.UploadedFile;
import org.noear.water.dso.NoticeUtils;
import org.noear.water.utils.*;
import wateradmin.controller.BaseController;
import wateradmin.dso.*;
import wateradmin.dso.db.DbWaterCfgI18nApi;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_cfg.EnumModel;
import wateradmin.models.water_cfg.I18nModel;
import wateradmin.utils.JsonFormatTool;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


@Controller
@Mapping("/cfg/i18n")
public class I18nController extends BaseController {
    static final String _i18n_lang = "_i18n.lang";
    static final String _i18n_bundle = "_i18n.bundle";

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
    public ModelAndView innerDo(Context ctx, String tag_name, String bundle, String name, String lang) throws Exception {
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

        if (Utils.isEmpty(lang)) {
            lang = ctx.cookie("lang");
        }

        if (Utils.isNotEmpty(lang) && "default".equals(lang) == false) {
            String lang1 = lang;
            long langCount = langs.stream().filter(m -> m.tag.equals(lang1)).count();
            if (langCount == 0L) {
                lang = null;
            }
        }

        if (Utils.isEmpty(lang)) {
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
    public ModelAndView edit(Integer id, String tag_name, String bundle) throws SQLException {
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

        if (model.bundle != null) {
            bundle = model.bundle;
        }

        List<I18nModel> langs = DbWaterCfgI18nApi.getI18nByName(tag_name, model.bundle, model.name);
        if (langs.size() == 0) {
            langs.add(new I18nModel());
        }

        List<EnumModel> lang_type = EnumUtil.get("lang_type");

        viewModel.put("lang_type", lang_type);
        viewModel.put("langs", ONode.stringify(langs));
        viewModel.put("tag_name", tag_name);
        viewModel.put("bundle", bundle);
        return view("cfg/i18n_edit");
    }

    @AuthPermissions(SessionPerms.admin)
    @Mapping("edit/ajax/save")
    public ViewModel saveDo(String tag, String bundle, String name, String nameOld, String items) throws Exception {
        try {
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
        } catch (Throwable e) {
            return viewModel.code(0, e.getLocalizedMessage());
        }
    }


    @Mapping("ajax/batch")
    public ViewModel batchDo(String tag, Integer act, String ids) throws Exception {
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限！");
        }

        if (act == null) {
            act = 0;
        }

        DbWaterCfgI18nApi.delI18nByIds(act, ids);

        return viewModel.code(1, "ok");
    }


    @AuthPermissions(SessionPerms.admin)
    @Mapping("ajax/del")
    public ViewModel delDo(String tag, String bundle, String name, String nameOld) throws Exception {
        boolean result = DbWaterCfgI18nApi.delI18n(tag, bundle, nameOld);
        if (result) {
            viewModel.code(1, "删除成功");
        } else {
            viewModel.code(0, "删除失败");
        }

        return viewModel;
    }

    @Mapping("ajax/export")
    public void exportDo(Context ctx, String tag, String bundle, String fmt, String ids) throws Exception {
        List<I18nModel> list = DbWaterCfgI18nApi.getI18nByIds(ids);
        String filename = "water_cfg_i18n_" + tag + "_" + bundle + "_" + Datetime.Now().getDate();

        if(list.size() == 0){
            return;
        }


        if ("jsond".equals(fmt)) {
            String data = JsondUtils.encode("water_cfg_i18n", list);
            String filename2 = filename + ".jsond";

            ctx.headerSet("Content-Disposition", "attachment; filename=\"" + filename2 + "\"");
            ctx.output(data);
            return;
        }

        Map<String, String> i18nMap = new LinkedHashMap<>();
        for (I18nModel m1 : list) {
            i18nMap.put(m1.name, m1.value);
        }

        if(i18nMap.containsKey(_i18n_bundle) == false) {
            i18nMap.put(_i18n_bundle, bundle);
        }

        if(i18nMap.containsKey(_i18n_lang) == false){
            i18nMap.put(_i18n_lang, list.get(0).lang);
        }

        if ("json".equals(fmt)) {
            String data = JsonFormatTool.formatJson(ONode.stringify(i18nMap));//格式化一下好看些
            String filename2 = filename + ".json";

            ctx.headerSet("Content-Disposition", "attachment; filename=\"" + filename2 + "\"");
            ctx.output(data);
            return;
        }

        if ("properties".equals(fmt)) {
            StringBuilder data = new StringBuilder();
            i18nMap.forEach((name, value)->{
                data.append(name).append("=").append(value.replace("\n", "\\n")).append("\n");
            });

            String filename2 = filename + ".properties";

            ctx.headerSet("Content-Disposition", "attachment; filename=\"" + filename2 + "\"");
            ctx.output(data.toString());
            return;
        }

        if ("yml".equals(fmt)) {
            StringBuilder data = new StringBuilder();
            i18nMap.forEach((name, value)-> {
                data.append(name);
                if (value.contains("'")) { // 如果有单引号，则用双引号
                    data.append(": \"").append(value.replace("\n", "\\n")).append("\"\n");
                } else {
                    data.append(": '").append(value.replace("\n", "\\n")).append("'\n");
                }
            });

            String filename2 = filename + ".yml";

            ctx.headerSet("Content-Disposition", "attachment; filename=\"" + filename2 + "\"");
            ctx.output(data.toString());
            return;
        }
    }

    @AuthPermissions(SessionPerms.admin)
    @Mapping("ajax/import")
    public ViewModel importDo(String tag, String bundle, UploadedFile file) throws Exception {
        try {
            if ("jsond".equals(file.extension)) {
                return importFileForJsond(tag, bundle, file);
            } else {
                return importFileForProfile(tag, bundle, file);
            }
        } catch (Throwable e) {
            return viewModel.code(0, e.getLocalizedMessage());
        }
    }


    private ViewModel importFileForJsond(String tag, String bundle, UploadedFile file) throws Exception {
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限！");
        }

        String jsonD = IOUtils.toString(file.content);
        JsondEntity entity = JsondUtils.decode(jsonD);

        if (entity == null || "water_cfg_i18n".equals(entity.table) == false) {
            return viewModel.code(0, "数据不对！");
        }

        List<I18nModel> list = entity.data.toObjectList(I18nModel.class);

        if (list.size() == 0) {
            return viewModel.code(0, "数据为空！");
        }

        //确定 bundle, lang
        String lang = list.get(0).lang;
        if (lang == null) {
            lang = "";
        }
        if (Utils.isEmpty(bundle)) {
            bundle = list.get(0).bundle;
        }

        for (I18nModel m : list) {
            DbWaterCfgI18nApi.impI18n(tag, bundle, m.name, m.lang, m.value);
        }

        //通知更新
        NoticeUtils.updateI18nCache(tag, bundle, lang);

        return viewModel.code(1, "ok");
    }

    private ViewModel importFileForProfile(String tag, String bundle, UploadedFile file) throws Exception {
        String i18nStr = Utils.transferToString(file.content, "UTF-8");
        Properties i18n = Utils.buildProperties(i18nStr);

        //初始化 _i18n.lang (_开头可以排序在前)
        String lang = i18n.getProperty(_i18n_lang);

        //初始化 _i18n.bundle
        if (Utils.isEmpty(bundle)) {
            bundle = i18n.getProperty(_i18n_bundle);
        }

        if (Utils.isEmpty(bundle)) {
            return viewModel.code(0, "提示：缺少元信息配置");
        }

        i18n.remove(_i18n_bundle);
        i18n.remove(_i18n_lang);


        if (lang == null) {
            lang = "";
        }

        for (Object k : i18n.keySet()) {
            if (k instanceof String) {
                String name = (String) k;
                DbWaterCfgI18nApi.impI18n(tag, bundle, name, lang, i18n.getProperty(name));
            }
        }

        //通知更新
        NoticeUtils.updateI18nCache(tag, bundle, lang);

        return viewModel.code(1, "导入成功");
    }
}
