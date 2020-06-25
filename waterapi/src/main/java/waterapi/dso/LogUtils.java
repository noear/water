package waterapi.dso;

import org.noear.snack.ONode;
import org.noear.solon.core.XContext;
import org.noear.water.WCX;
import org.noear.water.log.Level;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.utils.TextUtils;
import waterapi.Config;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

/**
 * Created by noear on 2017/7/27.
 */
public class LogUtils {
    private static final String logger_api = WCX.water_log_api;


    public static void info(String summary, XContext ctx) {
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

            if(TextUtils.isEmpty(summary)){
                summary = FromUtils.getFrom(ctx);
            }

            ProtocolHub.logStorer.append(logger_api, Level.INFO, tag, null, null, _from, summary, args.toJson(), Config.localHost);
            //DbWaterLogApi.addLog(logger_api, tag, ip, "", label, args.toJson());
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public static void error(XContext ctx, Exception ex) {
        try {
            String _from = FromUtils.getFromName(ctx);

            Map<String, String> pnames = ctx.paramMap();
            String tag = ctx.path();

            String content = getFullStackTrace(ex);
            ONode label = new ONode();

            if (pnames != null) {
                pnames.forEach((k, v) -> {
                    label.set(k, v);
                });
            }

            ProtocolHub.logStorer.append(logger_api, Level.ERROR, tag, null, null, _from, label.toJson(), content, Config.localHost);
            //DbWaterLogApi.addLog(logger_error, tag, "", "", label.toJson(), content);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public static void error(String tag, String tag1, String summary, Exception ex) {
        try {
            String _from = FromUtils.getFromName(XContext.current());

            String content = getFullStackTrace(ex);

            ProtocolHub.logStorer.append(logger_api, Level.ERROR, tag, tag1, null, _from, summary, content, Config.localHost);
            //DbWaterLogApi.addLog(logger_error, tag, tag1, "", label, content);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public static String getFullStackTrace(Throwable e) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(baos));
        return baos.toString();
    }

}
