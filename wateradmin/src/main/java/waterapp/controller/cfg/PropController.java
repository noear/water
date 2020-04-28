package waterapp.controller.cfg;

import org.noear.snack.ONode;
import org.noear.snack.core.TypeRef;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XFile;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.IOUtils;
import org.noear.water.utils.JsonxUtils;
import org.noear.water.utils.TextUtils;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import waterapp.controller.BaseController;
import waterapp.dso.BcfTagChecker;
import waterapp.dso.db.DbWaterCfgApi;
import waterapp.models.TagCountsModel;
import waterapp.models.water_cfg.ConfigModel;
import waterapp.viewModels.ViewModel;
import waterapp.dso.Session;

import java.sql.SQLException;
import java.util.List;

@XController
@XMapping("/cfg/prop")
public class PropController extends BaseController{
    @XMapping("")
    public ModelAndView index(String tag_name) throws SQLException {
        List<TagCountsModel> tags = DbWaterCfgApi.getConfigTags();

        BcfTagChecker.filter(tags, m -> m.tag);

        if (TextUtils.isEmpty(tag_name) == false) {
            viewModel.put("tag_name",tag_name);
        } else {
            if (tags.isEmpty() == false) {
                viewModel.put("tag_name",tags.get(0).tag);
            } else {
                viewModel.put("tag_name",null);
            }
        }
        viewModel.put("tags",tags);
        return view("cfg/prop");
    }
    //跳转编辑页面。
    @XMapping("edit")
    public ModelAndView editConfig(String tag_name, Integer row_id) throws SQLException {
        if(row_id == null){
            row_id = 0;
        }

        ConfigModel cfg = DbWaterCfgApi.getConfig(row_id);

        if(cfg.row_id > 0){
            tag_name = cfg.tag;
        }

        viewModel.put("row_id",row_id);
        viewModel.put("cfg",cfg);
        viewModel.put("tag_name",tag_name);
        return view("cfg/prop_edit");
    }

    //编辑、保存功能。
    @XMapping("edit/ajax/save")
    public ViewModel save(Integer row_id,String tag,String key,Integer type,String value) throws SQLException {
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限");
        }

        boolean result = DbWaterCfgApi.setConfig(row_id, tag, key, type, value);

        if (result) {
            viewModel.code(1, "保存成功");
        } else {
            viewModel.code(0, "保存失败");
        }

        return viewModel;
    }

    //编辑、保存功能。
    @XMapping("edit/ajax/del")
    public ViewModel del(Integer row_id) throws SQLException {
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限");
        }

        DbWaterCfgApi.delConfig(row_id);

        return viewModel.code(1, "操作成功");
    }

    @XMapping("inner")
    public ModelAndView innerDo(XContext ctx, String tag_name,String key) throws SQLException {
        int state = ctx.paramAsInt("state",1);

        List<ConfigModel> list = DbWaterCfgApi.getConfigsByTag(tag_name,key, state);

        viewModel.put("list",list);
        viewModel.put("tag_name",tag_name);
        viewModel.put("state",state);
        viewModel.put("key",key);

        return view("cfg/prop_inner");
    }



    //批量导出
    @XMapping("ajax/export")
    public void exportDo(XContext ctx, String tag, String ids) throws Exception {
        List<ConfigModel> list = DbWaterCfgApi.getConfigByIds(ids);
        String json = ONode.stringify(list);
        String jsonX = JsonxUtils.encode(json);

        String filename2 = "water_config_" + tag + "_" + Datetime.Now().getDate() + ".jsonx";

        ctx.headerSet("Content-Disposition", "attachment; filename=\"" + filename2 + "\"");
        ctx.output(jsonX);
    }


    //批量导入
    @XMapping("ajax/import")
    public ViewModel importDo(XContext ctx, String tag, XFile file) throws Exception {
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限！");
        }

        String jsonX = IOUtils.toString(file.content);
        String json = JsonxUtils.decode(jsonX);

        List<ConfigModel> list = ONode.deserialize(json, new TypeRef<List<ConfigModel>>() {
        }.getClass());

        for (ConfigModel m : list) {
            DbWaterCfgApi.impConfig(tag, m);
        }

        return viewModel.code(1,"ok");
    }

    //批量处理
    @XMapping("ajax/batch")
    public ViewModel batchDo(XContext ctx, String tag, Integer act, String ids) throws Exception {
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限！");
        }

        if(act == null){
            act = 0;
        }

        DbWaterCfgApi.delConfigByIds(act, ids);

        return viewModel.code(1, "ok");
    }
}
