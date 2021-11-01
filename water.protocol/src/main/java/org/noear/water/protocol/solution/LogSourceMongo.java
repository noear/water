package org.noear.water.protocol.solution;

import com.mongodb.client.AggregateIterable;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.noear.snack.ONode;
import org.noear.water.model.LogM;
import org.noear.water.model.TagCountsM;
import org.noear.water.protocol.LogSource;
import org.noear.water.protocol.model.log.LogModel;
import org.noear.water.utils.NameUtils;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.TextUtils;
import org.noear.weed.mongo.MgContext;
import org.noear.weed.mongo.MgTableQuery;

import javax.print.Doc;
import java.io.IOException;
import java.util.*;

/**
 * @author noear 2021/2/2 created
 */
public class LogSourceMongo implements LogSource {
    final MgContext _db;

    public LogSourceMongo(MgContext db) {
        _db = db;
    }

    @Override
    public List<LogModel> query(String logger, Integer level, int size, String tagx, long timestamp) throws Exception {
        if (TextUtils.isEmpty(logger)) {
            return new ArrayList<>();
        }

        if (level == null) {
            level = 0;
        }

        MgTableQuery tb = _db.table(logger);

        tb.whereTrue();

        if(TextUtils.isNotEmpty(tagx)) {
            if (tagx.startsWith("*")) {
                tb.andEq("trace_id", tagx.substring(1));
            } else {
                String[] tags = tagx.split("@");

                if (tags.length > 0 && tags[0].length() > 0) {
                    tb.andEq("tag", tags[0]);
                }
                if (tags.length > 1 && tags[1].length() > 0) {
                    tb.andEq("tag1", tags[1]);
                }
                if (tags.length > 2 && tags[2].length() > 0) {
                    tb.andEq("tag2", tags[2]);
                }
                if (tags.length > 3 && tags[3].length() > 0) {
                    tb.andEq("tag3", tags[3]);
                }
            }
        }

        if (level != null && level > 0) {
            tb.andEq("level", level);
        }

        if (timestamp > 0) {
            tb.andLte("log_fulltime", timestamp);
        }

        return tb.limit(size)
                .orderByDesc("log_fulltime")
                .andByDesc("log_id")
                .selectList(LogModel.class);
    }

    @Override
    public List<TagCountsM> queryGroupCountBy(String logger, String filed) throws Exception {
        List<Document> filter = Arrays.asList(new Document("$group",
                new Document("_id",
                        new Document("tag", "$" + filed)
                                .append("counts", new Document("$sum", 1)))));


        AggregateIterable<Document> docList = _db.mongo().getCollection(logger).aggregate(filter);

        List<TagCountsM> tagCountsList = new ArrayList<>();
        ONode oNode = new ONode();
        for (Document doc : docList) {
            TagCountsM tc = oNode.fill(doc).get("_id").toObject(TagCountsM.class);
            tagCountsList.add(tc);
        }

        return tagCountsList;
    }

    @Override
    public void writeAll(String logger, List<LogM> list) throws Exception {
        if (list.size() == 0) {
            return;
        }

        List<Map<String, Object>> dataList = new ArrayList<>();

        for (LogM event : list) {
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
            data.put("content", event.content);
            data.put("from", event.from);

            data.put("group", event.group);
            data.put("service", event.service);
            data.put("class_name", NameUtils.formatClassName(event.class_name));
            data.put("thread_name", event.thread_name);

            data.put("log_date", datetime.getDate());
            data.put("log_fulltime", datetime.getFulltime().getTime());

            dataList.add(data);
        }

        _db.table(logger).insertList(dataList);
    }

    @Override
    public void create(String logger) throws Exception {

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

    @Override
    public void close() throws IOException {
        _db.close();
    }
}
