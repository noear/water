package waterapp.controller.rubber;

import com.alibaba.fastjson.JSONObject;


import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import waterapp.Config;
import waterapp.controller.BaseController;
import waterapp.dso.db.DbRubberApi;
import waterapp.models.water_rebber.CountModel;
import waterapp.models.water_rebber.LogRequestModel;

import java.sql.SQLException;
import java.util.List;

/**
 * @Author:Fei.chu
 * @Date:Created in 10:40 2018/05/15
 * @Description:请求记录
 */

@XController
@XMapping("/rubber/")
public class ReqRecordController extends BaseController {

    @XMapping("reqrecord")
    public ModelAndView reqRecord(Integer page, Integer pageSize,String key) throws SQLException{
        //return view("rubber/reqrecord");

        if (page == null) {
            page = 1; //从1开始（数据库那边要减1）
        }

        if (pageSize == null || pageSize == 0) {
            pageSize = 17;
        }
        CountModel count = new CountModel();
        List<LogRequestModel> models = DbRubberApi.getReuestList(page,pageSize,key,count);

        viewModel.put("pageSize", pageSize);
        viewModel.put("rowCount", count.getCount());
        viewModel.put("models",models);
        viewModel.put("key",key);

        return view("rubber/reqrecord_inner");
    }

    @XMapping("reqrecord/exec/scheme")
    public ModelAndView reqrecord_exec_scheme() throws Exception{

        viewModel.set("list",DbRubberApi.getSchemes());

        return view("rubber/reqrecord_exec_scheme");
    }

    @XMapping("reqrecord/exec/query")
    public ModelAndView reqrecord_exec_query() throws Exception{

        viewModel.set("list",DbRubberApi.getSchemes());

        return view("rubber/reqrecord_exec_query");
    }

    @XMapping("reqrecord/exec/model")
    public ModelAndView reqrecord_exec_model() throws Exception{

        viewModel.set("list",DbRubberApi.getModelList());

        return view("rubber/reqrecord_exec_model");
    }


    //数据模型右侧列表
//    @XMapping("reqrecord/inner")
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
    @XMapping("rerecord/detil")
    public ModelAndView detail(Long log_id) throws SQLException{
        LogRequestModel log = DbRubberApi.getLogReqById(log_id);
        //JSONArray evaluation = DbRubberApi.getEvaluationResult(log.details_json);
        viewModel.put("log",log);

        viewModel.put("matcher", JSONObject.parseObject(log.matcher_json));
        viewModel.put("evaluation", JSONObject.parseObject(log.evaluation_json));

        return view("rubber/reqrecord_detail");
    }
}
