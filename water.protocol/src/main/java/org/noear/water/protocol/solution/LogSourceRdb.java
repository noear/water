package org.noear.water.protocol.solution;

import org.noear.water.log.Level;
import org.noear.water.log.LogEvent;
import org.noear.water.protocol.LogSource;
import org.noear.water.protocol.model.LogModel;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogSourceRdb implements LogSource {
    DbContext _db;

    public LogSourceRdb(DbContext db) {
        _db = db;
    }

    @Override
    public List<LogModel> query(String logger, String trace_id, Integer level, int size, String tag, String tag1, String tag2, String tag3, Integer log_date, Long log_id) throws Exception {
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
                .andIf(TextUtils.isNotEmpty(trace_id), "trace_id = ?", trace_id)
                .andIf(TextUtils.isNotEmpty(tag), "tag = ?", tag)
                .andIf(TextUtils.isNotEmpty(tag1), "tag1 = ?", tag1)
                .andIf(TextUtils.isNotEmpty(tag2), "tag2 = ?", tag2)
                .andIf(TextUtils.isNotEmpty(tag3), "tag3 = ?", tag3)
                .andIf(log_date > 0, "log_date = ?", log_date)
                .andIf(log_id > 0, "log_id <= ?", log_id)
                .andIf(level > 0, "level=?", level)
                .orderBy("log_fulltime desc, log_id desc")
                .limit(size)
                .select("*")
                .getList(LogModel.class);
    }

    @Override
    public void write(long log_id, String logger, String trace_id, Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content, String from, Date log_fulltime) throws Exception{

        DbTableQuery qr = _db.table(logger).usingExpr(true)
                .set("log_id", log_id)
                .set("trace_id", trace_id)
                .set("level", level.code)
                .setDf("tag", tag, "")
                .setDf("tag1", tag1, "")
                .setDf("tag2", tag2, "")
                .setDf("tag3", tag3, "")
                .setDf("summary", summary, "")
                .setDf("content", content, "")
                .setDf("from", from, "");

        if (log_fulltime == null) {
            qr.set("log_date", "$DATE(NOW())")
                    .set("log_fulltime", "$NOW()")
                    .insert();
        } else {
            qr.set("log_date", new Datetime(log_fulltime).getDate())
                    .set("log_fulltime", log_fulltime)
                    .insert();
        }
    }

    @Override
    public void writeAll(String logger, List<LogEvent> list) throws Exception {
        if (list.size() == 0) {
            return;
        }

        _db.table(logger).insertList(list, (log, item) -> {
            item.set("log_id", log.log_id)
                    .set("trace_id", log.trace_id)
                    .set("level", log.level)
                    .setDf("tag", log.tag, "")
                    .setDf("tag1", log.tag1, "")
                    .setDf("tag2", log.tag2, "")
                    .setDf("tag3", log.tag3, "")
                    .setDf("summary", log.summary, "")
                    .setDf("content", log.content, "")
                    .setDf("from", log.from, "")
                    .setDf("log_date", log.log_date, "$DATE(NOW())")
                    .setDf("log_fulltime", log.log_fulltime, "$NOW()");
        });
    }

    @Override
    public long stat(String logger, Integer level, Integer log_date) throws Exception {
        return _db.table(logger)
                .where("1=1")
                .andIf(level != null, "level=?", level)
                .andIf(log_date != null, "log_date=?", log_date)
                .count();
    }

    @Override
    public void clear(String logger, int keep_days) {
        int date = Datetime.Now().addDay(- keep_days).getDate();
        try {
            _db.table(logger).where("log_date <= ?", date).delete();
        }catch (Throwable ex){
            ex.printStackTrace();
        }
    }
}
