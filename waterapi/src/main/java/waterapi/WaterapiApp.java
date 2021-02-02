package waterapi;

import org.noear.solon.Solon;
import org.noear.solon.core.NvMap;
import org.noear.solon.core.handle.Context;
import org.noear.solon.extend.staticfiles.StaticFiles;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.solution.HeiheiImp;
import org.noear.water.protocol.solution.LogSourceFactoryImp;
import org.noear.water.protocol.solution.LogStorerImp;
import org.noear.water.protocol.solution.MessageLockRedis;
import org.noear.water.track.TrackBuffer;
import waterapi.dso.IDUtils;
import waterapi.dso.LogUtils;
import waterapi.dso.WaterLoggerLocal;
import waterapi.dso.db.DbWaterCfgApi;

public class WaterapiApp {

	public static void main(String[] args) {
		NvMap argx = NvMap.from(args);

		if (argx.containsKey("setup")) {
			//启动安装界面
			//
			System.out.println("setup mode ...");
			StaticFiles.instance().put(".sql", "text/sql");

			Solon.start(WaterapiApp.class, argx, app -> {

			});
		} else {
			//关掉静态文件支持//启动运行界面
			//
			//System.setProperty("org.noear.solon.extend.staticfiles.enabled", "0");

			Solon.start(WaterapiApp.class, argx, app -> {
				app.enableStaticfiles(false);

				Config.tryInit();

				ProtocolHub.config = DbWaterCfgApi::getConfigM;

				ProtocolHub.logSourceFactory = new LogSourceFactoryImp(Config.water_log_store, DbWaterCfgApi::getLogger);
				ProtocolHub.logStorer = new LogStorerImp(IDUtils::buildLogID);
				ProtocolHub.messageLock = new MessageLockRedis(Config.rd_lock);
				ProtocolHub.messageQueue = ProtocolHub.getMessageQueue(Config.water_msg_queue);
				ProtocolHub.heihei = new HeiheiImp(new WaterLoggerLocal());

				TrackBuffer.singleton().bind(Config.rd_track);
			}).onError(err -> {
				Context ctx = Context.current();
				if (ctx == null) {
					LogUtils.error("global", "", "", err);
				} else {
					LogUtils.error(ctx, err);
				}
			});
		}
	}
}
