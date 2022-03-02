package waterapi.dso;

import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.core.handle.Context;
import org.noear.water.WW;
import org.noear.weed.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;

/**
 * Created by noear on 2017/7/27.
 */
public class LogUtils {
    private static final Logger logger = LoggerFactory.getLogger(WW.logger_water_log_api);

    public static void info(Context ctx, long _times) {
        try {
            String tag = ctx.path();

            if (tag == null) {
                return;
            }

            String _from = FromUtils.getFromName(ctx);

            MDC.put("tag0", tag);
            MDC.put("tag3", _from);

            StringBuilder content = new StringBuilder(200);

            content.append("> Header: ").append(ONode.stringify(ctx.headerMap())).append("\n");
            content.append("> Param: ").append(ONode.stringify(ctx.paramMap())).append("\n");
            content.append("T Elapsed time: ").append(_times).append("ms");
            content.append("\n\n");
            content.append("< Body: ").append(ONode.stringify(ctx.result));

            logger.info(content.toString());

        } catch (Throwable ee) {
            //不能再转入别的日志了
            ee.printStackTrace();
        }
    }

    public static void warn(Context ctx, String tag2, String content) {
        try {
            String _from = FromUtils.getFromName(ctx);

            Map<String, String> pnames = ctx.paramMap();
            String tag = ctx.path();

            ONode label = new ONode();

            if (pnames != null) {
                pnames.forEach((k, v) -> {
                    label.set(k, v);
                });
            }

            MDC.put("tag0", tag);
            MDC.put("tag2", tag2);
            MDC.put("tag3", _from);

            logger.warn("{}\r\n{}", label.toJson(), content);
        } catch (Throwable ee) {
            //不能再转入别的日志了
            ee.printStackTrace();
        }
    }

    public static void error(Context ctx, Throwable ex) {
        try {
            if (ctx == null) {
                MDC.put("tag0", "global");
                MDC.put("tag1", ex.getClass().getSimpleName());
                logger.error("{}", ex);
                return;
            }

            String _from = FromUtils.getFromName(ctx);
            Command cmd = ctx.attr("weed_cmd");
            String param = ONode.stringify(ctx.paramMap());

            String tag = ctx.path();

            MDC.put("tag0", tag);
            MDC.put("tag1", ex.getClass().getSimpleName());
            MDC.put("tag3", _from);

            if (cmd == null) {
                logger.error("> Param: {}\n\n< Error: {}", param, ex);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("> Param: ").append(param).append("\n");
                sb.append("$ Sql: ").append(cmd.text).append("\n");
                sb.append("$ Sql-Param: ").append(ONode.stringify(cmd.paramMap())).append("\n\n");
                sb.append("< Error: ").append(Utils.throwableToString(ex));

                logger.error(sb.toString());
            }
        } catch (Throwable ee) {
            //不能再转入别的日志了
            ee.printStackTrace();
        }
    }
}
