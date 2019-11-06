package webapp;

import org.noear.solon.XApp;

public class App {
    public static void main(String[] args) {

        //
        // http://106.15.45.208:9371/cfg/?tag=water
        //
        System.setProperty("water.host", "http://106.15.45.208:9371");

        XApp.start(App.class, args, (app) -> {
            app.sharedAdd("db",Config.water);
        });
    }
}
