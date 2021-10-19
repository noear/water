package waterapi.dso;

import org.noear.snack.ONode;
import org.noear.solon.core.handle.Context;
import org.noear.water.WW;
import org.noear.water.utils.ThrowableUtils;
import org.noear.weed.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;

/**
 * Created by noear on 2017/7/27.
 */
public class LogUtils {
    private static final Logger logger = LoggerFactory.getLogger(WW.water_log_api);

    public static void info(Context ctx) {
        try {
            String tag = ctx.path();

            if (tag == null) {
                return;
            }

            String _from = FromUtils.getFromName(ctx);

            StringBuilder content = new StringBuilder(200);

            content.append("> ").append(ONode.stringify(ctx.paramMap()));
            content.append("\n\n");
            content.append("< ").append(ONode.stringify(ctx.result));

            MDC.put("tag0", tag);
            MDC.put("tag3", _from);

            logger.info(content.toString());
        } catch (Exception ee) {
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
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public static void error(Context ctx, Throwable ex) {
        if (ctx == null) {
            return;
        }

        try {
            Command cmd = ctx.attr("weed_cmd");
            String _from = FromUtils.getFromName(ctx);

            String tag = ctx.path();

            MDC.put("tag0", tag);
            MDC.put("tag3", _from);

            if (cmd == null) {
                String summary = ONode.stringify(ctx.paramMap());

                logger.error("{}\r\n{}", summary, ex);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("::Sql= ").append(cmd.text).append("\n");
                sb.append("::Args= ").append(ONode.stringify(cmd.paramMap())).append("\n\n");
                sb.append("::Error= ").append(ThrowableUtils.getString(ex));

                logger.error(sb.toString());
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public static void error(Context ctx, String tag, String tag1, String summary, Throwable ex) {
        try {
            String _from = null;
            if (ctx != null) {
                _from = FromUtils.getFromName(ctx);
            }

            MDC.put("tag0", tag);
            MDC.put("tag1", tag1);
            MDC.put("tag3", _from);

            logger.error("{}\r\n{}", summary, ex);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }
}
