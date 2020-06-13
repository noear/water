package waterapp;

import org.noear.solon.XApp;
import org.noear.solon.core.XMap;
import org.noear.solon.core.XMethod;
import org.noear.solon.extend.staticfiles.XStaticFiles;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.solution.HeiheiDefault;
import org.noear.water.protocol.solution.LogStorerDb;
import org.noear.water.protocol.solution.MessageQueueRedis;
import org.noear.water.utils.Timecount;
import org.noear.water.utils.Timespan;
import waterapp.dso.IDUtils;
import waterapp.dso.LogSourceWrap;
import waterapp.dso.TraceUtils;
import waterapp.dso.WaterLoggerLocal;

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

				ProtocolHub.logStorer = new LogStorerDb(new LogSourceWrap(), () -> IDUtils.buildLogID());
				ProtocolHub.messageQueue = new MessageQueueRedis(Config.water_msg_queue, Config.rd_msg);
				ProtocolHub.heihei = new HeiheiDefault(new WaterLoggerLocal());
			});
		}

		XApp.global().before("**", XMethod.HTTP, c -> {
			c.attrSet("timecount", new Timecount().start());
		});

		XApp.global().after("**", XMethod.HTTP, c -> {
			Timecount timecount = c.attr("timecount", null);

			if (timecount != null) {
				Timespan timespan = timecount.stop();
				TraceUtils.track(Config.water_service_name, "cmd", c.path(), timespan.milliseconds());
			}
		});
	}
}
