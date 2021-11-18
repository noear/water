package watersev.utils;

import org.noear.luffy.executor.ExecutorFactory;
import org.noear.solon.logging.utils.TagsMDC;
import org.noear.water.WW;
import org.noear.water.utils.EncryptUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import watersev.dso.LogUtil;

public class JsUtil {
    private static final String language = "javascript";
    private static Logger log_sev = LoggerFactory.getLogger(WW.logger_water_log_sev);

    public synchronized static void loadJs(String code) {
        try {
            ExecutorFactory.exec(language, code, null);
        } catch (Exception ex) {
            String md5 = EncryptUtils.md5(code);

            TagsMDC.tag0("JsUtil");
            log_sev.error("{}\n{}", "loadJs::" + md5, ex);
            log_sev.error("{}\n{}", "loadJs::" + md5, code);
        }
    }

    public static Object evalJs(String code) {
        try {
            return ExecutorFactory.exec(language, code, null);
        } catch (Exception ex) {
            String md5 = EncryptUtils.md5(code);

            TagsMDC.tag0("JsUtil");
            log_sev.error("{}\n{}", "evalJs::" + md5, ex);
            log_sev.error("{}\n{}", "evalJs::" + md5, code);
            return null;
        }
    }
}
