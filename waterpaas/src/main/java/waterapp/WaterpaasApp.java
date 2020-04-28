package waterapp;

import org.noear.solon.XApp;
import org.noear.solon.core.XMethod;
import org.noear.solonjt.dso.*;
import solonjt.JtRun;
import waterapp.controller.AppHandler;
import waterapp.controller.FrmInterceptor;


public class WaterpaasApp {
    public static void main(String[] args) {
        JtRun.init();

        XApp app = XApp.start(WaterpaasApp.class, args, (x) -> {
            Config.tryInit();

            x.sharedAdd("cache",Config.cache_data);
            x.sharedAdd("XFun", JtFun.g);
            x.sharedAdd("XMsg", JtMsg.g);
            x.sharedAdd("XUtil", JtUtil.g);
            x.sharedAdd("XLock", JtLock.g);
        });

        app.before("**", XMethod.GET, FrmInterceptor.g());
        app.before("**", XMethod.POST, FrmInterceptor.g());

        //文件代理
        app.all("**", AppHandler.g());

        JtRun.xfunInit();

    }
}
