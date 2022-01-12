package wateradmin.controller.log;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.util.DateAnalyzer;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.model.log.LogFormater;
import org.noear.water.protocol.model.log.LogModel;
import org.noear.water.utils.TextUtils;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.TagChecker;
import wateradmin.dso.TagUtil;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_cfg.LoggerModel;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@Mapping("/log/")
public class LogController extends BaseController {

    @Mapping("query")
    public ModelAndView index(String tag_name, Context ctx) throws Exception {
        List<TagCountsModel> tags = DbWaterCfgApi.getLoggerTags();

        TagChecker.filter(tags, m -> m.tag);

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


        long pageSize = 50;
        List<LogModel> list = new ArrayList<>();
        boolean allowSearch = false;


        if (!TextUtils.isEmpty(logger)) {
            LoggerModel loggerModel = DbWaterCfgApi.getLogger(logger);

            try {
                long timestamp = 0;
                if (TextUtils.isNotEmpty(time)) {
                    timestamp = DateAnalyzer.getGlobal().parse(time.replace("+", " ")).getTime();
                }

                allowSearch = ProtocolHub.getLogSource(logger).allowSearch();

                list = ProtocolHub.logQuerier.query(logger, level, 50, tagx, startLogId, timestamp);
            } catch (Exception ex) {
                log.error("{}",ex);
            }

            viewModel.put("log", loggerModel);
            viewModel.put("logger", loggerModel.logger);
        } else {
            viewModel.put("logger", "");
        }

        viewModel.put("allowSearch", allowSearch);
        viewModel.put("tag_name", tag_name);
        viewModel.put("pageSize", pageSize);
        viewModel.put("list", list);
        viewModel.put("listSize", list.size());
        viewModel.put("logs", loggers);

        if (list.size() > 0) {
            viewModel.put("lastId", list.get(list.size() - 1).log_id);
        } else {
            viewModel.put("lastId", 0L);
        }

        viewModel.put("formater", LogFormater.instance);

        return view("log/query_inner");
    }
}
