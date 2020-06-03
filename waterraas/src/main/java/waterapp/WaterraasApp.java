package waterapp;

import org.noear.rubber.Rubber;
import org.noear.solon.XApp;
import org.noear.solon.core.XMethod;
import org.noear.solonjt.dso.*;
import org.noear.water.WaterClient;
import org.noear.water.solon_plugin.XWaterAdapter;
import org.noear.water.utils.Timecount;
import solonjt.JtRun;
import waterapp.controller.DebugController;
import waterapp.controller.PreviewController;
import waterapp.controller.ReleaseController;
import waterapp.controller.debug.BlockController;
import waterapp.controller.debug.QueryController;
import waterapp.controller.release.ModelController;
import waterapp.controller.release.SchemeController;
import waterapp.dao.CacheUtil;

import java.util.Collections;


public class WaterraasApp {

    public static void main(String[] args) {
        JtRun.init();

        XApp.start(WaterraasApp.class, args, (x) -> {
            Config.tryInit();

            x.sharedAdd("cache",Config.cache_data);
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

            JtRun.xfunInit();
        });


        try {
            Rubber.tryInit(CacheUtil.data);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
