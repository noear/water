package waterraas;

import org.noear.rubber.Rubber;
import org.noear.solon.Solon;
import org.noear.luffy.dso.*;
import luffy.JtRun;
import org.noear.solon.SolonApp;
import waterraas.controller.DebugController;
import waterraas.controller.PreviewController;
import waterraas.controller.ReleaseController;
import waterraas.controller.debug.BlockController;
import waterraas.controller.debug.QueryController;
import waterraas.controller.release.ModelController;
import waterraas.controller.release.SchemeController;
import waterraas.dao.CacheUtil;


public class WaterraasApp {

    public static void main(String[] args) {
        JtRun.init();

        SolonApp app = Solon.start(WaterraasApp.class, args, (x) -> {
            Config.tryInit();

            x.sharedAdd("cache",Config.cache_data);
            x.sharedAdd("XFun", JtFun.g);
            x.sharedAdd("XMsg", JtMsg.g);
            x.sharedAdd("XUtil", JtUtil.g);
            x.sharedAdd("XLock", JtLock.g);
        });

        app.all("/debug", new DebugController());
        app.all("/release", new ReleaseController());
        app.get("/preview(.js)?", new PreviewController());

        app.all("/s/*/*", new SchemeController());
        app.all("/m/*/*", new ModelController());
        app.all("/q/*/*", new QueryController());
        app.all("/d/*/*", new BlockController());

        JtRun.xfunInit();

        try {
            Rubber.tryInit(CacheUtil.data);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
