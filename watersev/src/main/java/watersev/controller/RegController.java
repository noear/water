package watersev.controller;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.cloud.model.Instance;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.WaterClient;

import java.util.*;

/**
 * 注册子服务
 *
 * @author noear
 */
@Slf4j
@Component
public class RegController implements IJob {
    static final Set<String> subServiceSet = new HashSet<>();

    /**
     * 添加子服务
     */
    public static void addService(String subService) {
        synchronized (subServiceSet) {
            subServiceSet.add(subService);
        }
    }

    @Override
    public String getName() {
        return "reg";
    }

    @Override
    public int getInterval() {
        return 1000 * 5;
    }

    private List<String> sevList = new ArrayList<>();

    @Override
    public void exec() throws Throwable {
        //线程安全处理
        if (sevList.size() != subServiceSet.size()) {
            sevList.clear();
            synchronized (subServiceSet) {
                sevList.addAll(subServiceSet);
            }
        }

        //设置跟踪
        CloudClient.trace().setLocalTraceId(Utils.guid());

        for (String sev : sevList) {
            checkinOne(sev);
        }
    }

    private static void checkinOne(String subService) {
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
