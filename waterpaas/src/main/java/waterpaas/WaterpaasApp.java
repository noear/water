package waterpaas;

import org.noear.solon.XApp;
import org.noear.solon.core.XMethod;
import org.noear.luffy.dso.*;
import org.noear.water.WaterClient;
import org.noear.water.solon_plugin.FromUtils;
import org.noear.water.solon_plugin.XWaterAdapter;
import org.noear.water.utils.Timecount;
import luffy.JtRun;
import waterpaas.controller.AppHandler;
import waterpaas.controller.FrmInterceptor;


public class WaterpaasApp {
    public static void main(String[] args) {
        JtRun.init();

        XApp app = XApp.start(WaterpaasApp.class, args, (x) -> {
            Config.tryInit(x);

            x.sharedAdd("cache", Config.cache_data);
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


        //添加性能记录
        app.before("**", XMethod.HTTP, -1, (c) -> {
            //
            //不记录，检测的性能
            //
            if ("/run/check/".equals(c.path()) == false) {
                c.attrSet("timecount", new Timecount().start());
            }
        });
        app.after("**", XMethod.HTTP, (c) -> {
            Timecount timecount = c.attr("timecount", null);

            if (timecount != null && c.status() != 404) {
                String tag = c.attr("file_tag", "paas");

                long _times = timecount.stop().milliseconds();
                String _node = XWaterAdapter.global().localHost();
                String _from = FromUtils.getFrom(c);

                WaterClient.Track.track(XWaterAdapter.global().service_name(), tag, c.path(), _times, _node, _from);
            }
        });
    }
}