package wateradmin.controller.paas;

import com.alibaba.fastjson.JSONObject;


import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.auth.annotation.AuthRoles;
import wateradmin.controller.BaseController;
import wateradmin.dso.SessionRoles;
import wateradmin.dso.db.DbRubberApi;
import wateradmin.models.water_paas.RebberCountModel;
import wateradmin.models.water_paas.RebberLogRequestModel;

import java.sql.SQLException;
import java.util.List;


@Controller
@Mapping("/rubber/")
public class RubberReqRecordController extends BaseController {

    @Mapping("reqrecord")
    public ModelAndView reqRecord(Integer page, Integer pageSize,String key) throws SQLException{
        //return view("rubber/reqrecord");

        if (page == null) {
            page = 1; //从1开始（数据库那边要减1）
        }

        if (pageSize == null || pageSize == 0) {
            pageSize = 17;
        }
        RebberCountModel count = new RebberCountModel();
        List<RebberLogRequestModel> models = DbRubberApi.getReuestList(page,pageSize,key,count);

        viewModel.put("pageSize", pageSize);
        viewModel.put("rowCount", count.getCount());
        viewModel.put("models",models);
        viewModel.put("key",key);

        return view("rubber/reqrecord_inner");
    }

    @AuthRoles({SessionRoles.role_operator, SessionRoles.role_admin})
    @Mapping("reqrecord/exec/scheme")
    public ModelAndView reqrecord_exec_scheme() throws Exception{

        viewModel.set("list",DbRubberApi.getSchemes());

        return view("rubber/reqrecord_exec_scheme");
    }

    @AuthRoles({SessionRoles.role_operator, SessionRoles.role_admin})
    @Mapping("reqrecord/exec/query")
    public ModelAndView reqrecord_exec_query() throws Exception{

        viewModel.set("list",DbRubberApi.getSchemes());

        return view("rubber/reqrecord_exec_query");
    }

    @AuthRoles({SessionRoles.role_operator, SessionRoles.role_admin})
    @Mapping("reqrecord/exec/model")
    public ModelAndView reqrecord_exec_model() throws Exception{

        viewModel.set("list",DbRubberApi.getModelList());

        return view("rubber/reqrecord_exec_model");
    }


    //数据模型右侧列表
//    @Mapping("reqrecord/inner")
//    public ModelAndView inner(Integer page, Integer pageSize,String key) throws SQLException {
//
//        if (page == null) {
//            page = 1; //从1开始（数据库那边要减1）
//        }
//
//        if (pageSize == null || pageSize == 0) {
//            pageSize = 17;
//        }
//        CountModel count = new CountModel();
//        List<LogRequestModel> models = DbRubberApi.getReuestList(page,pageSize,key,count);
//
//        viewModel.put("pageSize", pageSize);
//        viewModel.put("rowCount", count.getCount());
//        viewModel.put("models",models);
//        viewModel.put("key",key);
//        viewModel.put("raas_uri", Config.raas_uri);
//
//        return view("rubber/reqrecord_inner");
//    }

    //请求记录详情
    @Mapping("rerecord/detil")
    public ModelAndView detail(Long log_id) throws SQLException{
        RebberLogRequestModel log = DbRubberApi.getLogReqById(log_id);
        //JSONArray evaluation = DbRubberApi.getEvaluationResult(log.details_json);
        viewModel.put("log",log);

        viewModel.put("matcher", JSONObject.parseObject(log.matcher_json));
        viewModel.put("evaluation", JSONObject.parseObject(log.evaluation_json));

        return view("rubber/reqrecord_detail");
    }
}
