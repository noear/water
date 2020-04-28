package waterapp;

import org.noear.solon.XApp;
import org.noear.solon.XUtil;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.DefaultHeihei;
import waterapp.wrap.LogStorerDb;
import waterapp.wrap.MessageQueueRedis;

public class WaterapiApp {

	public static void main(String[] args) {
		 XApp.start(WaterapiApp.class, args, app->{
			 Config.tryInit(app.port(), app.prop().getProp("water.dataSource"));

			 ProtocolHub.logStorer = LogStorerDb.singleton();
			 ProtocolHub.messageQueue = MessageQueueRedis.singleton();
			 ProtocolHub.heihei = DefaultHeihei.singleton();
		 });
	}
}
