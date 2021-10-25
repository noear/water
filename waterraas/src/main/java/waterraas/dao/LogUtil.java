package waterraas.dao;

import org.noear.solon.logging.utils.TagsMDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LogUtil {
    static Logger log = LoggerFactory.getLogger("water_log_raas");

    public static void logBlockError(String block, String args_str, Exception ex) {

        TagsMDC.tag0("d");
        TagsMDC.tag1(block);

        log.error("{}\r\n{}", args_str, ex);

//        WaterClient.Log.append("water_log_raas", Level.ERROR,
//                "d", block, args_str,
//                Utils.throwableToString(ex));
    }

    public static void logModelError(String model, String args_str, Exception ex) {
        TagsMDC.tag0("m");
        TagsMDC.tag1(model);

        log.error("{}\r\n{}", args_str, ex);

//        WaterClient.Log.append("water_log_raas", Level.ERROR,
//                "m", model, args_str,
//                Utils.throwableToString(ex));
    }

    public static void logSchemeError(String scheme, String args_str, Exception ex) {
        TagsMDC.tag0("s");
        TagsMDC.tag1(scheme);

        log.error("{}\r\n{}", args_str, ex);

//        WaterClient.Log.append("water_log_raas", Level.ERROR,
//                "s", scheme, args_str,
//                Utils.throwableToString(ex));
    }

    public static void logSchemeCallbackError(String scheme, String args_str, String note) {
        if (note == null) {
            note = "null";
        }

        TagsMDC.tag0("s");
        TagsMDC.tag1(scheme);
        TagsMDC.tag2("callback");

        log.error("{}\r\n{}", args_str, note);

//        WaterClient.Log.append("water_log_raas", Level.ERROR,
//                "s", scheme, "callback", args_str, note);
    }
}
