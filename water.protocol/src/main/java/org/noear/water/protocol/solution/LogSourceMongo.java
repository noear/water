package org.noear.water.protocol.solution;

import org.noear.water.log.Level;
import org.noear.water.log.LogEvent;
import org.noear.water.protocol.LogSource;
import org.noear.water.protocol.model.log.LogModel;
import org.noear.water.utils.NameUtils;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.TextUtils;
import org.noear.weed.mongo.MgContext;
import org.noear.weed.mongo.MgTableQuery;

import java.util.*;

/**
 * @author noear 2021/2/2 created
 */
public class LogSourceMongo implements LogSource {
    MgContext _db;

    public LogSourceMongo(MgContext db) {
        _db = db;
    }

    @Override
    public List<LogModel> query(String logger, String trace_id, Integer level, int size, String tag, String tag1, String tag2, String tag3, long timestamp) throws Exception {
        if (TextUtils.isEmpty(logger)) {
            return new ArrayList<>();
        }

        MgTableQuery tb = _db.table(logger);

        tb.whereTrue();

        if (TextUtils.isNotEmpty(trace_id)) {
            tb.andEq("trace_id", trace_id);
        }

        if (TextUtils.isNotEmpty(tag)) {
            tb.andEq("tag", tag);
        }

        if (TextUtils.isNotEmpty(tag1)) {
            tb.andEq("tag1", tag1);
        }

        if (TextUtils.isNotEmpty(tag2)) {
            tb.andEq("tag2", tag2);
        }

        if (TextUtils.isNotEmpty(tag3)) {
            tb.andEq("tag3", tag3);
        }

        if (level != null && level > 0) {
            tb.andEq("level", level);
        }

        if (timestamp > 0) {
            tb.andLte("log_fulltime", timestamp);
        }

        return tb.orderByDesc("log_fulltime")
                .andByDesc("log_id")
                .selectList(LogModel.class);
    }

    @Override
    public void write(long log_id, String logger, String trace_id, Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content, String from, Date log_fulltime, String class_name, String thread_name) throws Exception{

        Datetime datetime = null;
        if (log_fulltime == null) {
            datetime = new Datetime();
        } else {
            datetime = new Datetime(log_fulltime);
        }

        MgTableQuery tb = _db.table(logger);

        tb.set("log_id", log_id);
        tb.set("trace_id", trace_id);
        tb.set("level", level.code);
        tb.set("tag", tag);
        tb.set("tag1", tag1);
        tb.set("tag2", tag2);
        tb.set("tag3", tag3);
        tb.set("summary", summary);
        tb.set("content", content);
        tb.set("from", from);

        tb.set("class_name", NameUtils.formatClassName(class_name));
        tb.set("thread_name", thread_name);

        tb.set("log_date", datetime.getDate());
        tb.set("log_fulltime", datetime.getFulltime());

        tb.insert();
    }

    @Override
    public void writeAll(String logger, List<LogEvent> list) throws Exception {
        if (list.size() == 0) {
            return;
        }

        List<Map<String, Object>> dataList = new ArrayList<>();

        for (LogEvent event : list) {
            Datetime datetime = null;
            if (event.log_fulltime == null) {
                datetime = new Datetime();
            } else {
                datetime = new Datetime(event.log_fulltime);
            }

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("log_id", event.log_id);
            data.put("trace_id", event.trace_id);
            data.put("level", event.level);
            data.put("tag", event.tag);
            data.put("tag1", event.tag1);
            data.put("tag2", event.tag2);
            data.put("tag3", event.tag3);
            data.put("summary", event.summary);
            data.put("content", event.content);
            data.put("from", event.from);

            data.put("class_name", NameUtils.formatClassName(event.class_name));
            data.put("thread_name", event.thread_name);

            data.put("log_date", datetime.getDate());
            data.put("log_fulltime", datetime.getFulltime().getTime());

            dataList.add(data);
        }

        _db.table(logger).insertList(dataList);
    }

    @Override
    public long clear(String logger, int keep_days, int limit_rows) throws Exception {
        initIndex(logger);

        int date = Datetime.Now().addDay(-keep_days).getDate();

        return _db.table(logger).whereLte("log_date", date).delete();
    }

    private void initIndex(String logger){
        _db.table(logger).orderByDesc("level").createIndex(true);

        _db.table(logger).orderByDesc("log_date").createIndex(true);
        _db.table(logger).orderByDesc("log_id").createIndex(true);
        _db.table(logger).orderByDesc("log_fulltime").createIndex(true);

        _db.table(logger).orderByDesc("tag").createIndex(true);
        _db.table(logger).orderByDesc("tag1").createIndex(true);
        _db.table(logger).orderByDesc("tag2").createIndex(true);
        _db.table(logger).orderByDesc("tag3").createIndex(true);

        _db.table(logger).orderByDesc("trace_id").createIndex(true);

        _db.table(logger).orderByDesc("log_fulltime").andByDesc("log_id")
                .createIndex(true);
    }
}
