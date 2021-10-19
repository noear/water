package waterraas.dao;

import org.noear.solon.Utils;
import org.noear.water.WaterClient;
import org.noear.water.log.Level;

public final class LogUtil {
    public static void logBlockError(String block, String args_str, Exception ex) {
        WaterClient.Log.append("water_log_raas", Level.ERROR,
                "d", block, args_str,
                Utils.throwableToString(ex));
    }

    public static void logModelError(String model, String args_str, Exception ex) {
        WaterClient.Log.append("water_log_raas", Level.ERROR,
                "m", model, args_str,
                Utils.throwableToString(ex));
    }

    public static void logSchemeError(String scheme, String args_str, Exception ex) {
        WaterClient.Log.append("water_log_raas", Level.ERROR,
                "s", scheme, args_str,
                Utils.throwableToString(ex));
    }

    public static void logSchemeCallbackError(String scheme, String args_str, String note) {
        if (note == null) {
            note = "null";
        }

        WaterClient.Log.append("water_log_raas", Level.ERROR,
                "s", scheme, "callback", args_str, note);
    }
}
