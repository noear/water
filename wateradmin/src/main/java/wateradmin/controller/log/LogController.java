package wateradmin.controller.log;

import org.noear.solon.core.XContext;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.utils.TextUtils;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.BcfTagChecker;
import wateradmin.dso.TagUtil;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_cfg.LoggerModel;

import java.util.ArrayList;
import java.util.List;

@XController
@XMapping("/log/")
public class LogController extends BaseController {

    @XMapping("query")
    public ModelAndView index(String tag_name, XContext ctx) throws Exception {
        List<TagCountsModel> tags = DbWaterCfgApi.getLoggerTags();

        BcfTagChecker.filter(tags, m -> m.tag);

        tag_name = TagUtil.build(tag_name, tags);

        viewModel.put("tag_name", tag_name);
        viewModel.put("tags", tags);
        return view("log/query");
    }

    @XMapping("query/inner")
    public ModelAndView index_inner(String tag_name, String logger, String tagx, Integer log_date, Long log_id, Integer level, XContext ctx) throws Exception {

        List<LoggerModel> loggers = DbWaterCfgApi.getLoggerByTag(tag_name);

        if (TextUtils.isEmpty(logger)) {
            logger = ctx.cookie("wateradmin_log__tag_" + tag_name);
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


        String trace_id = null;
        String tag = null, tag1 = null, tag2 = null, tag3 = null;


        if (TextUtils.isNotEmpty(tagx)) {
            if(tagx.startsWith("*")){
                trace_id = tagx.substring(1);
            }else {
                String[] ss = tagx.split("@");
                if (ss.length > 0) {
                    tag = ss[0];
                }

                if (ss.length > 1) {
                    tag1 = ss[1];
                }

                if (ss.length > 2) {
                    tag2 = ss[2];
                }

                if (ss.length > 3) {
                    tag3 = ss[3];
                }
            }
        }

        if (!TextUtils.isEmpty(logger)) {
            LoggerModel log = DbWaterCfgApi.getLogger(logger);

            try {
                list = ProtocolHub.logQuerier.query(logger, trace_id, level, 50, tag, tag1, tag2, tag3, log_date, log_id);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            viewModel.put("log", log);
            viewModel.put("logger", log.logger);
        } else {
            viewModel.put("logger", "");
        }

        viewModel.put("tag_name", tag_name);
        viewModel.put("list", list);
        viewModel.put("logs", loggers);

        return view("log/query_inner");
    }
}
