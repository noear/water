package xwater.controller.cfg;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.UploadedFile;
import org.noear.water.utils.*;
import xwater.controller.BaseController;
import xwater.dso.TagChecker;
import xwater.dso.TagUtil;
import xwater.dso.db.DbWaterCfgApi;
import xwater.models.TagCountsModel;
import xwater.models.view.water_cfg.ConfigModel;
import xwater.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;

@Controller
@Mapping("/cfg/prop")
public class PropController extends BaseController {
    @Mapping("")
    public ModelAndView index(String tag_name) throws SQLException {
        List<TagCountsModel> tags = DbWaterCfgApi.getConfigTags();

        //TagChecker.filterWaterTag(tags, m -> m.tag);


        tag_name = TagUtil.build(tag_name, tags);

        viewModel.put("tag_name", tag_name);
        viewModel.put("tags", tags);
        return view("cfg/prop");
    }

    @Mapping("inner")
    public ModelAndView innerDo(Context ctx, String tag_name, String key) throws SQLException {
        TagUtil.cookieSet(tag_name);

        List<ConfigModel> list = DbWaterCfgApi.getConfigsByTag(tag_name, key);

        viewModel.put("list", list);
        viewModel.put("tag_name", tag_name);
        viewModel.put("key", key);

        return view("cfg/prop_inner");
    }


    //跳转编辑页面。
    @Mapping("edit")
    public ModelAndView editConfig(String tag_name, Integer row_id) throws SQLException {
        if (row_id == null) {
            row_id = 0;
        }

        ConfigModel tml = DbWaterCfgApi.getConfigByTagName("_system", "config_tml");
        ConfigModel cfg = DbWaterCfgApi.getConfig(row_id);

        if (cfg.row_id > 0) {
            tag_name = cfg.tag;
        }

        if (TextUtils.isEmpty(tml.value)) {
            tml.value = "{}";
        }

        viewModel.put("config_tml", tml.getNode().toJson());
        viewModel.put("row_id", row_id);
        viewModel.put("cfg", cfg);
        viewModel.put("tag_name", tag_name);
        return view("cfg/prop_edit");
    }

    //编辑、保存功能。
    @Mapping("edit/ajax/save")
    public ViewModel save(Integer row_id, String tag, String key, Integer type, String value, String edit_mode, int is_disabled) throws SQLException {
        boolean result = DbWaterCfgApi.setConfig(row_id, tag.trim(), key.trim(), type, value, edit_mode, is_disabled==0);

        if (result) {
            viewModel.code(1, "保存成功");
        } else {
            viewModel.code(0, "保存失败");
        }

        return viewModel;
    }

    //编辑、保存功能。
    @Mapping("edit/ajax/del")
    public ViewModel del(Integer row_id) throws SQLException {
        DbWaterCfgApi.delConfig(row_id);

        return viewModel.code(1, "操作成功");
    }


    //批量导出
    @Mapping("ajax/export")
    public void exportDo(Context ctx, String tag, String ids) throws Exception {
        List<ConfigModel> list = DbWaterCfgApi.getConfigByIds(ids);

        String jsonD = JsondUtils.encode("water_cfg_properties", list);

        String filename2 = "water_config_" + tag + "_" + Datetime.Now().getDate() + ".jsond";

        ctx.headerSet("Content-Disposition", "attachment; filename=\"" + filename2 + "\"");
        ctx.output(jsonD);
    }


    //批量导入
    @Mapping("ajax/import")
    public ViewModel importDo(String tag, UploadedFile file) throws Exception {
        String jsonD = IOUtils.toString(file.getContent());
        JsondEntity entity = JsondUtils.decode(jsonD);

        if (entity == null || "water_cfg_properties".equals(entity.table) == false) {
            return viewModel.code(0, "数据不对！");
        }

        List<ConfigModel> list = entity.data.toObjectList(ConfigModel.class);

        for (ConfigModel m : list) {
            DbWaterCfgApi.impConfigOrRep(tag, m);
        }

        return viewModel.code(1, "ok");
    }

    //批量处理
    @Mapping("ajax/batch")
    public ViewModel batchDo(Context ctx, String tag, Integer act, String ids) throws Exception {

        if (act == null) {
            act = 0;
        }

        DbWaterCfgApi.delConfigByIds(act, ids);

        return viewModel.code(1, "ok");
    }
}
