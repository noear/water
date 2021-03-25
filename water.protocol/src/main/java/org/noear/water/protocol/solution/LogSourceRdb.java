package org.noear.water.protocol.solution;

import org.noear.water.log.Level;
import org.noear.water.log.LogEvent;
import org.noear.water.protocol.LogSource;
import org.noear.water.protocol.model.log.LogModel;
import org.noear.water.utils.NameUtils;
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
    public List<LogModel> query(String logger, String trace_id, Integer level, int size, String tag, String tag1, String tag2, String tag3, long timestamp) throws Exception {
        if (TextUtils.isEmpty(logger)) {
            return new ArrayList<>();
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
                .andIf(timestamp > 0, "log_fulltime <= ?", timestamp)
                .andIf(level > 0, "level=?", level)
                .orderBy("log_fulltime desc, log_id desc")
                .limit(size)
                .selectList("*", LogModel.class);
    }

    @Override
    public void write(long log_id, String logger, String trace_id, Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content, String from, Date log_fulltime ,String class_name, String thread_name) throws Exception {
        Datetime datetime = null;
        if (log_fulltime == null) {
            datetime = new Datetime();
        } else {
            datetime = new Datetime(log_fulltime);
        }

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


        qr.set("log_date", datetime.getDate())
                .set("log_fulltime", datetime.getFulltime().getTime())
                .set("class_name", NameUtils.formatClassName(class_name))
                .set("thread_name", thread_name)
                .insert();
    }

    @Override
    public void writeAll(String logger, List<LogEvent> list) throws Exception {
        if (list.size() == 0) {
            return;
        }

        _db.table(logger).insertList(list, (event, item) -> {
            Datetime datetime = null;
            if (event.log_fulltime == null) {
                datetime = new Datetime();
            } else {
                datetime = new Datetime(event.log_fulltime);
            }

            item.set("log_id", event.log_id)
                    .set("trace_id", event.trace_id)
                    .set("level", event.level)
                    .setDf("tag", event.tag, "")
                    .setDf("tag1", event.tag1, "")
                    .setDf("tag2", event.tag2, "")
                    .setDf("tag3", event.tag3, "")
                    .setDf("summary", event.summary, "")
                    .setDf("content", event.content, "")
                    .setDf("from", event.from, "")
                    .set("class_name", NameUtils.formatClassName(event.class_name))
                    .set("thread_name", event.thread_name)
                    .set("log_date", datetime.getDate())
                    .set("log_fulltime", datetime.getFulltime().getTime());
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
