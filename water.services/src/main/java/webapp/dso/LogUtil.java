package webapp.dso;

import org.noear.snack.ONode;
import org.noear.solon.core.XContext;
import webapp.dso.db.DbLogApi;
import webapp.utils.IPUtil;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

public class LogUtil {
    private static final String logger_debug = "water_log_api_debug";
    private static final String logger_error = "water_log_api_error";
    private static final String logger_api = "water_log_api";


    public static void write(String label, XContext context) {
        try {
            String tag = context.path();

            if (tag == null) {
                return;
            }

            String ip = IPUtil.getIP(context);

            Map<String,String> pnames = context.paramMap();

            ONode args = new ONode();
            if(pnames != null) {
                pnames.forEach((k, v) -> {
                    args.set(k, v);
                });
            }

            DbLogApi.addLog(logger_api, tag, ip, "", label, args.toJson());
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public static void write(String tag, String label, String content) {
        doWrite(tag, null, label, content);
    }

    public static void doWrite(String tag, String tag1, String label, String content) {
        try {
            DbLogApi.addLog(logger_api, tag, tag1, "", label, content);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.print(tag + "::\r\n");
        System.out.print(content);
        System.out.print("\r\n");
    }

    public static void debug(String tag, String label , String txt) {
        StringBuilder sb = new StringBuilder();
        sb.append(txt);

        try {
            DbLogApi.addLog(logger_debug, tag, "", "", label, sb.toString());
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

            DbLogApi.addLog(logger_error, tag, "", "", label.toJson(), content);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public static void error(String tag, String tag1, String label, Exception ex) {
        try {
            String content = getFullStackTrace(ex);

            DbLogApi.addLog(logger_error, tag, tag1, "", label, content);
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
