package webapp;

import org.noear.solon.XApp;
import org.noear.solon.core.XMethod;
import org.noear.water.WaterClient;
import org.noear.water.solon_plugin.XWaterAdapter;
import org.noear.water.utils.Timecount;

public class DemoApp {
    public static void main(String[] args) {
        XApp app = XApp.start(DemoApp.class, args);



        //监控服务：之：添加接口性能记录
        app.before("**", XMethod.HTTP,-1,(c)->{
            c.attrSet("_timecount", new Timecount().start());
        });
        app.after("**", XMethod.HTTP,(c)->{
            Timecount timecount = c.attr("_timecount", null);

            if (timecount == null || c.status() == 404) {
                return;
            }

            String node = XWaterAdapter.global().localHost();
            long times = timecount.stop().milliseconds();

            WaterClient.Track.track("water-demo", "path", c.path(), times, node);
        });
    }
}
