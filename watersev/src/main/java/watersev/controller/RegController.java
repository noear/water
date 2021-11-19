package watersev.controller;

import org.noear.solon.annotation.Component;
import org.noear.solon.extend.schedule.IJob;
import watersev.dso.RegUtil;

/**
 * 注册子服务
 *
 * @author noear
 */
@Component
public class RegController implements IJob {
    @Override
    public String getName() {
        return "reg";
    }

    @Override
    public int getInterval() {
        return 1000 * 5;
    }

    @Override
    public void exec() throws Throwable {
        RegUtil.checkin();
    }
}
