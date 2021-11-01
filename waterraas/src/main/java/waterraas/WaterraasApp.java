package waterraas;

import org.noear.rubber.Rubber;
import org.noear.solon.Solon;
import org.noear.luffy.dso.*;
import luffy.JtRun;
import org.noear.solon.cloud.utils.http.PreheatUtils;
import waterraas.controller.DebugController;
import waterraas.controller.PreviewController;
import waterraas.controller.ReleaseController;
import waterraas.controller.debug.BlockController;
import waterraas.controller.debug.QueryController;
import waterraas.controller.release.ModelController;
import waterraas.controller.release.SchemeController;
import waterraas.dso.CacheUtil;


public class WaterraasApp {

    public static void main(String[] args) {
        JtRun.init();

        Solon.start(WaterraasApp.class, args, (x) -> {
            Config.tryInit();

            x.enableErrorAutoprint(false);

            x.sharedAdd("cache", Config.cache_data);
            x.sharedAdd("XFun", JtFun.g);
            x.sharedAdd("XMsg", JtMsg.g);
            x.sharedAdd("XUtil", JtUtil.g);
            x.sharedAdd("XLock", JtLock.g);


            x.all("/debug", new DebugController());
            x.all("/release", new ReleaseController());
            x.get("/preview(.js)?", new PreviewController());

            x.all("/s/*/*", new SchemeController());
            x.all("/m/*/*", new ModelController());
            x.all("/q/*/*", new QueryController());
            x.all("/d/*/*", new BlockController());
        });

        JtRun.xfunInit();

        try {
            Rubber.tryInit(CacheUtil.data);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        PreheatUtils.preheat("/run/check/");
    }
}
