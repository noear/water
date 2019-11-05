package webapp;

import org.noear.solon.XApp;

public class App {
    public static void main(String[] args) {
        XApp.start(App.class, args,(app)->{
            Config.tryInit(
                    app.port(),
                    app.prop().getProp("water.dataSource"));
        });

    }
}
