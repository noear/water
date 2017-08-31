package waterapi.dao;

import noear.snacks.ONode;
import waterapi.dao.db.DbLogApi;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Enumeration;

/**
 * Created by yuety on 2017/7/27.
 */
public class LogUtil {
    private static final String logger = "water_log_api_error";

    public static void error(HttpServletRequest request, Exception ex) {
        try {
            Enumeration<String> pnames = request.getParameterNames();
            String tag = request.getPathInfo();

            String content = getFullStackTrace(ex);
            ONode label = new ONode();

            while (pnames.hasMoreElements()) {
                String name = pnames.nextElement();
                label.set(name, request.getParameter(name));
            }

            DbLogApi.addLog(logger, tag, 0l, 0l, label.toString(), content);
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
