package org.noear.water.protocol.solution;

import org.noear.water.log.Level;
import org.noear.water.protocol.LogSource;
import org.noear.water.protocol.model.LogModel;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;

import java.util.ArrayList;
import java.util.List;

public class LogSourceDb implements LogSource {
    DbContext _db;

    public LogSourceDb(DbContext db) {
        _db = db;
    }

    @Override
    public List<LogModel> query(String logger, Integer level, int size, String tag, String tag1, String tag2, String tag3, Integer log_date, Long log_id) throws Exception {
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

        return _db.table(logger)
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

    @Override
    public void write(long log_id, String logger, Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content, String from) {
        try {
            _db.table(logger).usingExpr(true)
                    .set("log_id", log_id)
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
