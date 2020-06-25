package watersev.utils;

import org.noear.solonjt.executor.ExecutorFactory;
import org.noear.water.utils.EncryptUtils;
import watersev.dso.LogUtil;

public class JsUtil {
    private static final String language = "javascript";

    public synchronized static void loadJs(String code) {
        try {
            ExecutorFactory.exec(language, code, null);
        } catch (Exception ex) {
            ex.printStackTrace();

            String md5 = EncryptUtils.md5(code);
            LogUtil.error("JsUtil", null,"loadJs::" + md5, ex);
            LogUtil.error("JsUtil", null,"loadJs::" + md5, code);
        }
    }

    public static Object evalJs(String code) {
        try {
            return ExecutorFactory.exec(language, code, null);
        } catch (Exception ex) {
            ex.printStackTrace();

            String md5 = EncryptUtils.md5(code);
            LogUtil.error("JsUtil", null,"evalJs::" + md5, ex);
            LogUtil.error("JsUtil", null,"evalJs::" + md5, code);
            return null;
        }
    }
}
