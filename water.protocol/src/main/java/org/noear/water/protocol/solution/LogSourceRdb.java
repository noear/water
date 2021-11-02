package org.noear.water.protocol.solution;

import org.noear.solon.Utils;
import org.noear.water.model.LogM;
import org.noear.water.model.TagCountsM;
import org.noear.water.protocol.LogSource;
import org.noear.water.protocol.model.log.LogModel;
import org.noear.water.utils.NameUtils;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import org.noear.weed.wrap.DbType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LogSourceRdb implements LogSource {
    final DbContext _db;
    final String _rdbTml;
    final String _chTml;

    public LogSourceRdb(DbContext db) {
        _db = db;
        try {
            _rdbTml = Utils.getResourceAsString("water/water_log_rdb_tml.sql");
            _chTml = Utils.getResourceAsString("water/water_log_ch_tml.sql");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<LogModel> query(String logger, Integer level, int size, String tagx, long startLogId, long timestamp) throws Exception {
        if (TextUtils.isEmpty(logger)) {
            return new ArrayList<>();
        }

        if (level == null) {
            level = 0;
        }

        DbTableQuery tb = _db.table(logger).usingExpr(false);

        tb.whereTrue();

        if (TextUtils.isNotEmpty(tagx)) {
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

        if(startLogId > 0){
            tb.andLte("log_id", startLogId);
        }

        if (timestamp > 0) {
            tb.andLte("log_fulltime", timestamp);
        }

        return tb.limit(size)
                .orderByDesc("log_fulltime")
                .andByDesc("log_id")
                .selectList("*", LogModel.class);
    }

    @Override
    public List<TagCountsM> queryGroupCountBy(String logger, String service, String filed) throws Exception {
        DbTableQuery query = _db.table(logger);

        if (TextUtils.isNotEmpty(service)) {
            query.whereEq("service", service);
        }

        return query.groupBy(filed)
                .selectList(filed + " tag, COUNT(*) counts", TagCountsM.class);
    }

    @Override
    public void writeAll(String logger, List<LogM> list) throws Exception {
        if (list.size() == 0) {
            return;
        }

        _db.table(logger).usingExpr(false)
                .insertList(list, (event, item) -> {
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
                            .setDf("tag4", event.tag4, "")
                            .set("weight",event.weight)
                            .setDf("group", event.group, "")
                            .setDf("service", event.service, "")
                            .set("class_name", NameUtils.formatClassName(event.class_name))
                            .set("thread_name", event.thread_name)
                            .setDf("content", event.content, "")
                            .setDf("metainfo", event.metainfo, "")
                            .setDf("from", event.from, "")
                            .set("log_date", datetime.getDate())
                            .set("log_fulltime", datetime.getFulltime().getTime());
                });
    }

    @Override
    public void create(String logger) throws Exception {
        if (_db.getType() == DbType.ClickHouse) {
            String sql = _chTml.replace("${logger}", logger);
            _db.exe(sql);
        } else {
            String sql = _rdbTml.replace("${logger}", logger);
            _db.exe(sql);
        }
    }

    @Override
    public long clear(String logger, int keep_days, int limit_rows) throws Exception {
        int date = Datetime.Now().addDay(-keep_days).getDate();

        if (limit_rows > 0) {
            return _db.table(logger).whereLte("log_date", date).limit(limit_rows).delete();
        } else {
            return _db.table(logger).whereLte("log_date", date).delete();
        }
    }

    @Override
    public void close() throws IOException {
        _db.close();
    }
}
