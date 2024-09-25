package waterapi.controller.log;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Whitelist;
import org.noear.water.model.LogLevel;
import org.noear.water.model.LogM;
import org.noear.water.protocol.utils.SnowflakeUtils;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.TextUtils;
import waterapi.controller.UapiBase;
import waterapi.dso.LogPipelineLocal;

import java.util.Date;

/**
 * 添加日志
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Whitelist
@Controller
public class CMD_log_add extends UapiBase {
    static final String log_fulltime_formt = "yyyy-MM-dd HH:mm:ss";

    /**
     * @param logger   日志记录器name
     * @param level    日志级别
     * @param content  内容
     * @param from     来自
     * @param trace_id 链跟跟踪ID
     */
    @NotEmpty("logger")
    @Mapping("/log/add/")
    public Result cmd_exec(Context ctx, String logger, int level, String content,
                           String from, String trace_id) throws Exception {

        String log_fulltime_str = ctx.param("log_fulltime");

        LogM log = new LogM();

        log.log_id = SnowflakeUtils.genId(); //ProtocolHub.idBuilder.getLogId(log.logger);
        log.group = ctx.paramOrDefault("group", "");
        log.service = ctx.paramOrDefault("app_name", "");

        log.tag = ctx.paramOrDefault("tag", "");
        log.tag1 = ctx.paramOrDefault("tag1", "");
        log.tag2 = ctx.paramOrDefault("tag2", "");
        log.tag3 = ctx.paramOrDefault("tag3", "");
        log.tag4 = ctx.paramOrDefault("tag4", "");

        log.class_name = ctx.param("class_name");
        log.thread_name = ctx.param("thread_name");

        if (TextUtils.isNotEmpty(log_fulltime_str)) {
            log.log_fulltime = Datetime.parse(log_fulltime_str, log_fulltime_formt).getFulltime();
        }else{
            log.log_fulltime = new Date();
        }

        if (TextUtils.isNotEmpty(trace_id)) {
            if (trace_id.length() > 40) {
                trace_id = trace_id.substring(0, 40);
            }

            log.trace_id = trace_id;
        }

        log.level = LogLevel.of(level).code;
        log.content = content;
        log.from = from;

        log.logger = logger;
        log.log_id = SnowflakeUtils.genId();

        LogPipelineLocal.singleton().add(log);

        return Result.succeed();
    }
}
