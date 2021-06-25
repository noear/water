package waterapi.dso;

import org.noear.snack.ONode;
import org.noear.solon.core.handle.Context;
import org.noear.water.WW;
import org.noear.water.utils.ThrowableUtils;
import org.noear.weed.Command;

import java.util.Map;

/**
 * Created by noear on 2017/7/27.
 */
public class LogUtils {
    private static final WaterLoggerLocal logger = new WaterLoggerLocal(WW.water_log_api);

    public static WaterLoggerLocal getLogger() {
        return logger;
    }

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

            logger.info(tag, null, null, _from, "", content.toString());
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

            logger.warn(tag, null, tag2, _from, label.toJson(), content);
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

            if (cmd == null) {
                String summary = ONode.stringify(ctx.paramMap());

                logger.error(tag, null, null, _from, summary, ex);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("::Sql= ").append(cmd.text).append("\n");
                sb.append("::Args= ").append(ONode.stringify(cmd.paramMap())).append("\n\n");
                sb.append("::Error= ").append(ThrowableUtils.getString(ex));

                logger.error(tag, null, null, _from, "", sb.toString());
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

            logger.error(tag, tag1, null, _from, summary, ex);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }
}
