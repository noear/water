package wateradmin.controller.paas;

import com.alibaba.fastjson.JSONObject;
import org.noear.solon.auth.annotation.AuthPermissions;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.UploadedFile;
import org.noear.water.utils.*;
import org.noear.weed.DataItem;
import org.noear.weed.DataList;


import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.TagChecker;
import wateradmin.dso.Session;
import wateradmin.dso.SessionPerms;
import wateradmin.dso.db.DbRubberApi;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_cfg.ConfigModel;
import wateradmin.models.water_paas.RebberBlockModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Mapping("/rubber/")
public class RubberBlockController extends BaseController {

    //数据block
    @Mapping("block")
    public ModelAndView block(Integer block_id,String tag_name) throws SQLException{

        List<TagCountsModel> tags = DbRubberApi.getBlockTags();
        TagChecker.filter(tags, m -> m.tag);

        viewModel.put("tags",tags);

        if (TextUtils.isEmpty(tag_name) == false) {
            viewModel.put("tag_name", tag_name);
        } else {
            if (tags.isEmpty() == false) {
                viewModel.put("tag_name", tags.get(0).tag);
            } else {
                viewModel.put("tag_name", null);
            }
        }
        viewModel.put("block_id",block_id);

        return view("rubber/block");
    }

    //数据block右侧列表
    @Mapping("block/inner")
    public ModelAndView blockInner(Integer block_id,String tag_name) throws SQLException{
        if(block_id == null){
            block_id = 0;
        }

        if(block_id>0){
            return blockEdit(block_id);
        }else {
            List<RebberBlockModel> blocks = DbRubberApi.getBlocks(tag_name);

            viewModel.put("tag_name",tag_name);
            viewModel.put("blocks", blocks);

            return view("rubber/block_inner");
        }
    }

    //数据块items
    @Mapping("block/items")
    public ModelAndView blockItems(Integer block_id,String fname,String fval) throws Exception{
        RebberBlockModel block = DbRubberApi.getBlockById(block_id);

        DataList list = DbRubberApi.getBlockItems(block, fname, fval);

        viewModel.put("cols", block.cols());
        viewModel.put("items",list.getRows());
        viewModel.put("block",block);

        return view("rubber/block_items");
    }

    //编辑数据块
    @Mapping("block/edit")
    public ModelAndView blockEdit(Integer block_id) throws SQLException{
        if (block_id == null) {
            block_id = 0;
        }

        List<ConfigModel> configs= DbWaterCfgApi.getDbConfigsEx();
        List<String> option_sources = new ArrayList<>();
        for(ConfigModel config : configs){
            option_sources.add(config.tag+"/"+config.key);
        }
        viewModel.put("option_sources",option_sources);

        RebberBlockModel block = DbRubberApi.getBlockById(block_id);

        if(TextUtils.isEmpty(block.app_expr)){
            block.app_expr = "return false;";
        }

        viewModel.put("block",block);

        return view("rubber/block_edit");
    }

    //保存数据块编辑
    @AuthPermissions({SessionPerms.operator, SessionPerms.admin})
    @Mapping("block/edit/ajax/save")
    public ViewModel editSave(Integer block_id, String tag, String name, String name_display, String related_db, String related_tb,
                              String struct, String app_expr, Integer is_editable, String note) throws SQLException{
        long result = DbRubberApi.blockSave(block_id, tag, name, name_display, related_db, related_tb, struct, app_expr,is_editable,note);

        if (result>0) {
            viewModel.put("code",1);
            viewModel.put("msg","编辑成功");
        } else {
            viewModel.put("code",0);
            viewModel.put("msg","编辑失败");
        }
        return viewModel;
    }

    @AuthPermissions({SessionPerms.operator, SessionPerms.admin})
    @Mapping("block/edit/ajax/del")
    public ViewModel del(Integer block_id) throws SQLException{
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限！");
        }

        long result = DbRubberApi.blockDel(block_id);

        if (result>0) {
            viewModel.put("code",1);
            viewModel.put("msg","编辑成功");
        } else {
            viewModel.put("code",0);
            viewModel.put("msg","编辑失败");
        }
        return viewModel;
    }

    //d-block item内容编辑页
    @Mapping("block/item/edit")
    public ModelAndView itemsEdit(String item_key,Integer block_id) throws SQLException{

        RebberBlockModel block = DbRubberApi.getBlockById(block_id);
        DataItem item = DbRubberApi.getBlockItem(block, item_key);


        viewModel.put("item_key",item_key);
        viewModel.put("block",block);
        viewModel.put("item",item);
        viewModel.put("cols",block.cols());

        return view("rubber/block_items_edit");
    }

    //d-block item编辑保存
    @AuthPermissions({SessionPerms.operator, SessionPerms.admin})
    @Mapping("block/item/edit/ajax/save")
    public ViewModel itemEditSave(Integer block_id,String item_key,String data) throws SQLException {

        RebberBlockModel block = DbRubberApi.getBlockById(block_id);
        DbRubberApi.setBlockItem(block, item_key, JSONObject.parseObject(data));

        viewModel.put("code", 1);
        viewModel.put("msg", "编辑成功");

        return viewModel;
    }


    //批量导出
    @AuthPermissions({SessionPerms.operator, SessionPerms.admin})
    @Mapping("block/ajax/export")
    public void exportDo(Context ctx, String tag, String ids) throws Exception {
        List<RebberBlockModel> list = DbRubberApi.getBlockByIds(ids);

        String jsonD = JsondUtils.encode("rubber_block", list);

        String filename2 = "water_raasfile_block_" + tag + "_" + Datetime.Now().getDate() + ".jsond";

        ctx.headerSet("Content-Disposition", "attachment; filename=\"" + filename2 + "\"");
        ctx.output(jsonD);
    }


    //批量导入
    @AuthPermissions(SessionPerms.admin)
    @Mapping("block/ajax/import")
    public ViewModel importDo(Context ctx, String tag, UploadedFile file) throws Exception {
        String jsonD = IOUtils.toString(file.content);
        JsondEntity entity = JsondUtils.decode(jsonD);

        if(entity == null || "rubber_block".equals(entity.table) == false){
            return viewModel.code(0, "数据不对！");
        }

        List<RebberBlockModel> list = entity.data.toObjectList(RebberBlockModel.class);


        for (RebberBlockModel vm : list) {
            DbRubberApi.blockImp(tag, vm);
        }

        return viewModel.code(1,"ok");
    }
}
