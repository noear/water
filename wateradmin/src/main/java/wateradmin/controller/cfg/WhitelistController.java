package wateradmin.controller.cfg;

import org.noear.snack.ONode;
import org.noear.snack.core.TypeRef;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.UploadedFile;
import org.noear.water.utils.*;
import wateradmin.controller.BaseController;
import wateradmin.dso.BcfTagChecker;
import wateradmin.dso.Session;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.models.TagCountsModel;
import wateradmin.dso.TagUtil;
import wateradmin.models.water_cfg.WhitelistModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;


@Controller
@Mapping("/cfg/whitelist")
public class WhitelistController extends BaseController {

    //IP白名单列表
    @Mapping("")
    public ModelAndView whitelist(String tag_name) throws Exception {
        List<TagCountsModel> tags = DbWaterCfgApi.getWhitelistTags();

        BcfTagChecker.filter(tags, m -> m.tag);

        tag_name = TagUtil.build(tag_name,tags);

        viewModel.put("tag_name",tag_name);
        viewModel.put("tags",tags);
        return view("cfg/whitelist");
    }

    @Mapping("inner")
    public ModelAndView innerDo(Context ctx, String tag_name, String key) throws Exception {
        int state = ctx.paramAsInt("state",1);

        List<WhitelistModel> list = DbWaterCfgApi.getWhitelistByTag(tag_name, key, state);

        BcfTagChecker.filter(list, m -> m.tag);

        viewModel.put("list", list);
        viewModel.put("tag_name", tag_name);
        viewModel.put("state", state);
        viewModel.put("key",key);

        return view("cfg/whitelist_inner");
    }

    //跳转ip白名单新增页面
    @Mapping("edit")
    public ModelAndView whitelistAdd(Integer id, String tag_name) throws SQLException {
        WhitelistModel model = null;
        if (id != null) {
            model = DbWaterCfgApi.getWhitelist(id);
            viewModel.put("m", model);
        } else {
            model = new WhitelistModel();
            viewModel.put("m", model);
        }

        if (model.tag != null) {
            tag_name = model.tag;
        }

        viewModel.put("tag_name", tag_name);
        return view("cfg/whitelist_edit");
    }

    //保存ip白名单新增
    @Mapping("edit/ajax/save")
    public ViewModel saveWhitelistAdd(Integer row_id, String tag,String type, String value, String note) throws Exception {
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限");
        }

        boolean result = DbWaterCfgApi.setWhitelist(row_id, tag, type, value, note);
        if (result) {
            DbWaterCfgApi.reloadWhitelist();

            viewModel.code(1, "操作成功");
        } else {
            viewModel.code(0, "操作失败");
        }

        return viewModel;
    }



    //删除IP白名单记录
    @Mapping("ajax/del")
    public ViewModel saveWhitelistDel(Integer row_id) throws Exception {
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限");
        }

        boolean result = DbWaterCfgApi.delWhitelist(row_id);
        if (result) {
            DbWaterCfgApi.reloadWhitelist();

            viewModel.code(1, "删除成功");
        } else {
            viewModel.code(0, "删除失败");
        }

        return viewModel;
    }



    //批量导出
    @Mapping("ajax/export")
    public void exportDo(Context ctx, String tag, String ids) throws Exception {
        List<WhitelistModel> list = DbWaterCfgApi.getWhitelistByIds(ids);
        String json = ONode.stringify(list);
        String jsonX = JsonxUtils.encode(json);

        String filename2 = "water_whitelist_" + tag + "_" + Datetime.Now().getDate() + ".jsonx";

        ctx.headerSet("Content-Disposition", "attachment; filename=\"" + filename2 + "\"");
        ctx.output(jsonX);
    }


    //批量导入
    @Mapping("ajax/import")
    public ViewModel importDo(Context ctx, String tag, UploadedFile file) throws Exception {
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限！");
        }

        String jsonX = IOUtils.toString(file.content);
        String json = JsonxUtils.decode(jsonX);

        List<WhitelistModel> list = ONode.deserialize(json, new TypeRef<List<WhitelistModel>>() {
        }.getClass());

        for (WhitelistModel m : list) {
            DbWaterCfgApi.impWhitelist(tag, m);
        }

        return viewModel.code(1,"ok");
    }

    //批量删除
    @Mapping("ajax/batch")
    public ViewModel batchDo(Context ctx, String tag, Integer act, String ids) throws Exception {
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限！");
        }

        if(act == null){
            act = 0;
        }

        DbWaterCfgApi.delWhitelistByIds(act, ids);

        return viewModel.code(1, "ok");
    }
}
