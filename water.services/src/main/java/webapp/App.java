package webapp;

import org.noear.solon.XApp;

public class App {
    public static void main(String[] args) {
        XApp app = XApp.start(App.class, args);
        Config.tryInit(app.port());
    }
}
