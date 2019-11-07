package webapp.controller.mot;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.admin.tools.controller.BaseController;
import org.noear.water.tools.TextUtils;
import webapp.dao.BcfServiceChecker;
import webapp.dao.db.DbWaterLogApi;
import webapp.models.water_log.LogSqlModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@XController
@XMapping("/mot/")
public class BehaviorController extends BaseController {

    private final static String tableName = "sql_log_behavior";

    //消息异常记录
    @XMapping("behavior")
    public ModelAndView behavior(String tag) throws SQLException {
        List<LogSqlModel> tags = DbWaterLogApi.getSqlServiceTags(tableName);


        BcfServiceChecker.filter(tags, m -> m.tag);

        viewModel.put("tags",tags);

        if (TextUtils.isEmpty(tag) && tags.size()>0) {
            viewModel.put("tag",tags.get(0).tag);
        } else {
            viewModel.put("tag",tag);
        }

        return view("mot/behavior");
    }

    /** state: ALL,SELECT,UPDATE,INSERT,DELETE,OTHER */
    @XMapping("behavior/inner")
    public ModelAndView behavior_inner(Integer page,String tag,String tagx,  String log_date, String path,Integer _state) throws SQLException {
        if(page == null){
            page=1;
        }

        List<LogSqlModel> tag2s = DbWaterLogApi.getSqlOperatorTags(tableName,tag);
        List<LogSqlModel> logs = new ArrayList<>();

        int i_hour = 0;
        int i_date = 0;

        if (TextUtils.isEmpty(log_date) == false) {
            if(log_date.indexOf(".")>0) {
                String[] ss = log_date.split("\\.");
                i_date = Integer.parseInt(ss[0]);
                i_hour = Integer.parseInt(ss[1]);
            }else{
                i_date = Integer.parseInt(log_date);
            }
        }

        String method = null;
        long rowCount = 0;
        int pageSize=50;

        if(_state!=null){
            switch (_state){
                case 1:method = "SELECT";break;
                case 2:method = "UPDATE";break;
                case 3:method = "INSERT";break;
                case 4:method = "DELETE";break;
                case 5:method = "OTHER";break;
            }
        }

        if (!TextUtils.isEmpty(tableName)) {
            rowCount = DbWaterLogApi.getSqlLogsCount(tableName, tag, null, method,0,tagx,path, i_date,i_hour);
            logs = DbWaterLogApi.getSqlLogsByPage(tableName, tag, null, method,0,tagx,path, i_date,i_hour,page,pageSize);
        }

        viewModel.put("pageSize", pageSize);
        viewModel.put("rowCount", rowCount);
        viewModel.put("list",logs);
        viewModel.put("tag2s",tag2s);
        viewModel.put("tag",tag);

        return view("mot/behavior_inner");
    }
}
