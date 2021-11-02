package watersetup.dso;

import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.Props;
import watersetup.Config;

/**
 * @author noear 2021/10/31 created
 */
public class InitPlugin implements Plugin {
    @Override
    public void start(SolonApp app) {
        Props prop = app.cfg().getProp("water.dataSource");
        if (prop.size() == 0) {
            prop = app.cfg().getProp("water.ds");
        }

        Config.tryInit(prop);
    }
}
