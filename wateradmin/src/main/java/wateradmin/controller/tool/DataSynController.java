package wateradmin.controller.tool;

import org.noear.water.utils.TextUtils;


import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.BcfTagChecker;
import wateradmin.dso.ConfigType;
import wateradmin.dso.Session;
import wateradmin.dso.db.DbWaterApi;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water.SynchronousModel;
import wateradmin.models.water_cfg.ConfigModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;

@Controller
@Mapping("/tool/sync")
public class DataSynController extends BaseController {
    //plan视图跳转
    @Mapping("")
    public ModelAndView plan(String tag_name) throws SQLException {
        List<TagCountsModel> tags = DbWaterApi.syncGetTags();

        BcfTagChecker.filter(tags, m -> m.tag);

        viewModel.put("tags", tags);
        if (TextUtils.isEmpty(tag_name) == false) {
            viewModel.put("tag_name", tag_name);
        } else {
            if (tags.isEmpty() == false) {
                viewModel.put("tag_name", tags.get(0).tag);
            } else {
                viewModel.put("tag_name", null);
            }
        }
        return view("tool/sync");
    }

    //数据同步的iframe inner视图。
    @Mapping("inner")
    public ModelAndView planInner(String tag_name,String sync_name,Integer _state) throws SQLException {
        if (_state!=null) {
            viewModel.put("_state", _state);
            int state = _state;
            if (state == 0) {
                _state = 1;
            } else if (state == 1) {
                _state = 0;
            }
        }
        if(_state==null)
            _state = 1;
        List<SynchronousModel> list = DbWaterApi.syncGetList(tag_name,sync_name, _state);
        viewModel.put("synchronous",list);
        viewModel.put("tag_name",tag_name);
        return view("tool/sync_inner");
    }

    //跳转数据同步编辑页面
    @Mapping("edit")
    public ModelAndView edit(Integer sync_id) throws SQLException{
        if(sync_id == null){
            sync_id = 0;
        }

        List<ConfigModel> cfgs = DbWaterCfgApi.getConfigTagKeyByType(null, ConfigType.db);
        SynchronousModel syn = DbWaterApi.syncGet(sync_id);

        viewModel.put("cfgs", cfgs);
        viewModel.put("syn",syn);
        return view("tool/sync_edit");
    }

    //保存数据同步编辑
    @Mapping("edit/ajax/save")
    public ViewModel saveEdit(Integer syn_id, Integer type, String name, String tag, Integer interval, String target, String target_pk,
                              String source_model, String alarm_mobile, Integer is_enabled) throws SQLException{
        boolean result = DbWaterApi.syncSave(syn_id,type, name, tag, interval, target, target_pk, source_model, alarm_mobile, is_enabled);
        if (result){
            viewModel.code(1,"保存成功!");
        } else {
            viewModel.code(0,"保存失败!");
        }

        return viewModel;
    }

    @Mapping("edit/ajax/del")
    public ViewModel del(Integer syn_id) throws SQLException {
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限");
        }

        boolean result = DbWaterApi.syncDel(syn_id);
        if (result) {
            viewModel.code(1, "删除成功!");
        } else {
            viewModel.code(0, "删除失败!");
        }

        return viewModel;
    }
}
