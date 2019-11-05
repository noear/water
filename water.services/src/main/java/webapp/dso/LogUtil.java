package webapp.dso;

import org.noear.snack.ONode;
import org.noear.solon.core.XContext;
import org.noear.water.tools.ThrowableUtils;
import org.noear.water.tools.log.Level;
import webapp.dso.db.DbLogApi;
import webapp.utils.IPUtil;

public class LogUtil {
    private static final String logger_name = "water_log_api";

    public static void info(XContext context) {
        info(null, context);
    }

    public static void info(String summary, XContext context) {
        ONode data = new ONode().setAll(context.paramMap());

        info(context.path(), summary, data.toJson());
    }

    public static void info(String tag, String summary, String content) {
        doInfo(tag, null, summary, content);
    }

    public static void doInfo(String tag, String tag1, String summary, String content) {
        try {
            String from = IPUtil.getIP(XContext.current());

            DbLogApi.addLog(logger_name, Level.INFO, tag, tag1, null, null, summary, content, from);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.print("INFO::" + tag + "::\r\n");
        System.out.print(content);
        System.out.print("\r\n");
    }

    public static void debug(String tag, String summary , String txt) {
        try {
            String from = IPUtil.getIP(XContext.current());

            DbLogApi.addLog(logger_name, Level.DEBUG, tag, null, null, null, summary, txt, from);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.print("DEBUG::" + tag + "::\r\n");
        System.out.print(txt);
        System.out.print("\r\n");
    }

    public static void error(XContext context, Exception ex) {
        String tag = context.path();
        String content = ThrowableUtils.getString(ex);

        try {
            String from = IPUtil.getIP(XContext.current());
            ONode summary = new ONode().setAll(context.paramMap());

            DbLogApi.addLog(logger_name, Level.ERROR, tag, null, null, null, summary.toJson(), content, from);
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        System.out.print("ERROR::" + tag + "\r\n");
        System.out.print(content);
        System.out.print("\r\n");
    }

    public static void error(String tag, String tag1, String summary, Exception ex) {
        String content = ThrowableUtils.getString(ex);

        try {
            String from = IPUtil.getIP(XContext.current());

            DbLogApi.addLog(logger_name, Level.ERROR, tag, tag1, null,null, summary, content, from);
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        System.out.print("ERROR::" + tag + "\r\n");
        System.out.print(content);
        System.out.print("\r\n");
    }

}
