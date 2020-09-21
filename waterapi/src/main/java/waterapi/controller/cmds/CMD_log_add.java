package waterapi.controller.cmds;

import org.noear.water.log.Level;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.TextUtils;

import java.util.Date;

/**
 * Created by noear on 2017/7/19.
 */
public class CMD_log_add extends CMDBase {
    static final String log_fulltime_formt = "yyyy-MM-dd HH:mm:ss";

    @Override
    protected boolean isLogging() {
        return false;
    }

    @Override
    protected void cmd_exec() throws Exception {
        String logger = get("logger");

        if (checkParamsIsOk(logger) == false) {
            return;
        }

        String tag = get("tag", "");
        String tag1 = get("tag1", "");
        String tag2 = get("tag2", "");
        String tag3 = get("tag3", "");


        int level = getInt("level", 0);

        String summary = get("summary");
        String content = get("content");

        String from = get("from");
        String trace_id = get("trace_id");

        String log_fulltime_str = get("log_fulltime");
        Date log_fulltime = null;

        if (TextUtils.isEmpty(log_fulltime_str) == false) {
            log_fulltime = Datetime.parse(log_fulltime_str, log_fulltime_formt).getFulltime();
        }

        if (TextUtils.isNotEmpty(trace_id)) {
            if (trace_id.length() > 40) {
                trace_id = trace_id.substring(0, 40);
            }
        }

        ProtocolHub.logStorer.write(logger, trace_id, Level.of(level), tag, tag1, tag2, tag3, summary, content, from, log_fulltime);
        data.set("code", 1);
        data.set("msg", "success");
    }
}
