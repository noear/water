package webapp.controller.paas;

import org.noear.water.admin.tools.controller.BaseController;
import org.noear.water.admin.tools.viewModels.ViewModel;
import org.noear.water.tools.TextUtils;


import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import webapp.Config;
import webapp.dao.BcfTagChecker;
import webapp.models.water_paas.PaasTmlModel;
import webapp.dao.db.DbPaaSApi;
import webapp.models.water_paas.PaasApiModel;

import java.sql.SQLException;
import java.util.List;


@XController
@XMapping("/paas/")
public class ApiController extends BaseController {

    @XMapping("api")
    public ModelAndView api(String tag_name,String api_name) throws SQLException{
        List<PaasApiModel> tags = DbPaaSApi.getApiTags();

        BcfTagChecker.filter(tags, m -> m.tag);

        viewModel.put("tags",tags);
        if (TextUtils.isEmpty(tag_name) == false) {
            viewModel.put("tag_name",tag_name);
        } else {
            if (tags.isEmpty() == false) {
                viewModel.put("tag_name",tags.get(0).tag);
            } else {
                viewModel.put("tag_name",null);
            }
        }
        viewModel.put("api_name",api_name);
        return view("paas/api");
    }


    //iframe 的inner视图。
    @XMapping("api/inner")
    public ModelAndView api_inner(String tag_name,String api_name,Integer _state) throws SQLException {
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
        List<PaasApiModel> list = DbPaaSApi.getApiList(tag_name,api_name, _state);
        String url_start = Config.paas_uri;
        viewModel.put("url_start",url_start);
        viewModel.put("apis",list);
        viewModel.put("tag_name",tag_name);
        viewModel.put("api_name",api_name);
        return view("paas/api_inner");
    }


    //编辑接口跳转
    @XMapping("api/edit")
    public ModelAndView api_edit(String tag_name, Integer api_id) throws SQLException {
        if(api_id == null){
            api_id = 0;
        }

        PaasApiModel api = null;
        if(api_id == 0) {
            api = new PaasApiModel();
            api.is_get = 1;
            api.is_post = 1;
        }
        else{
            api = DbPaaSApi.getApiById(api_id);
            tag_name = api.tag;
        }

        List<PaasTmlModel> tmlList = DbPaaSApi.tmlGetTagNames();

        viewModel.put("tag_name",tag_name);
        viewModel.put("url_start",Config.paas_uri);
        viewModel.put("api",api);
        viewModel.put("tmlList",tmlList);
        return view("paas/api_edit");
    }

    //ajax编辑保存功能
    @XMapping("api/edit/ajax/save")
    public ViewModel api_edit_ajax_save(Integer api_id, String code, String tag, String api_name, String note, Integer cache_time,
                                        Integer is_enabled, String args, Integer is_get, Integer is_post) throws SQLException {

        boolean result = DbPaaSApi.editApi(api_id, code, tag, api_name, note, cache_time, is_enabled, args, is_get, is_post);

        if (result) {
            viewModel.code(1, "保存成功！");
        } else {
            viewModel.code(0, "保存失败！");
        }

        return viewModel;
    }
}
