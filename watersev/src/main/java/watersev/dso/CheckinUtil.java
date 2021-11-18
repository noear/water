package watersev.dso;

import org.noear.solon.Solon;
import org.noear.water.WaterClient;
import org.noear.water.utils.LocalUtils;
import org.noear.water.utils.LockUtils;

/**
 * @author noear 2021/11/18 created
 */
public class CheckinUtil {
    public static void checkin(String subService) {
        String lockName = "watersev-sev-" + subService;
        if (LockUtils.tryLock("watersev", lockName, 5) == false) {
            return;
        }

        try {
            boolean is_unstable = Solon.cfg().isDriftMode();
            WaterClient.Registry.register(Solon.cfg().appGroup(),
                    subService,
                    LocalUtils.getLocalIp(),
                    "", "", 1, "", is_unstable);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
