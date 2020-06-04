package waterapp.dso;

import org.noear.snack.ONode;
import org.noear.solon.core.XContext;
import org.noear.water.log.Level;
import org.noear.water.protocol.ProtocolHub;
import waterapp.Config;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

/**
 * Created by noear on 2017/7/27.
 */
public class LogUtils {
    private static final String logger_api = "water_log_api";


    public static void info(String summary, XContext context) {
        try {
            String tag = context.path();

            if (tag == null) {
                return;
            }

            String ip = IPUtils.getIP(context);

            Map<String, String> pnames = context.paramMap();

            ONode args = new ONode();
            if (pnames != null) {
                pnames.forEach((k, v) -> {
                    args.set(k, v);
                });
            }

            ProtocolHub.logStorer.append(logger_api, Level.INFO, tag, ip, summary, args.toJson(), Config.localHost);
            //DbWaterLogApi.addLog(logger_api, tag, ip, "", label, args.toJson());
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public static void info(String tag, String summary, String content) {
        info(tag, null, summary, content);
    }

    public static void info(String tag, String tag1, String summary, String content) {
        try {
            ProtocolHub.logStorer.append(logger_api, Level.INFO, tag, tag1, summary, content, Config.localHost);
            //DbWaterLogApi.addLog(logger_api, tag, tag1, "", label, content);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.print(tag + "::\r\n");
        System.out.print(content);
        System.out.print("\r\n");
    }

    public static void debug(String tag, String summary , String txt) {
        StringBuilder sb = new StringBuilder();
        sb.append(txt);

        try {
            ProtocolHub.logStorer.append(logger_api, Level.DEBUG, tag,  summary, sb.toString(), Config.localHost);
            //DbWaterLogApi.addLog(logger_debug, tag, "", "", label, sb.toString());
        }catch (Exception ex){
            ex.printStackTrace();
        }

        System.out.print(tag + "::\r\n");
        System.out.print(sb.toString());
        System.out.print("\r\n");
    }

    public static void error(XContext context, Exception ex) {
        try {
            Map<String,String> pnames = context.paramMap();
            String tag = context.path();

            String content = getFullStackTrace(ex);
            ONode label = new ONode();

            if(pnames!=null){
                pnames.forEach((k,v)->{
                    label.set(k, v);
                });
            }

            ProtocolHub.logStorer.append(logger_api, Level.ERROR, tag,  label.toJson(), content, Config.localHost);
            //DbWaterLogApi.addLog(logger_error, tag, "", "", label.toJson(), content);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public static void error(String tag, String tag1, String summary, Exception ex) {
        try {
            String content = getFullStackTrace(ex);

            ProtocolHub.logStorer.append(logger_api, Level.ERROR, tag,  tag1, summary, content, Config.localHost);
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
