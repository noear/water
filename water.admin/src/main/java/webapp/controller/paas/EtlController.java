package webapp.controller.paas;

import org.noear.water.admin.tools.controller.BaseController;
import org.noear.water.admin.tools.viewModels.ViewModel;
import org.noear.water.tools.Datetime;
import org.noear.water.tools.TextUtils;
import org.noear.weed.DataItem;
import org.noear.weed.DataList;
import org.noear.weed.DbContext;


import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import webapp.Config;
import webapp.dao.BcfTagChecker;
import webapp.dao.db.DbPaaSApi;
import webapp.models.water_paas.PaasEtlModel;
import webapp.utils.TimeUtil;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;


@XController
@XMapping("/paas/")
public class EtlController extends BaseController {

    //进入etl视图
    @XMapping("etl")
    public ModelAndView etl(String tag,String etl_name) throws SQLException {
        List<PaasEtlModel> tags = DbPaaSApi.getETLTags();

        BcfTagChecker.filter(tags, m -> m.tag);

        viewModel.put("tags",tags);
        if (TextUtils.isEmpty(tag) == false) {
            viewModel.put("tag", tag);
        } else {
            if (tags.isEmpty() == false) {
                viewModel.put("tag", tags.get(0).tag);
            } else {
                viewModel.put("tag", null);
            }
        }
        viewModel.put("etl_name",etl_name);
        return view("paas/etl");
    }

    //etl列表
    @XMapping("etl/inner")
    public ModelAndView inner(String tag, String etl_name, Integer _state) throws SQLException {
        if (_state != null) {
            viewModel.put("_state", _state);
            int state = _state;
            if (state == 0) {
                _state = 1;
            } else if (state == 1) {
                _state = 0;
            }
        }
        if (_state == null)
            _state = 1;

        List<PaasEtlModel> etls = DbPaaSApi.getEtlList(tag,etl_name,_state);

        viewModel.put("etls",etls);
        viewModel.put("tag", tag);
        viewModel.put("etl_name",etl_name);
        return view("paas/etl_inner");
    }

    //新增etl跳转
    @XMapping("etl/add")
    public ModelAndView addEtl() throws SQLException {
        String begin_time = TimeUtil.liveTimeFormat(new Date()).replaceAll(" ", "T");
        viewModel.put("begin_time", begin_time);
        PaasEtlModel etl = new PaasEtlModel();
        etl.e_max_instance = 1;
        etl.t_max_instance = 1;
        etl.l_max_instance = 1;
        viewModel.put("etl",etl);
        return view("paas/etl_edit");
    }

    //编辑etl跳转
    @XMapping("etl/edit")
    public ModelAndView editEtl(Long etl_id) throws SQLException {
        PaasEtlModel etl = DbPaaSApi.getEtlById(etl_id);
//        String timestamp_cursor = "";

        viewModel.put("tagName", etl.tag);
        viewModel.put("etl", etl);
        viewModel.put("cursor", etl.cursor_str());
        return view("paas/etl_edit");
    }

    //ajax编辑保存功能
    @XMapping("etl/edit/ajax/save")
    public ViewModel etlEditSave(Integer etl_id, String code, String tag, String etl_name,
                                 Integer is_enabled, Integer e_enabled, Integer t_enabled, Integer l_enabled, Integer is_update,
                                 String alarm_mobile, String cursor, Integer cursor_type, Integer e_max_instance,
                                 Integer t_max_instance, Integer l_max_instance) throws SQLException {
        try {
            if(cursor_type == null){
                cursor_type = 0;
            }

            long cursor_val = 0;
            if (is_update != null) {
                if(is_update == 1) {
                    if(cursor.indexOf("-")>0){
                        cursor_val = Datetime.parse(cursor, "yyyy-MM-dd HH:mm").getTicks();
                    }else{
                        cursor_val = Long.parseLong(cursor);
                    }
                }
            }
            boolean result = DbPaaSApi.editEtl(etl_id, code, tag, etl_name, is_enabled,e_enabled,t_enabled,l_enabled,
                    is_update,cursor_type,cursor_val,alarm_mobile,e_max_instance,t_max_instance,l_max_instance);
        if (result) {
            viewModel.code(1, "保存成功！");
        } else {
            viewModel.code(0, "保存失败!");
        }
        } catch (ParseException e) {
            viewModel.code(0, "时间坐标转换失败!");
        }
        return viewModel;
    }

}
