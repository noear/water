package webapp.controller.smp;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.admin.tools.controller.BaseController;
import org.noear.water.admin.tools.viewModels.ViewModel;
import org.noear.water.tools.TextUtils;
import webapp.dao.BcfTagChecker;
import webapp.dao.db.DbWaterSyncApi;
import webapp.models.water.SynchronousModel;

import java.sql.SQLException;
import java.util.List;

@XController
@XMapping("/smp/")
public class DataSynController extends BaseController {
    //plan视图跳转
    @XMapping("sync")
    public ModelAndView plan(String tag) throws SQLException {
        List<SynchronousModel> tags = DbWaterSyncApi.getSyncTags();

        BcfTagChecker.filter(tags, m -> m.tag);

        viewModel.put("tags", tags);
        if (TextUtils.isEmpty(tag) == false) {
            viewModel.put("tag", tag);
        } else {
            if (tags.isEmpty() == false) {
                viewModel.put("tag", tags.get(0).tag);
            } else {
                viewModel.put("tag", null);
            }
        }
        return view("smp/sync");
    }

    //数据同步的iframe inner视图。
    @XMapping("sync/inner")
    public ModelAndView planInner(String tag,String sync_name,Integer _state) throws SQLException {
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
        List<SynchronousModel> list = DbWaterSyncApi.getSyncList(tag,sync_name, _state);
        viewModel.put("synchronous",list);
        viewModel.put("tag",tag);
        return view("smp/sync_inner");
    }

    //跳转数据同步新增页面
    @XMapping("sync/add")
    public ModelAndView add() throws SQLException{
        viewModel.put("syn",new SynchronousModel());
        return view("smp/sync_edit");
    }




    //跳转数据同步编辑页面
    @XMapping("sync/edit")
    public ModelAndView edit(Integer sync_id) throws SQLException{
        SynchronousModel syn = DbWaterSyncApi.getSyncById(sync_id);
        viewModel.put("syn",syn);
        return view("smp/sync_edit");
    }

    //保存数据同步编辑
    @XMapping("sync/edit/ajax/save")
    public ViewModel saveEdit(Integer syn_id, Integer type, String name, String tag, Integer interval, String target, String target_pk, String source,
                              String source_model, String alarm_mobile, Integer is_enabled) throws SQLException{
        boolean result = DbWaterSyncApi.updSync(syn_id,type, name, tag, interval, target, target_pk, source, source_model, alarm_mobile, is_enabled);
        if (result){
            viewModel.code(1,"保存成功!");
        } else {
            viewModel.code(0,"保存失败!");
        }

        return viewModel;
    }

}
