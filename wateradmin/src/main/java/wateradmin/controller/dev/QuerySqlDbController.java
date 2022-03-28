package wateradmin.controller.dev;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.logging.utils.TagsMDC;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DataItem;
import org.noear.weed.DataList;
import org.noear.weed.DbContext;
import org.slf4j.MDC;
import wateradmin.controller.BaseController;
import wateradmin.dso.ConfigType;
import wateradmin.dso.Session;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.models.water_cfg.ConfigModel;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//非单例
@Slf4j
@Controller
@Mapping("/dev/query_db")
public class QuerySqlDbController extends BaseController {
   @Mapping("")
   public ModelAndView query() throws SQLException {
       List<ConfigModel> cfgs = DbWaterCfgApi.getConfigTagKeyByType(null, ConfigType.db);

       viewModel.put("cfgs", cfgs);

       return view("dev/query_db");
   }

   static Pattern limit_exp = Pattern.compile("\\s+limit\\s+(\\d+,)?(\\d+)");

   @Mapping(value = "ajax/do")
   public String query_do(Context ctx, String code) {

       //1.对不良条件进行过滤
       if (TextUtils.isEmpty(code))
           return "没有代码";

       String code_raw = code;
       code = code.trim();
       if(code.startsWith("--")){
           code = code.substring(2);
       }

       if (code.indexOf("::") < 0 || code.startsWith("tag/key"))
           return "没有指定数据库";

       code = code.replace("\n"," ");

       String code2 = " " + code.split("::")[1].toLowerCase().trim();

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
                   if (Integer.parseInt(m.group(2)) > 50) {
                       return "最多不可超过50条";
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
           return exec_sql(ctx, code, is_ddl, code_raw);
       } catch (Exception ex) {
           StringBuilder sb = new StringBuilder();
           sb.append("<p>");
           sb.append(Utils.throwableToString(ex));
           sb.append("</p>");
           return sb.toString();
       }
   }

   private String exec_sql(Context ctx, String code, boolean is_ddl, String code_raw) throws SQLException {
       String db = code.split("::")[0];
       String sql = code.split("::")[1];

       if (db.indexOf("water/water") >= 0 && sql.toLowerCase().indexOf("water_cfg_properties") > 0) {
           return "只支持业务库查询";
       }

       DbContext dbContext = DbWaterCfgApi.getConfigByTagName(db).getDb();

       DataList list = dbContext.sql(sql).log(true).getDataList();
       String html = "";

       //3.构建输出内容
       if (list != null && list.getRowCount() > 0) {
           if (is_ddl) {
               html = buildDDL(list);
           } else {
               html = buildData(list);
           }
       }

       //记录日志
       TagsMDC.tag0("dev_query_sqldb");
       TagsMDC.tag1(db);
       TagsMDC.tag2(Session.current().getDisplayName());
       log.info(code_raw);

       //返回
       return html;
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

       //Collections.sort(cols);

       //3.2.构建html
       sb.append("<table class=\"list\">");
       sb.append("<thead><tr>");
       for(String col : cols){
           sb.append("<td>").append(col).append("</td>");
       }
       sb.append("</tr></thead>");
       sb.append("<tbody>");

       for(DataItem item : list.getRows()) {
           sb.append("<tr>");
           for (String col : cols) {
               Object val = item.get(col);
               if (val instanceof BigDecimal) {
                   sb.append("<td>").append(((BigDecimal) val).toPlainString()).append("</td>");
               } else {
                   sb.append("<td>").append(val).append("</td>");
               }
           }

           sb.append("</tr>");
       }

       sb.append("</tbody>");

       return sb.toString();
   }
}
