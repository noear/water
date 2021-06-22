package wateradmin.dso;

import org.noear.solon.Solon;
import org.noear.water.WaterClient;

/**
 * @author noear 2021/6/22 created
 */
public class NoticeUtils {
    public static void updateConfig(String tag, String name) {
        if (Solon.cfg().isSetupMode()) {
            return;
        }

        WaterClient.Notice.updateConfig(tag, name);
    }

    public static void updateCache(String... cacheTags) {
        if (Solon.cfg().isSetupMode()) {
            return;
        }

        WaterClient.Notice.updateCache(cacheTags);
    }

    public static String heihei(String target, String msg) {
        return WaterClient.Notice.heihei(target, msg);
    }
}
