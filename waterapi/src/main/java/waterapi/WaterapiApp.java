package waterapi;

import org.noear.solon.SolonBuilder;
import org.noear.solon.core.handle.Context;
import org.noear.water.WW;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.solution.*;
import org.noear.water.track.TrackBuffer;
import waterapi.dso.CacheUtils;
import waterapi.dso.LogUtils;
import waterapi.dso.db.DbWaterCfgApi;
import waterapi.utils.PreheatUtils;

public class WaterapiApp {
	public static void main(String[] args) {
		new SolonBuilder()
				.onError(err -> {
					Context ctx = Context.current();

					if (ctx == null) {
						LogUtils.error(ctx, "global", "", "", err);
					} else {
						LogUtils.error(ctx, err);
					}
				})
				.start(WaterapiApp.class, args, x -> {
					//加载环境变量(支持弹性容器设置的环境)
					x.cfg().loadEnv("water.");

					x.enableStaticfiles(false);

					//尝试初始化
					Config.tryInit();

					TrackBuffer.singleton().bind(Config.rd_track);

					ProtocolHub.config = DbWaterCfgApi::getConfigM;

					ProtocolHub.logSourceFactory = new LogSourceFactoryImp(Config.water_log_store, DbWaterCfgApi::getLogger);

					ProtocolHub.messageSourceFactory = new MessageSourceFactoryImp(Config.water_msg_store, CacheUtils.data);
					ProtocolHub.messageQueue = ProtocolHub.getMessageQueue(Config.water_msg_queue);
					ProtocolHub.heihei = new HeiheiImp(); //new WaterLoggerLocal()

					//尝试注册服务
					Config.tryRegService();
				});


		PreheatUtils.preheat("/run/check/");
		PreheatUtils.preheat("/cfg/get/?tag=water");
	}
}
