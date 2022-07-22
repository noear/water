package wateradmin.controller.tool;

import com.alibaba.fastjson.JSONObject;
import org.noear.solon.Utils;
import org.noear.water.WaterClient;
import org.noear.weed.DataItem;
import org.noear.weed.DataList;
import org.noear.weed.DbContext;
import org.noear.weed.DbProcedure;
import org.noear.water.utils.TextUtils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.ConfigType;
import wateradmin.dso.JtRunner;
import wateradmin.dso.Session;
import wateradmin.dso.db.DbWaterApi;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_tool.ReportModel;
import wateradmin.models.water_cfg.ConfigModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//非单例
@Controller
@Mapping("/tool/report")
public class ReportController extends BaseController {

    static Pattern limit_exp = Pattern.compile("\\s+limit\\s+(\\d+)");

    @Mapping("")
    public ModelAndView query(String tag_name) throws SQLException {
        List<TagCountsModel> tags = DbWaterApi.reportGetTags();

        if (!TextUtils.isEmpty(tag_name)){
            viewModel.put("tag_name",tag_name);
        } else {
            if (tags.size()>0){
                viewModel.put("tag_name",tags.get(0).tag);
            } else {
                viewModel.put("tag_name","");
            }
        }
        viewModel.put("tags",tags);
        return view("tool/report");
    }

    @Mapping("report_inner")
    public ModelAndView reportInner(String tag_name) throws SQLException{
        List<ReportModel> tags = DbWaterApi.reportGetListByTag(tag_name);

        viewModel.put("reports",tags);
        return view("tool/report_inner");
    }

    @Mapping("edit")
    public ModelAndView edit(Integer row_id) throws SQLException{
        List<ConfigModel> cfgs = DbWaterCfgApi.getConfigTagKeyByType(null, ConfigType.db);

        ReportModel report = DbWaterApi.reportGet(row_id);

        viewModel.put("cfgs", cfgs);
        viewModel.put("report",report);
        return view("tool/report_edit");
    }

    @Mapping("edit/ajax/save")
    public ViewModel save(Integer row_id,String tag,String name,String code,String note,String args) throws SQLException{
        boolean isOk = DbWaterApi.reportSave(row_id, tag, name, code,note,args);
        if (isOk){
            viewModel.put("code",1);
            viewModel.put("msg","编辑成功");
        } else{
            viewModel.put("code",0);
            viewModel.put("msg","编辑失败");
        }
        return viewModel;
    }

    @Mapping("edit/ajax/del")
    public ViewModel del(Integer row_id) throws SQLException{
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限");
        }

        boolean isOk = DbWaterApi.reportDel(row_id);

