package org.noear.water.protocol.solution;

import org.noear.water.log.Level;
import org.noear.water.log.LogEvent;
import org.noear.water.protocol.LogSource;
import org.noear.water.protocol.model.LogModel;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.MongoX;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DataItem;
import org.noear.weed.wrap.ClassWrap;

import java.util.*;

/**
 * @author noear 2021/2/2 created
 */
public class LogSourceMongo implements LogSource {
    MongoX mongoX;

    public LogSourceMongo(MongoX mongoX) {
        this.mongoX = mongoX;
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

        Map<String, Object> filter = new LinkedHashMap<>();

        if (TextUtils.isNotEmpty(trace_id)) {
            filter.put("trace_id", trace_id);
        }

        if (TextUtils.isNotEmpty(tag)) {
            filter.put("tag", tag);
        }

        if (TextUtils.isNotEmpty(tag1)) {
            filter.put("tag1", tag1);
        }

        if (TextUtils.isNotEmpty(tag2)) {
            filter.put("tag2", tag2);
        }

        if (TextUtils.isNotEmpty(tag3)) {
            filter.put("tag3", tag3);
        }

        if (log_date > 0) {
            filter.put("log_date", log_date);
        }

        if (level > 0) {
            filter.put("level", level);
        }

        if (log_id > 0) {
            Map<String, Object> tmp = new HashMap<>();
            tmp.put("$lte", log_id);
            filter.put("log_id", tmp);
        }

        Map<String, Object> orderBy = new LinkedHashMap<>();
        orderBy.put("log_fulltime", -1);
        orderBy.put("log_id", -1);


        List<Map<String, Object>> list0 = mongoX.findTop(logger, filter, orderBy, size);
        List<LogModel> list = new ArrayList<>();

        for (Map<String, Object> map : list0) {
            LogModel item = ClassWrap.get(LogModel.class).toEntity(new DataItem().setMap(map));
            list.add(item);
        }

        return list;
    }

    @Override
    public void write(long log_id, String logger, String trace_id, Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content, String from, Date log_fulltime) {
        try {
            Datetime datetime = null;
            if (log_fulltime == null) {
                datetime = new Datetime();
            } else {
                datetime = new Datetime(log_fulltime);
            }

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("log_id", log_id);
            data.put("trace_id", trace_id);
            data.put("level", level.code);
            data.put("tag", tag);
            data.put("tag1", tag1);
            data.put("tag2", tag2);
            data.put("tag3", tag3);
            data.put("summary", summary);
            data.put("content", content);
            data.put("from", from);

            data.put("log_date", datetime.getDate());
            data.put("log_fulltime", datetime.getFulltime());

            mongoX.insertOne(logger, data);

        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void writeAll(String logger, List<LogEvent> list) {
        if (list.size() == 0) {
            return;
        }

        try {
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

                data.put("log_date", datetime.getDate());
                data.put("log_fulltime", datetime.getFulltime());

                dataList.add(data);
            }

            mongoX.insertMany(logger, dataList);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public long stat(String logger, Integer level, Integer log_date) throws Exception {
        Map<String, Object> filter = new LinkedHashMap<>();

        if (level != null) {
            filter.put("level", level);
        }

        if (log_date != null) {
            filter.put("log_date", log_date);
        }

        if (filter.size() > 0) {
            return mongoX.countDocuments(logger, filter);
        } else {
            return mongoX.count(logger);
        }
    }

    @Override
    public void clear(String logger, int keep_days) {
        initIndex(logger);

        int date = Datetime.Now().addDay(-keep_days).getDate();
        try {
            Map<String, Object> filter = new LinkedHashMap<>();

            Map<String, Object> tmp = new LinkedHashMap<>();
            tmp.put("$lte", date);
            filter.put("log_date", tmp);

            mongoX.deleteMany(logger, filter);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    private void initIndex(String logger){
        Map<String, Object> index = null;
        Map<String, Object> options = new HashMap<>();
        options.put("background", true);

        index = new HashMap<>();
        index.put("level", -1);
        mongoX.createIndex(logger, index, options);

        index = new HashMap<>();
        index.put("log_date", -1);
        mongoX.createIndex(logger, index, options);

        index = new HashMap<>();
        index.put("log_id", -1);
        mongoX.createIndex(logger, index, options);

        index = new HashMap<>();
        index.put("log_fulltime", -1);
        mongoX.createIndex(logger, index, options);

        index = new HashMap<>();
        index.put("tag", -1);
        mongoX.createIndex(logger, index, options);

        index = new HashMap<>();
        index.put("tag1", -1);
        mongoX.createIndex(logger, index, options);

        index = new HashMap<>();
        index.put("tag2", -1);
        mongoX.createIndex(logger, index, options);

        index = new HashMap<>();
        index.put("tag3", -1);
        mongoX.createIndex(logger, index, options);

        index = new HashMap<>();
        index.put("trace_id", -1);
        mongoX.createIndex(logger, index, options);

        index = new HashMap<>();
        index.put("log_fulltime", -1);
        index.put("log_id", -1);
        mongoX.createIndex(logger, index, options);
    }
}
