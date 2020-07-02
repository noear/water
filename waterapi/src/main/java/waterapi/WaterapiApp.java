package waterapi;

import org.noear.solon.XApp;
import org.noear.solon.core.XMap;
import org.noear.solon.core.XMethod;
import org.noear.solon.extend.staticfiles.XStaticFiles;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.solution.HeiheiImp;
import org.noear.water.protocol.solution.LogSourceFactoryImp;
import org.noear.water.protocol.solution.LogStorerImp;
import org.noear.water.protocol.solution.MessageLockRedis;
import org.noear.water.track.TrackBuffer;
import org.noear.water.utils.Timecount;
import org.noear.water.utils.Timespan;
import org.noear.water.track.TrackUtils;
import waterapi.dso.FromUtils;
import waterapi.dso.IDUtils;
import waterapi.dso.WaterLoggerLocal;
import waterapi.dso.db.DbWaterCfgApi;
import waterapi.dso.wrap.LogSourceDef;

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

				ProtocolHub.config = DbWaterCfgApi::getConfigM;

				ProtocolHub.logSourceFactory = new LogSourceFactoryImp(new LogSourceDef(), DbWaterCfgApi::getLogger);
				ProtocolHub.logStorer = new LogStorerImp(IDUtils::buildLogID);
				ProtocolHub.messageLock = new MessageLockRedis(Config.rd_lock);
				ProtocolHub.messageQueue = ProtocolHub.getMessageQueue(Config.water_msg_queue);
				ProtocolHub.heihei = new HeiheiImp(new WaterLoggerLocal());

				TrackBuffer.singleton().bind(Config.rd_track);
			});
		}

		XApp.global().before("**", XMethod.HTTP, c -> {
			c.attrSet("timecount", new Timecount().start());
		});

		XApp.global().after("**", XMethod.HTTP, c -> {
			Timecount timecount = c.attr("timecount", null);

			if (timecount != null) {
				String _node = Config.getLocalHost();
				String _from = FromUtils.getFrom(c);

				Timespan timespan = timecount.stop();
				TrackBuffer.singleton().append(Config.water_service_name, "cmd", c.path(), timespan.milliseconds(), _node, _from);
			}
		});
	}
}
