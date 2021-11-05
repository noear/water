package waterapi;

import org.noear.solon.Solon;
import org.noear.solon.cloud.CloudManager;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.solution.*;
import org.noear.water.track.TrackBuffer;
import waterapi.dso.CacheUtils;
import waterapi.dso.MsgInitPlugin;
import waterapi.dso.db.DbWaterCfgApi;
import waterapi.dso.log.CloudLogServiceLocalImp;
import waterapi.utils.PreheatUtils;

public class WaterapiApp {
	public static void main(String[] args) {
		Solon.start(WaterapiApp.class, args, x -> {
			//注册本地日志服务
			CloudManager.register(new CloudLogServiceLocalImp());

			//加载环境变量(支持弹性容器设置的环境)
			x.cfg().loadEnv("water.");

			x.enableStaticfiles(false);
			x.enableErrorAutoprint(false);


			//尝试初始化
			Config.tryInit();


			TrackBuffer.singleton().bind(Config.rd_track);

			ProtocolHub.config = DbWaterCfgApi::getConfigM;

			ProtocolHub.logSourceFactory = new LogSourceFactoryImpl(Config.water_log_store, DbWaterCfgApi::getLogger);

			ProtocolHub.msgBrokerFactory = new MsgBrokerFactoryImpl(Config.water_msg_store, CacheUtils.data, DbWaterCfgApi::getBroker);

			ProtocolHub.heihei = new HeiheiImp(); //new WaterLoggerLocal()

			//尝试注册服务
			Config.tryRegService();

			x.pluginAdd(-1, new MsgInitPlugin());
		});


		PreheatUtils.preheat("/run/check/");
		PreheatUtils.preheat("/cfg/get/?tag=water");
	}
}
