package waterapi.controller.log;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Whitelist;
import org.noear.water.log.Level;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.TextUtils;
import waterapi.controller.UapiBase;

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
     * @param summary  摘要
     * @param content  内容
     * @param from     来自
     * @param trace_id 链跟跟踪ID
     */
    @NotEmpty("logger")
    @Mapping("/log/add/")
    public Result cmd_exec(Context ctx, String logger, int level, String summary, String content,
                           String from, String trace_id) throws Exception {

        String tag = ctx.param("tag", "");
        String tag1 = ctx.param("tag1", "");
        String tag2 = ctx.param("tag2", "");
        String tag3 = ctx.param("tag3", "");

        String class_name = ctx.param("class_name");
        String thread_name = ctx.param("thread_name");

        String log_fulltime_str = ctx.param("log_fulltime");
        Date log_fulltime = null;

        if (TextUtils.isNotEmpty(log_fulltime_str)) {
            log_fulltime = Datetime.parse(log_fulltime_str, log_fulltime_formt).getFulltime();
        }

        if (TextUtils.isNotEmpty(trace_id)) {
            if (trace_id.length() > 40) {
                trace_id = trace_id.substring(0, 40);
            }
        }


        ProtocolHub.logStorer.write(logger, trace_id, Level.of(level), tag, tag1, tag2, tag3, summary, content, from, log_fulltime, class_name, thread_name);

        return Result.succeed();
    }
}
