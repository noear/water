package watersetup.dso;

import org.noear.solon.SolonApp;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.Props;
import org.noear.weed.DbContext;
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

        DbContext db = Config.getDb(prop);

        if (db != null) {
            Config.water = db;
        }
    }
}
