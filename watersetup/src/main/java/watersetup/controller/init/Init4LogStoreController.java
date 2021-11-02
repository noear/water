package watersetup.controller.init;

import org.noear.redisx.RedisClient;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Post;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.handle.Result;
import org.noear.water.WW;
import org.noear.water.model.ConfigM;
import org.noear.water.protocol.LogSource;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.solution.LogSourceFactoryImpl;
import org.noear.weed.DbContext;
import watersetup.Config;
import watersetup.dso.InitUtils;
import watersetup.dso.db.DbWaterCfgApi;
import watersetup.models.water_cfg.LoggerModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * @author noear 2021/11/2 created
 */
@Controller
public class Init4LogStoreController {

    @Post
    @Mapping("/ajax/init/water_log")
    public Result ajax_connect(String config) throws Exception {
        if (Config.water == null) {
            return Result.failure("未连接数据库，刷新再试...");
        }

        if (Utils.isEmpty(config)) {
            return Result.failure("配置不能为空");
        }

        ProtocolHub.config = Config::getCfg;

        ConfigM logCfg = new ConfigM("water_log_store", config, 0);
        ProtocolHub.logSourceFactory = new LogSourceFactoryImpl(logCfg, DbWaterCfgApi::getLogger);


        List<LoggerModel> loggerList = DbWaterCfgApi.getLoggerList();

        for (LoggerModel logger : loggerList) {
            if (Utils.isEmpty(logger.source)) {
                ProtocolHub.logSourceFactory
                        .getSource(logger.logger)
                        .create(logger.logger);
            }
        }

        //更新配置
        DbWaterCfgApi.updConfig(WW.water, WW.water_log_store, config);
        DbWaterCfgApi.updConfig(WW.water, Config.water_setup_step, "4");

        //2.
        return Result.succeed(null, "配置成功");
    }
}
