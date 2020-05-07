package waterapp.controller.mot;

import org.noear.water.utils.TextUtils;


import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import waterapp.controller.BaseController;
import waterapp.dso.BcfServiceChecker;
import waterapp.dso.db.DbWaterLogApi;
import waterapp.models.water_log.LogSqlModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@XController
@XMapping("/mot/")
public class BehaviorController extends BaseController {

    private final static String tableName = "water_mot_log_bcf";

    //消息异常记录
    @XMapping("behavior")
    public ModelAndView behavior(String tag_name) throws SQLException {
        List<LogSqlModel> tags = DbWaterLogApi.getSqlServiceTags(tableName);


        BcfServiceChecker.filter(tags, m -> m.tag);

        viewModel.put("tags",tags);

        if (TextUtils.isEmpty(tag_name) && tags.size()>0) {
            viewModel.put("tag_name",tags.get(0).tag);
        } else {
            viewModel.put("tag_name",tag_name);
        }

        return view("mot/behavior");
    }

    /** state: ALL,SELECT,UPDATE,INSERT,DELETE,OTHER */
    @XMapping("behavior/inner")
    public ModelAndView behavior_inner(Integer page,String tag_name,String tagx,  String log_date, String path,Integer _state) throws SQLException {
        if(page == null){
            page=1;
        }

        List<LogSqlModel> tag2s = DbWaterLogApi.getSqlOperatorTags(tableName,tag_name);
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
            rowCount = DbWaterLogApi.getSqlLogsCount(tableName, tag_name, null, method,0,tagx,path, i_date,i_hour);
            logs = DbWaterLogApi.getSqlLogsByPage(tableName, tag_name, null, method,0,tagx,path, i_date,i_hour,page,pageSize);
        }

        viewModel.put("pageSize", pageSize);
        viewModel.put("rowCount", rowCount);
        viewModel.put("list",logs);
        viewModel.put("tag2s",tag2s);
        viewModel.put("tag_name",tag_name);

        return view("mot/behavior_inner");
    }
}
