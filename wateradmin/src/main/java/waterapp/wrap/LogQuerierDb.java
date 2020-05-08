package waterapp.wrap;

import org.noear.water.protocol.ILogQuerier;
import org.noear.water.protocol.model.LogModel;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import waterapp.Config;
import waterapp.dso.db.DbWaterCfgApi;
import waterapp.models.water_cfg.LoggerModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogQuerierDb implements ILogQuerier {
    private static ILogQuerier _singleton = null;

    public static ILogQuerier singleton() {
        if (_singleton == null) {
            _singleton = new LogQuerierDb();
        }

        return _singleton;
    }


    private DbContext dbDef() {
        return Config.water_log;
    }

    private static String _lock = "";
    private Map<String, DbContext> _dbMap = new HashMap<>();

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
    public List<LogModel> list(String logger, Integer level, int size, String tag, String tag1, String tag2, String tag3, Integer log_date, Long log_id) throws Exception {
        if (TextUtils.isEmpty(logger)) {
            return new ArrayList<>();
        }

        if (log_date == null) {
            log_date = 0;
        }

        if (log_id == null) {
            log_id = 0L;
        }

        if (level == null) {
            level = 0;
        }

        DbContext db = loggerSource(logger);

        return db.table(logger)
                .where("1 = 1")
                .andIf(TextUtils.isNotEmpty(tag), "tag = ?", tag)
                .andIf(TextUtils.isNotEmpty(tag1), "tag1 = ?", tag1)
                .andIf(TextUtils.isNotEmpty(tag2), "tag2 = ?", tag2)
                .andIf(TextUtils.isNotEmpty(tag3), "tag3 = ?", tag3)
                .andIf(log_date > 0, "log_date = ?", log_date)
                .andIf(log_id > 0, "log_id <= ?", log_id)
                .andIf(level > 0, "level=?", level)
                .orderBy("log_id desc")
                .limit(size)
                .select("*")
                .getList(LogModel.class);
    }
}
