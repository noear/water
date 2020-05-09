package waterapp.dso;

import org.noear.water.utils.ext.Fun1Ex;
import org.noear.weed.DbContext;
import waterapp.Config;
import waterapp.dso.db.DbWaterCfgApi;
import waterapp.models.LoggerModel;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LogSourceWrap implements Fun1Ex<String,DbContext> {
    private DbContext dbDef() {
        return Config.water_log;
    }

    private static String _lock="";
    private Map<String,DbContext> _dbMap = new HashMap<>();

    private DbContext loggerSource(String logger) throws SQLException {
        DbContext db = _dbMap.get(logger);

        if (db == null) {
            synchronized (_lock) {
                db = _dbMap.get(logger);

                if (db == null) {
                    LoggerModel cfg = DbWaterCfgApi.getLogger(logger);
                    db = DbWaterCfgApi.getDbContext(cfg.source, dbDef());
                    _dbMap.put(logger, db);
                }
            }
        }

        return db;
    }

    @Override
    public DbContext run(String source) throws Exception{
        return loggerSource(source);
    }
}
