package waterapp;

import org.noear.solon.XApp;
import org.noear.solon.core.XMap;
import org.noear.solon.extend.staticfiles.XStaticFiles;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.solution.HeiheiDefault;
import waterapp.wrap.LogStorerDb;
import waterapp.wrap.MessageQueueRedis;

public class WaterapiApp {

	public static void main(String[] args) {
		XMap argx = XMap.from(args);

		if (argx.containsKey("setup")) {
			//启动安装界面
			//
			System.out.println("setup mode ...");
			XStaticFiles.instance().put(".sql", "text/sql");

			XApp.start(WaterapiApp.class, argx, app -> {

			});
		} else {
			//关掉静态文件支持//启动运行界面
			//
			System.setProperty("org.noear.solon.extend.staticfiles.enabled","0");

			XApp.start(WaterapiApp.class, argx, app -> {
				Config.tryInit(app.port(), app.prop().getProp("water.dataSource"));

				ProtocolHub.logStorer = LogStorerDb.singleton();
				ProtocolHub.messageQueue = MessageQueueRedis.singleton();
				ProtocolHub.heihei = HeiheiDefault.singleton();
			});
		}

	}
}
