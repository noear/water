package wateradmin.controller.cfg;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.auth.annotation.AuthPermissions;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.UploadedFile;
import org.noear.water.utils.*;
import wateradmin.controller.BaseController;
import wateradmin.dso.SessionPerms;
import wateradmin.dso.TagChecker;
import wateradmin.dso.TagUtil;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.dso.db.DbWaterCfgKeyApi;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_cfg.ConfigModel;
import wateradmin.models.water_cfg.KeyModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Mapping("/cfg/key")
public class KeyController extends BaseController {

    @Mapping("")
    public ModelAndView home(String tag_name, int _state) throws Exception {
        List<TagCountsModel> tags = DbWaterCfgKeyApi.getKeyTags();

        TagChecker.filter(tags, m -> m.tag);

        tag_name = TagUtil.build(tag_name, tags);

        viewModel.put("_state", _state);
        viewModel.put("tag_name", tag_name);
        viewModel.put("tags", tags);
        return view("cfg/key");
    }

    @Mapping("inner")
    public ModelAndView inner(String tag_name, String key, int _state) throws Exception {
        TagUtil.cookieSet(tag_name);

        List<KeyModel> list = DbWaterCfgKeyApi.getKeyListByTag(tag_name, key, _state == 0);

        TagChecker.filter(list, m -> m.tag);

        viewModel.put("_state", _state);
        viewModel.put("list", list);
        viewModel.put("tag_name", tag_name);
        viewModel.put("key", key);

        return view("cfg/key_inner");
    }

    @Mapping("edit")
    public ModelAndView edit(int id, String tag_name) throws SQLException {
        //tips
        ConfigModel tips = DbWaterCfgApi.getConfigByTagName("_system", "key_tips");
        List<String> tipsList = new ArrayList<>();
        if (Utils.isNotEmpty(tips.value)) {
            for (String item : tips.value.split(",")) {
                tipsList.add(item.trim());
            }
        }

        if (tipsList.size() == 0) {
            tipsList.add("app_group_id");
            tipsList.add("user_group_id");
        }


        //model
        KeyModel model = DbWaterCfgKeyApi.getKey(id);

        if (model.key_id == 0) {
            model.access_key = Utils.guid();
            model.access_secret_key = RandomUtils.code(24);
            model.access_secret_salt = RandomUtils.code(16);
            model.is_enabled = 1;
        }

        if (model.tag != null) {
            tag_name = model.tag;
        }

        viewModel.put("tipsList", tipsList);
        viewModel.put("m", model);
        viewModel.put("tag_name", tag_name);
        return view("cfg/key_edit");
    }

    @AuthPermissions(SessionPerms.admin)
    @Mapping("edit/ajax/save")
    public ViewModel saveDo(Integer key_id, String tag, String access_key, String access_secret_key, String access_secret_salt, String label, String description, String metainfo, int is_enabled) throws Exception {

        try {
            boolean result = DbWaterCfgKeyApi.setKey(key_id, tag, access_key, access_secret_key, access_secret_salt, label, description, metainfo, is_enabled);
            if (result) {
                viewModel.code(1, "操作成功");
            } else {
                viewModel.code(0, "操作失败");
            }

            return viewModel;
        } catch (Throwable e) {
            return viewModel.code(0, e.getLocalizedMessage());
        }
    }


    @AuthPermissions(SessionPerms.admin)
    @Mapping("ajax/del")
    public ViewModel delDo(Integer key_id) throws Exception {
        boolean result = DbWaterCfgKeyApi.delKey(key_id);
        if (result) {
            viewModel.code(1, "删除成功");
        } else {
            viewModel.code(0, "删除失败");
        }

        return viewModel;
    }

    @Mapping("ajax/export")
    public void exportDo(Context ctx, String tag, String ids) throws Exception {
        List<KeyModel> list = DbWaterCfgKeyApi.getKeyByIds(ids);

        String jsonD = JsondUtils.encode("water_cfg_key", list);

        String filename2 = "water_cfg_key_" + tag + "_" + Datetime.Now().getDate() + ".jsond";

        ctx.headerSet("Content-Disposition", "attachment; filename=\"" + filename2 + "\"");

        ctx.output(jsonD);
    }

    @AuthPermissions(SessionPerms.admin)
    @Mapping("ajax/import")
    public ViewModel importDo(String tag, UploadedFile file) throws Exception {
        try {
            String jsonD = IOUtils.toString(file.getContent());
            JsondEntity entity = JsondUtils.decode(jsonD);

            if (entity == null || "water_cfg_key".equals(entity.table) == false) {
                return viewModel.code(0, "数据不对！");
            }

            List<KeyModel> list = entity.data.toObjectList(KeyModel.class);

            for (KeyModel m : list) {
                DbWaterCfgKeyApi.impKeyOrRep(tag, m);
            }

            return viewModel.code(1, "ok");
        } catch (Throwable e) {
            return viewModel.code(0, e.getLocalizedMessage());
        }
    }


    @AuthPermissions(SessionPerms.admin)
    @Mapping("ajax/batch")
    public ViewModel batchDo(Context ctx, String tag, Integer act, String ids) throws Exception {
        if (act == null) {
            act = 0;
        }

        DbWaterCfgKeyApi.delKeyByIds(act, ids);

        return viewModel.code(1, "ok");
    }
}
