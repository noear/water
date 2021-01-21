package waterapi.dso;

import org.noear.snack.ONode;
import org.noear.solon.core.handle.Context;
import org.noear.water.WW;
import org.noear.water.log.Level;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.utils.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

/**
 * Created by noear on 2017/7/27.
 */
public class LogUtils {
    private static final WaterLoggerLocal logger = new WaterLoggerLocal(WW.water_log_api);

    public static WaterLoggerLocal getLogger() {
        return logger;
    }

    public static void info(String summary, Context ctx) {
        try {
            String tag = ctx.path();

            if (tag == null) {
                return;
            }

            String _from = FromUtils.getFromName(ctx);


            Map<String, String> pnames = ctx.paramMap();

            ONode args = new ONode();
            if (pnames != null) {
                pnames.forEach((k, v) -> {
                    args.set(k, v);
                });
            }

            if (TextUtils.isEmpty(summary)) {
                summary = FromUtils.getFrom(ctx);
            }

            logger.info(tag, null, null, _from, summary, args.toJson());
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public static void warn(Context ctx, String tag2,String content) {
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
        if(ctx == null){
            return;
        }

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

            logger.error(tag, null, null, _from, label.toJson(), ex);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public static void error(String tag, String tag1, String summary, Throwable ex) {
        try {
            String _from = FromUtils.getFromName(Context.current());

            logger.error(tag, tag1, null, _from, summary, ex);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

//    public static String getFullStackTrace(Throwable e) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        e.printStackTrace(new PrintStream(baos));
//        return baos.toString();
//    }
}
