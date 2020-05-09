package waterapp;

import org.noear.solon.XApp;
import org.noear.solon.core.XMap;
import org.noear.solon.extend.staticfiles.XStaticFiles;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.solution.HeiheiDefault;
import org.noear.water.protocol.solution.LogStorerDb;
import org.noear.water.protocol.solution.MessageQueueRedis;
import waterapp.dso.IDUtil;
import waterapp.dso.LogSourceWrap;

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
			System.setProperty("org.noear.solon.extend.staticfiles.enabled", "0");

			XApp.start(WaterapiApp.class, argx, app -> {
				Config.tryInit(app.port(), app.prop().getProp("water.dataSource"));

				ProtocolHub.logStorer = new LogStorerDb(new LogSourceWrap(), () -> IDUtil.buildLogID());
				ProtocolHub.messageQueue = new MessageQueueRedis(Config.water_msg_queue, Config.rd_msg);
				ProtocolHub.heihei = HeiheiDefault.singleton();
			});
		}
	}
}
