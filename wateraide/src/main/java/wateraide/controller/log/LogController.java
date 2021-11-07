package wateraide.controller.log;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.water.WW;
import org.noear.water.model.ConfigM;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.model.log.LogFormater;
import org.noear.water.protocol.solution.LogSourceFactoryImpl;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.TextUtils;
import wateraide.Config;
import wateraide.controller.BaseController;
import wateraide.dso.BcfTagChecker;
import wateraide.dso.TagUtil;
import wateraide.dso.db.DbWaterCfgApi;
import wateraide.models.TagCountsModel;
import wateraide.models.water_cfg.LoggerModel;

import java.util.ArrayList;
import java.util.List;

@Controller
@Mapping("/log/")
public class LogController extends BaseController {

    private void tryInit() throws Exception {
        if (ProtocolHub.logSourceFactory == null) {
            ProtocolHub.config = Config::getCfg;
            ConfigM logCfg = Config.getCfg(WW.water, WW.water_log_store);

            if (logCfg != null) {
                try {
                    ProtocolHub.logSourceFactory = new LogSourceFactoryImpl(logCfg, DbWaterCfgApi::getLogger);
                } catch (Exception e) {
                    ProtocolHub.logSourceFactory = null;
                    throw e;
                }
            }
        }
    }

    @Mapping("query")
    public ModelAndView index(String tag_name, Context ctx) throws Exception {
        tryInit();

        List<TagCountsModel> tags = DbWaterCfgApi.getLoggerTags();

        BcfTagChecker.filterWaterTag(tags, m -> m.tag);

        tag_name = TagUtil.build(tag_name, tags);

        viewModel.put("tag_name", tag_name);
        viewModel.put("tags", tags);
        return view("log/query");
    }

    @Mapping("query/inner")
    public ModelAndView index_inner(String tag_name, String logger, String tagx, String time, long startLogId, Integer level, Context ctx) throws Exception {
        tryInit();

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
                    timestamp = Datetime.parse(time.replace("+", " "), "yyyy-MM-dd HH:mm:ss.SSS").getTicks();
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
