package wateradmin.controller.mot;

import org.noear.water.WW;
import org.noear.water.model.TagCountsM;
import org.noear.water.protocol.model.log.LogModel;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.TextUtils;


import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.BcfServiceChecker;
import wateradmin.dso.db.DbWaterLogApi;

import java.util.List;


@Controller
@Mapping("/mot/")
public class SqlController extends BaseController {

    private final static String logger = WW.logger_water_log_sql_p;

    //消息异常记录
    @Mapping("sql")
    public ModelAndView sql(String tag_name) throws Exception {
        List<TagCountsM> tags = DbWaterLogApi.getSqlServiceTags(logger);

        BcfServiceChecker.filter(tags, m -> m.tag);

        viewModel.put("tags", tags);

        if (TextUtils.isEmpty(tag_name) && tags.size() > 0) {
            viewModel.put("tag_name", tags.get(0).tag);
        } else {
            viewModel.put("tag_name", tag_name);
        }

        return view("mot/sql");
    }


    /**
     * state: ALL,SELECT,UPDATE,INSERT,DELETE,OTHER
     */
    @Mapping("sql/inner")
    public ModelAndView behavior_inner(String tag_name, String tagx, String log_date, Integer _state) throws Exception {
        List<TagCountsM> tag2s = DbWaterLogApi.getSqlSecondsTags(logger, tag_name);


        int seconds = 0;
        if (TextUtils.isEmpty(tagx) == false) {
            seconds = Integer.parseInt(tagx);
        }


        String method = null;

        if (_state != null) {
            switch (_state) {
                case 1:
                    method = "SELECT";
                    break;
                case 2:
                    method = "UPDATE";
                    break;
                case 3:
                    method = "INSERT";
                    break;
                case 4:
                    method = "DELETE";
                    break;
                case 5:
                    method = "OTHER";
                    break;
            }
        }

        long timestamp = 0;
        if (TextUtils.isNotEmpty(log_date)) {
            if (log_date.contains(".")) {
                timestamp = Datetime.parse(log_date, "yyyyMMdd.HH").getTicks();
            } else {
                timestamp = Datetime.parse(log_date, "yyyyMMdd").getTicks();
            }
        }


        List<LogModel> logs = DbWaterLogApi.getSqlLogsByPage(logger, tag_name, method, seconds, null, null, 0, timestamp);


        viewModel.put("refdate", Datetime.Now().addDay(-2).getDate());
        viewModel.put("list", logs);
        viewModel.put("tag2s", tag2s);
        viewModel.put("tag_name", tag_name);

        return view("mot/sql_inner");
    }
}