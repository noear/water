package waterapi;

import org.noear.solon.Solon;
import org.noear.water.WW;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.solution.*;
import org.noear.water.track.TrackBuffer;
import waterapi.dso.CacheUtils;
import waterapi.dso.AppInitPlugin;
import waterapi.dso.db.DbWaterCfgApi;
import waterapi.utils.PreheatUtils;

public class WaterapiApp {
	public static void main(String[] args) {
		Solon.start(WaterapiApp.class, args, x -> {
			x.enableStaticfiles(false);

			//加载环境变量(支持弹性容器设置的环境)
			x.cfg().loadEnv("water.");

			//设置接口
			//
			Config.tryInit();
			TrackBuffer.singleton().bind(Config.rd_track);

			ProtocolHub.config = DbWaterCfgApi::getConfigM;

			ProtocolHub.logSourceFactory = new LogSourceFactoryImpl(Config.water_log_store, DbWaterCfgApi::getLogger);

			ProtocolHub.msgBrokerFactory = new MsgBrokerFactoryImpl(Config.water_msg_store, CacheUtils.data, DbWaterCfgApi::getBroker);

			ProtocolHub.heihei = new HeiheiAgentImp(Config.water_heihei); //new WaterLoggerLocal()

			//尝试注册服务
			Config.tryRegService();

			x.pluginAdd(-1, new AppInitPlugin());
		});


		PreheatUtils.preheat(WW.path_run_check);
		PreheatUtils.preheat("/cfg/get/?tag=water");
	}
}
