package watersev.dso;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.cloud.model.Instance;
import org.noear.water.WaterClient;
import org.noear.water.utils.LockUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author noear 2021/11/18 created
 */
@Slf4j
public class RegUtil {
    static Set<String> subServiceSet = new HashSet<>();

    public static void register(String subService) {
        subServiceSet.add(subService);
    }

    public static void checkin() {
        for (String subService : subServiceSet) {
            checkinOne(subService);
        }
    }

    private static void checkinOne(String subService) {
        String lockName = "sev:" + subService + ":" + Instance.local().address();
        if (LockUtils.tryLock("watersev", lockName, 4) == false) {
            return;
        }

        try {
            boolean is_unstable = Solon.cfg().isDriftMode();
            WaterClient.Registry.register(Solon.cfg().appGroup(),
                    subService,
                    Instance.local().address(),
                    "", "", 1, "", is_unstable);
        } catch (Exception ex) {
            log.error("{}", ex);
        }
    }
}
