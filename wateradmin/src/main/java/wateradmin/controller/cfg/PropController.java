package wateradmin.controller.cfg;

import org.noear.solon.auth.annotation.AuthPermissions;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.UploadedFile;
import org.noear.water.utils.*;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import wateradmin.controller.BaseController;
import wateradmin.dso.SettingUtils;
import wateradmin.dso.TagChecker;
import wateradmin.dso.SessionPerms;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.models.ScaleType;
import wateradmin.models.TagCountsModel;
import wateradmin.dso.TagUtil;
import wateradmin.models.water_cfg.ConfigModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Mapping("/cfg/prop")
public class PropController extends BaseController {
    @Mapping("")
    public ModelAndView home(String tag_name, String label,  int _state) throws SQLException {
        List<TagCountsModel> tags = DbWaterCfgApi.getConfigTags();

        TagChecker.filter(tags, m -> m.tag);

        tag_name = TagUtil.build(tag_name, tags);

        if(label == null){
            label = "";
        }

        viewModel.put("tag_name", tag_name);
        viewModel.put("label", label);
        viewModel.put("tags", tags);
        viewModel.put("_state", _state);
        return view("cfg/prop");
    }

    @Mapping("inner")
    public ModelAndView inner(String tag_name, String label, String key, int _state) throws SQLException {
        TagUtil.cookieSet(tag_name);

        List<TagCountsModel> labelList;
        if (SettingUtils.propsScale().ordinal() < ScaleType.medium.ordinal()) {
            label = null;
            labelList = new ArrayList<>();
        } else {
            if (label == null) {
                label = "";
            }
            labelList = DbWaterCfgApi.getConfigLabelsByTag(tag_name);
        }

        List<ConfigModel> list = DbWaterCfgApi.getConfigsByTag(tag_name, label, key, _state == 0);

        viewModel.put("list", list);
        viewModel.put("labelList", labelList);
        viewModel.put("label", label);
        viewModel.put("tag_name", tag_name);
        viewModel.put("key", key);
        viewModel.put("_state", _state);

        return view("cfg/prop_inner");
    }


    //跳转编辑页面。
    @Mapping("edit")
    public ModelAndView edit(String tag_name, Integer row_id) throws SQLException {
        if (row_id == null) {
            row_id = 0;
        }

        ConfigModel tml = DbWaterCfgApi.getConfigByTagName("_system", "config_tml");
        ConfigModel cfg = DbWaterCfgApi.getConfig(row_id);

        if (cfg.row_id > 0) {
            tag_name = cfg.tag;
        } else {
            cfg.is_enabled = 1;
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
    @AuthPermissions(SessionPerms.admin)
    @Mapping("edit/ajax/save")
    public ViewModel save(Integer row_id, String tag, String key, Integer type, String label, String value, String edit_mode, int is_disabled) throws SQLException {
        DbWaterCfgApi.setConfig(row_id, tag.trim(), key.trim(), type, label, value, edit_mode, is_disabled == 0);

        return viewModel.code(1, "操作成功");
    }

    //编辑、保存功能。
    @AuthPermissions(SessionPerms.admin)
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
    @AuthPermissions(SessionPerms.admin)
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
    @AuthPermissions(SessionPerms.admin)
    @Mapping("ajax/batch")
    public ViewModel batchDo(int act, String ids) throws Exception {
        DbWaterCfgApi.delConfigByIds(act, ids);

        return viewModel.code(1, "ok");
    }
}