        if (isOk){
            viewModel.put("code",1);
            viewModel.put("msg","删除成功");
        } else{
            viewModel.put("code",0);
            viewModel.put("msg","删除失败");
        }
        return viewModel;
    }

    @Mapping("ajax/getConditionBuild")
    public ViewModel getConditionBuild(Integer row_id) throws SQLException{

        ReportModel report = DbWaterApi.reportGet(row_id);
        boolean hasCondition = false;
        if (!TextUtils.isEmpty(report.args)){

            StringBuilder sb = new StringBuilder();
            String[] argsList = report.args.split(",");

            //JtFunRunner  funRunner = new JtFunRunner("dev");
            for (String s:argsList) {
                String value = "";

                if (s.contains(":")){
                    value = s.substring(s.indexOf(":")+1).trim();
                    s = s.substring(0,s.indexOf(":"));

                    if(value.startsWith("$")) {
                        String code = value.substring(1);
                        value = JtRunner.eval(code, "").toString();
                    }
                }

                sb.append(s+"：<input type='text' alt='"+s+"' value='"+value+"'/>&nbsp;");
            }


            viewModel.put("htm",sb.toString());
            hasCondition = true;
        }

        viewModel.put("hasCondition",hasCondition);
        return viewModel;
    }

    @Mapping(value = "ajax/do")
    public String doQuery(Integer row_id,Boolean is_condition,String conditon_param) throws SQLException{
        JSONObject params = new JSONObject();
        if (!TextUtils.isEmpty(conditon_param))
            params = JSONObject.parseObject(conditon_param);

        ReportModel report = DbWaterApi.reportGet(row_id);

        //1.对不良条件进行过滤
        if (TextUtils.isEmpty(report.code))
            return "没有代码";

        if (report.code.indexOf("::") < 0)
            return "没有指定数据库";

        String code = report.code.replace("\n"," ");

        String code2 = " " + code.split("::")[1].toLowerCase();

        if (code.indexOf(";") >= 0) //不能有分号
            return "不能有;号";

        boolean is_ddl =false;

        if(code2.indexOf(" show create table")==0){ //因为前面加了空隔，所以从1开始
            if (code2.indexOf(" delete ") >= 0 || code2.indexOf(" update ") >= 0 || code2.indexOf(" insert ") >= 0 || code2.indexOf(" truncate ") >= 0)
                return "只支持select操作";

            is_ddl = true;
        }else {

            if (code2.indexOf(" delete ") >= 0 || code2.indexOf(" update ") >= 0 || code2.indexOf(" insert ") >= 0 || code2.indexOf(" truncate ") >= 0 || code2.indexOf(" table ") >= 0)
                return "只支持select操作";

            if (code2.indexOf(" select ") < 0)
                return "只支持select操作";

            if (code2.indexOf(" limit ") < 0)
                return "需要limit行数";
            else {
                Matcher m  = limit_exp.matcher(code2);

                if(m.find()){
                    if (Integer.parseInt(m.group(1)) > 1000) {
                        return "最多不可超过1000条";
                    }
                }else{
                    return "需要limit行数";
                }
            }

            if (code2.indexOf(" join ") > 0)
                return "不支持join操作";
        }

        //2.开始查询数据
        try {
            return exec_sql(code,is_ddl,is_condition,params,report.args);
        } catch (Exception ex) {
            StringBuilder sb = new StringBuilder();
            sb.append("<p>");
            sb.append(Utils.throwableToString(ex));
            sb.append("</p>");
            return sb.toString();
        }
    }

    private String exec_sql(String code, boolean is_ddl, boolean is_condition, JSONObject params,String args) throws SQLException{
        String db = code.split("::")[0].trim();
        String sql = code.split("::")[1].trim();
        if(db.startsWith("--")){
            db = db.substring(2);
        }

        DbContext dbContext =   WaterClient.Config.getByTagKey(db).getDb();

        if(dbContext == null){
            return  "数据库不存在...";
        }

        DataList list = new DataList();
        if (is_condition) {
            DbProcedure call = dbContext.call(sql);
            Map<String, String> map = new HashMap<>();
            String[] argsList = args.split(",");

            for (String s:argsList) {
                String[] ss = s.split(":");
                if (ss.length>1){
                    String val = ss[1];

                    map.put(ss[0],val);
                }
            }


            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String value = (String)entry.getValue();
                String key = entry.getKey().trim();
                String mapKey = key.replaceAll("@", "");
                if (TextUtils.isEmpty(value)){
                    value = map.get(mapKey)==null?"":map.get(mapKey);
                }
                call.set(key,value);
            }
            list = call.log(true).getDataList();
        } else {
            list = dbContext.sql(sql).log(true).getDataList();
        }

        //3.构建输出内容
        if(list != null && list.getRowCount()>0) {
            if(is_ddl){
                return buildDDL(list);
            }else{
                return buildData(list);
            }
        }

        return "";
    }

    private String buildDDL(DataList list){
        StringBuilder sb = new StringBuilder();
        String ddl = list.getRow(0).getString("Create Table");

        sb.append("<pre id='ddl'>");
        sb.append(ddl);
        sb.append("</pre>\n");

        return sb.toString();
    }

    private String buildData(DataList list){
        StringBuilder sb = new StringBuilder();
        DataItem tmp = list.getRow(0);

        //3.1.对列名进行排序
        Set<String> cols = tmp.keys();

        //3.2.构建html
        sb.append("<table class=\"list\">");
        sb.append("<thead><tr>");
        for(String col : cols){
            sb.append("<td>").append(col).append("</td>");
        }
        sb.append("</tr></thead>");
        sb.append("<tbody>");

        for(DataItem item : list.getRows()){
            sb.append("<tr>");
            for(String col : cols){
                sb.append("<td>").append(item.get(col)).append("</td>");
            }
            sb.append("</tr>");
        }

        sb.append("</tbody>");

        return sb.toString();
    }
}
