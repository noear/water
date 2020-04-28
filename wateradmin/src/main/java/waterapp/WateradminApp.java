package waterapp;

import org.noear.solon.XApp;
import org.noear.water.WaterClient;
import org.noear.water.protocol.ProtocolHub;
import waterapp.wrap.LogQuerierDb;

public class WateradminApp {
    public static void main(String[] args) {
        //
        // http://139.224.74.31:9371/cfg/get/?tag=water
        //
        XApp.start(WateradminApp.class, args, app -> {
            Config.tryInit();


            //设置接口
            //
            ProtocolHub.logQuerier = LogQuerierDb.singleton();
        });
    }
}