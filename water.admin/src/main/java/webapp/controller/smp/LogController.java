package webapp.controller.smp;


import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import webapp.controller.BaseController;
import webapp.dao.db.DbWaterApi;
import webapp.dao.db.DbWaterLogApi;
import webapp.models.water.LoggerModel;
import webapp.models.water_log.LogModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:Fei.chu
 * @Description:日志查询
 */

@XController
@XMapping("/smp/")
public class LogController extends BaseController{

    @XMapping("log")
    public ModelAndView index(String tableName, String tagx,  Integer log_date,Long log_id,String project, Integer level) throws Exception {
        List<LoggerModel> tags = DbWaterApi.getLoggerTags();
        List<LoggerModel> loggers = DbWaterApi.getLoggers(project);

        List<LogModel> logs = new ArrayList<>();

        if (log_date == null)
            log_date = 0;
        if (log_id == null)
            log_id = 0L;

        String tag=null, tag1=null, tag2=null;
        if(TextUtils.isEmpty(tagx)==false) {
            String[] ss = tagx.split("@");
            if(ss.length>0){
                tag = ss[0];
            }

            if(ss.length>1){
                tag1 = ss[1];
            }

            if(ss.length>2){
                tag2 = ss[2];
            }
        }

        if (!TextUtils.isEmpty(tableName)) {
            logs = DbWaterLogApi.getLogs(tableName, tag, tag1, tag2, log_date,log_id,level);
            LoggerModel log = DbWaterApi.getLog(tableName);
            viewModel.put("log",log);
        }
        viewModel.put("tags",tags);
        viewModel.put("project",project);
        viewModel.put("list",logs);
        viewModel.put("logs",loggers);

        return view("smp/log");
    }
}
