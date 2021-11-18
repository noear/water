package watersev.dso;

import org.noear.solon.Solon;
import org.noear.solon.cloud.model.Instance;
import org.noear.water.WaterClient;
import org.noear.water.utils.LockUtils;

/**
 * @author noear 2021/11/18 created
 */
public class RegUtil {
    public static void checkin(String subService) {
        String lockName = "watersev-sev-" + subService;
        if (LockUtils.tryLock("watersev", lockName, 5) == false) {
            return;
        }

        try {
            boolean is_unstable = Solon.cfg().isDriftMode();
            WaterClient.Registry.register(Solon.cfg().appGroup(),
                    subService,
                    Instance.local().address(),
                    "", "", 1, "", is_unstable);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
