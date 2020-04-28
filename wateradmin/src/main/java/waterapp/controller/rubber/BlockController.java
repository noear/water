package waterapp.controller.rubber;

import com.alibaba.fastjson.JSONObject;
import org.noear.snack.ONode;
import org.noear.snack.core.TypeRef;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XFile;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.IOUtils;
import org.noear.water.utils.JsonxUtils;
import org.noear.weed.DataItem;
import org.noear.weed.DataList;
import org.noear.water.utils.TextUtils;


import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import waterapp.controller.BaseController;
import waterapp.dso.BcfTagChecker;
import waterapp.dso.Session;
import waterapp.dso.db.DbRubberApi;
import waterapp.dso.db.DbWaterCfgApi;
import waterapp.models.TagCountsModel;
import waterapp.models.water_cfg.ConfigModel;
import waterapp.models.water_rebber.BlockModel;
import waterapp.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:Fei.chu
 * @Date:Created in 15:59 2018/06/12
 * @Description:数据block
 */

@XController
@XMapping("/rubber/")
public class BlockController extends BaseController {

    //数据block
    @XMapping("block")
    public ModelAndView block(Integer block_id,String tag_name) throws SQLException{

        List<TagCountsModel> tags = DbRubberApi.getBlockTags();
        BcfTagChecker.filter(tags, m -> m.tag);

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
    @XMapping("block/inner")
    public ModelAndView blockInner(Integer block_id,String tag_name) throws SQLException{
        if(block_id == null){
            block_id = 0;
        }

        if(block_id>0){
            return blockEdit(block_id);
        }else {
            List<BlockModel> blocks = DbRubberApi.getBlocks(tag_name);

            viewModel.put("tag_name",tag_name);
            viewModel.put("blocks", blocks);

            return view("rubber/block_inner");
        }
    }

    //数据块items
    @XMapping("block/items")
    public ModelAndView blockItems(Integer block_id,String fname,String fval) throws Exception{
        BlockModel block = DbRubberApi.getBlockById(block_id);

        DataList list = DbRubberApi.getBlockItems(block, fname, fval);

        viewModel.put("cols", block.cols());
        viewModel.put("items",list.getRows());
        viewModel.put("block",block);

        return view("rubber/block_items");
    }

    //编辑数据块
    @XMapping("block/edit")
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

        BlockModel block = DbRubberApi.getBlockById(block_id);

        if(TextUtils.isEmpty(block.app_expr)){
            block.app_expr = "return false;";
        }

        viewModel.put("block",block);

        return view("rubber/block_edit");
    }

    //保存数据块编辑
    @XMapping("block/edit/ajax/save")
    public ViewModel editSave(Integer block_id,String tag,String name,String name_display,String related_db,String related_tb,
                              String struct,String app_expr,Integer is_editable,String note) throws SQLException{
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

    @XMapping("block/edit/ajax/del")
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
    @XMapping("block/item/edit")
    public ModelAndView itemsEdit(String item_key,Integer block_id) throws SQLException{

        BlockModel block = DbRubberApi.getBlockById(block_id);
        DataItem item = DbRubberApi.getBlockItem(block, item_key);


        viewModel.put("item_key",item_key);
        viewModel.put("block",block);
        viewModel.put("item",item);
        viewModel.put("cols",block.cols());

        return view("rubber/block_items_edit");
    }

    //d-block item编辑保存
    @XMapping("block/item/edit/ajax/save")
    public ViewModel itemEditSave(Integer block_id,String item_key,String data) throws SQLException {

        BlockModel block = DbRubberApi.getBlockById(block_id);
        DbRubberApi.setBlockItem(block, item_key, JSONObject.parseObject(data));

        viewModel.put("code", 1);
        viewModel.put("msg", "编辑成功");

        return viewModel;
    }


    //批量导出
    @XMapping("block/ajax/export")
    public void exportDo(XContext ctx, String tag, String ids) throws Exception {
        List<BlockModel> list = DbRubberApi.getBlockByIds(ids);

        String json = ONode.stringify(list);
        String jsonX = JsonxUtils.encode(json);

        String filename2 = "water_raasfile_block_" + tag + "_" + Datetime.Now().getDate() + ".jsonx";

        ctx.headerSet("Content-Disposition", "attachment; filename=\"" + filename2 + "\"");
        ctx.output(jsonX);
    }


    //批量导入
    @XMapping("block/ajax/import")
    public ViewModel importDo(XContext ctx, String tag, XFile file) throws Exception {
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限！");
        }

        String jsonX = IOUtils.toString(file.content);
        String json = JsonxUtils.decode(jsonX);

        List<BlockModel> list = ONode.deserialize(json, new TypeRef<List<BlockModel>>() {
        }.getClass());


        for (BlockModel vm : list) {
            DbRubberApi.blockImp(tag, vm);
        }

        return viewModel.code(1,"ok");
    }
}
