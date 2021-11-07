package wateradmin.controller.log;

import org.noear.solon.core.handle.Context;
import org.noear.solon.core.util.DateUtil;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.TextUtils;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.BcfTagChecker;
import wateradmin.dso.LogFormater;
import wateradmin.dso.TagUtil;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_cfg.LoggerModel;

import java.util.ArrayList;
import java.util.List;

@Controller
@Mapping("/log/")
public class LogController extends BaseController {

    @Mapping("query")
    public ModelAndView index(String tag_name, Context ctx) throws Exception {
        List<TagCountsModel> tags = DbWaterCfgApi.getLoggerTags();

        BcfTagChecker.filter(tags, m -> m.tag);

        tag_name = TagUtil.build(tag_name, tags);

        viewModel.put("tag_name", tag_name);
        viewModel.put("tags", tags);
        return view("log/query");
    }

    @Mapping("query/inner")
    public ModelAndView index_inner(String tag_name, String logger, String tagx, String time, long startLogId, Integer level, Context ctx) throws Exception {

        List<LoggerModel> loggers = DbWaterCfgApi.getLoggerByTag(tag_name);

        if (TextUtils.isEmpty(logger)) {
            logger = ctx.cookie("wateradmin_log__tag_" + tag_name);
        }

        if ("null".equals(logger)) {
            logger = "";
        }

        if (TextUtils.isEmpty(logger)) {
            if (loggers.size() > 0) {
                logger = loggers.get(0).logger;
            }
        }

        //增加session 记忆
        TagUtil.cookieSet(tag_name);
        ctx.cookieSet("wateradmin_log__tag_" + tag_name, logger);


        List list = new ArrayList<>();

        boolean allowSearch = false;


        if (!TextUtils.isEmpty(logger)) {
            LoggerModel log = DbWaterCfgApi.getLogger(logger);

            try {
                long timestamp = 0;
                if (TextUtils.isNotEmpty(time)) {
                    timestamp = DateUtil.parse(time.replace("+", " ")).getTime();
                }

                allowSearch = ProtocolHub.getLogSource(logger).allowSearch();

                list = ProtocolHub.logQuerier.query(logger, level, 50, tagx, startLogId, timestamp);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            viewModel.put("log", log);
            viewModel.put("logger", log.logger);
        } else {
            viewModel.put("logger", "");
        }

        viewModel.put("allowSearch", allowSearch);
        viewModel.put("tag_name", tag_name);
        viewModel.put("list", list);
        viewModel.put("logs", loggers);

        viewModel.put("formater", LogFormater.instance);

        return view("log/query_inner");
    }
}
