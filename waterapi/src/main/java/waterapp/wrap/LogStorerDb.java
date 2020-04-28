package waterapp.wrap;

import org.noear.water.log.Level;
import org.noear.water.protocol.ILogStorer;
import org.noear.weed.DbContext;
import waterapp.Config;
import waterapp.dso.IDUtil;
import waterapp.dso.db.DbWaterCfgApi;
import waterapp.models.LoggerModel;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LogStorerDb implements ILogStorer {
    private static ILogStorer _singleton = null;
    public static ILogStorer singleton() {
        if (_singleton == null) {
            _singleton = new LogStorerDb();
        }

        return _singleton;
    }



    private DbContext db() {
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
                    db = DbWaterCfgApi.getDbContext(cfg.source, db());
                    _dbMap.put(logger, db);
                }
            }
        }

        return db;
    }

    @Override
    public void append(String logger, Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content, String from) {
        try {
            DbContext db = loggerSource(logger);

            db.table(logger).usingExpr(true)
                    .set("log_id", IDUtil.buildLogID())
                    .set("level", level.code)
                    .setDf("tag", tag, "")
                    .setDf("tag1", tag1, "")
                    .setDf("tag2", tag2, "")
                    .setDf("tag3", tag3, "")
                    .setDf("summary", summary, "")
                    .setDf("content", content, "")
                    .setDf("from", from, "")
                    .set("log_date", "$DATE(NOW())")
                    .set("log_fulltime", "$NOW()")
                    .insert();

        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
