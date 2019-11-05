package webapp;

import org.noear.solon.XApp;
import org.noear.solon.core.Aop;

public class App {
    public static void main(String[] args) {
        XApp app = XApp.start(App.class, args);
        Config.tryInit(app.port(), Aop.prop().getProp("water.dataSource"));
    }
}
