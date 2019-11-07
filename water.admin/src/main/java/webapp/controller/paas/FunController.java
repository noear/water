package webapp.controller.paas;

import org.noear.water.admin.tools.controller.BaseController;
import org.noear.water.admin.tools.viewModels.ViewModel;
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
import webapp.models.water_paas.PaasFunModel;

import java.sql.SQLException;
import java.util.List;

@XController
@XMapping("/paas/")
public class FunController extends BaseController {

    @XMapping("fun")
    public ModelAndView fun(String tag,String fun_name) throws SQLException{
        List<PaasFunModel> tags = DbPaaSApi.getFunTags();

        BcfTagChecker.filter(tags, m -> m.tag);

        viewModel.put("tags",tags);
        if (TextUtils.isEmpty(tag) == false) {
            viewModel.put("tag",tag);
        } else {
            if (tags.isEmpty() == false) {
                viewModel.put("tag",tags.get(0).tag);
            } else {
                viewModel.put("tag",null);
            }
        }
        viewModel.put("fun_name",fun_name);
        return view("paas/fun");
    }


    //公共函数的iframe 的inner视图。
    @XMapping("fun/inner")
    public ModelAndView funInner(String tag,String fun_name,Integer _state) throws SQLException {
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
        List<PaasFunModel> list = DbPaaSApi.getFunList(tag,fun_name, _state);
        viewModel.put("funs",list);
        viewModel.put("tag",tag);
        viewModel.put("fun_name",fun_name);
        return view("paas/fun_inner");
    }


    //编辑函数跳转
    @XMapping("fun/edit")
    public ModelAndView editFun(int fun_id) throws SQLException {
        PaasFunModel fun = DbPaaSApi.getFunById(fun_id);
        viewModel.put("tag",fun.tag);
        viewModel.put("fun",fun);
        viewModel.put("url_start",Config.paas_uri);
        return view("paas/fun_edit");
    }

    //新增函数跳转
    @XMapping("fun/add")
    public ModelAndView addFun(String tag) {
        viewModel.put("tag",tag);
        viewModel.put("fun",new PaasFunModel());
        viewModel.put("url_start",Config.paas_uri);
        return view("paas/fun_edit");
    }

    //ajax编辑保存功能
    @XMapping("fun/edit/ajax/save")
    public ViewModel funAddSave(Integer fun_id, String code, String tag, String fun_name, String name_display , String note, Integer is_enabled, String args) throws SQLException {
        boolean result = DbPaaSApi.editFun(fun_id, code, tag, fun_name,name_display, note, is_enabled, args);
        if (result) {
            viewModel.code(1, "保存成功！");
        } else {
            viewModel.code(0, "保存失败!");
        }

        return viewModel;
    }
}
