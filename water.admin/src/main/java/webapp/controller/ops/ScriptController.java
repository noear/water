package webapp.controller.ops;

import com.alibaba.fastjson.JSON;
import org.noear.snack.ONode;


import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.admin.tools.controller.BaseController;
import org.noear.water.admin.tools.dso.Session;
import org.noear.water.admin.tools.viewModels.ViewModel;
import org.noear.water.tools.TextUtils;
import webapp.dao.db.DbWindApi;
import webapp.models.water.ConfigModel;
import webapp.models.water_wind.WindFormalParamModel;
import webapp.models.water_wind.WindScriptModel;

import java.sql.SQLException;
import java.util.List;

@XController
@XMapping("/ops/")
public class ScriptController extends BaseController {

    @XMapping("script")
    public ModelAndView script(String tag_name) throws SQLException {
        List<WindScriptModel> tags = DbWindApi.getScriptTags();
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
        return view("ops/script");
    }

    @XMapping("script/inner")
    public ModelAndView scriptInner(String tag_name,Integer _state) throws SQLException {
            if (_state != null) {
            viewModel.put("_state", _state);
            int state = _state;
            if (state == 0) {
                _state = 1;
            } else if (state == 1) {
                _state = 0;
            }
        }
        if (_state == null) {
            _state = 1;
        }
        List<WindScriptModel> list = DbWindApi.getScriptByTagNameAndState(tag_name,_state);
        viewModel.put("list",list);
        viewModel.put("tag_name",tag_name);
        return view("ops/script_inner");
    }

    @XMapping("script/add")
    public ModelAndView serverAdd(String tag_name) throws SQLException{
        List<WindScriptModel> tags = DbWindApi.getScriptTags();
        List<ConfigModel> accounts = DbWindApi.getIAASAccionts();

        ONode data =  new ONode();
        ONode list = new ONode().asArray();
        ONode item = new ONode().set("param_name","").set("param_note","");
        list.add(item);
        data.set("list",list);

        viewModel.put("argsList",data.toJson());
        viewModel.put("accounts",accounts);
        viewModel.put("tags",tags);
        viewModel.put("tag_name",tag_name);
        viewModel.put("script",new WindScriptModel());
        return view("ops/script_edit");
    }


    @XMapping("script/edit")
    public ModelAndView scriptEdit(Integer script_id) throws SQLException{
        WindScriptModel script = DbWindApi.getScriptByID(script_id);
        List<WindScriptModel> tags = DbWindApi.getScriptTags();

        List<WindFormalParamModel> scriptParam = DbWindApi.getFormalParam(script_id);

        ONode data =  new ONode();
        ONode list = new ONode().asArray();
        for (WindFormalParamModel paramModel : scriptParam) {
            ONode item = new ONode().set("param_name",paramModel.param_name).set("param_note",paramModel.param_note);
            list.add(item);
        }
        data.set("list",list);

        viewModel.put("argsList",data.toJson());
        viewModel.put("tags",tags);
        viewModel.put("script",script);
        return view("ops/script_edit");
    }


    @XMapping("script/edit/ajax/save")
    public ViewModel scriptEditSave(Long script_id, String tag, String name, Integer type, String args_json, Integer env, Integer is_enabled, String code) throws Exception {
        boolean is_admin = Session.current().getIsAdmin()>0;

        if (is_admin == false) {
            return viewModel.code(0,"没有权限！");
        }
        if (env==null){
            env = 0;
        }
        String operator = Session.current().getUserName();

        Long result = DbWindApi.editScript(script_id,tag,name,type,env,is_enabled,code,operator);
        if (result>0){
            String jsonList = ONode.load(args_json).get("list").toJson();
            List<WindFormalParamModel> formalParams = JSON.parseArray(jsonList, WindFormalParamModel.class);

            if (script_id==null || script_id==0){
                script_id = result;
            }
            //清除旧的参数
            DbWindApi.deleteFormalParam(script_id);
            StringBuffer tags = new StringBuffer();
            for (int i = 0; i < formalParams.size(); i++) {
                Boolean paramResult = DbWindApi.setFormalParam(script_id,i+1,formalParams.get(i).param_name,formalParams.get(i).param_note);
                if (!paramResult){
                    viewModel.code(0,"参数保存异常!");
                    return viewModel;
                }
                tags.append(formalParams.get(i).param_name);
                if (i<formalParams.size()-1){
                    tags.append(",");
                }
            }
            DbWindApi.setScriptArgs(script_id,tags.toString());
            viewModel.code(1,"保存成功！");
        }else{
            viewModel.code(0,"保存失败！");
        }
        return viewModel;
    }
}
