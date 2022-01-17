package org.noear.water.protocol.solution;

import org.noear.esearchx.EsContext;
import org.noear.esearchx.EsIndiceQuery;
import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.water.model.LogM;
import org.noear.water.model.TagCountsM;
import org.noear.water.protocol.LogSource;
import org.noear.water.protocol.model.log.LogModel;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.NameUtils;
import org.noear.water.utils.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author noear 2021/10/20 created
 */
public class LogSourceElasticsearch implements LogSource {
    final EsContext _db;
    final String _dsl;
    final boolean _allowHourShard;

    public LogSourceElasticsearch(EsContext db, boolean allowHourShard) {
        _db = db;
        _allowHourShard = allowHourShard;

        try {
            _dsl = Utils.getResourceAsString("water/water_log_es_dsl.json");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<LogModel> query(String logger, Integer level, int size, String tagx, long startLogId, long timestamp) throws Exception {
        if (TextUtils.isEmpty(logger)) {
            return new ArrayList<>();
        }

        String indiceAliasName = "water-" + logger;

        EsIndiceQuery eq = _db.indice(indiceAliasName).where(c -> {
            c.filter(); //用过滤，不打分

            if (TextUtils.isNotEmpty(tagx)) {
                if (tagx.startsWith("*")) {
                    c.term("trace_id", tagx.substring(1));
                } else if (tagx.startsWith("$")) {
                    c.match("content", tagx.substring(1));
                } else {
                    String[] tags = tagx.split("@");

                    if (tags.length > 0 && tags[0].length() > 0) {
                        c.term("tag", tags[0]);
                    }
                    if (tags.length > 1 && tags[1].length() > 0) {
                        c.term("tag1", tags[1]);
                    }
                    if (tags.length > 2 && tags[2].length() > 0) {
                        c.term("tag2", tags[2]);
                    }
                    if (tags.length > 3 && tags[3].length() > 0) {
                        c.term("tag3", tags[3]);
                    }
                    if (tags.length > 4 && tags[4].length() > 0) {
                        c.term("tag4", tags[4]);
                    }

                    if (tags.length > 6 && tags[6].length() > 0) {
                        c.term("group", tags[6]);
                    }

                    if (tags.length > 7 && tags[7].length() > 0) {
                        c.term("service", tags[7]);
                    }
                }
            }

            if (level != null && level > 0) {
                c.term("level", level);
            }

            if (startLogId > 0) {
                c.range("log_id", r -> r.lte(startLogId));
            }

            if (timestamp > 0) {
                c.range("log_fulltime", r -> r.lte(timestamp));
            }
        });

        return eq.limit(size)
                .orderByDesc("log_fulltime")
                .andByDesc("log_id")
                .selectList(LogModel.class)
                .getList();
    }

    @Override
    public List<TagCountsM> queryGroupCountBy(String logger, String group, String service, String filed) throws Exception {
        EsIndiceQuery query = _db.indice(logger);


        if (TextUtils.isNotEmpty(group) || TextUtils.isNotEmpty(service)) {
            query.where(w -> {
                if (TextUtils.isNotEmpty(group)) {
                    w.term("group", group);
                }

                if (TextUtils.isNotEmpty(service)) {
                    w.term("service", service);
                }
            });
        }

        ONode oNode = query.aggs(a -> a.terms(filed))
                .selectAggs()
                .get(filed + "_terms")
                .get("buckets");

        List<TagCountsM> list = new ArrayList<>();

        for (ONode n1 : oNode.ary()) {
            TagCountsM t1 = new TagCountsM();
            t1.tag = n1.get("key").getString();
            t1.counts = n1.get("doc_count").getLong();
        }

        return list;
    }

    @Override
    public void writeAll(String logger, List<LogM> list) throws Exception {
        if (list.size() == 0) {
            return;
        }

        Datetime nowDatetime = Datetime.Now();

        for (LogM event : list) {

            if (event.log_fulltime == null) {
                event.log_date = nowDatetime.getDate();
            } else {
                event.log_date = new Datetime(event.log_fulltime).getDate();
            }

            event.class_name = NameUtils.formatClassName(event.class_name);
        }

        if (allowHourShard()) {
            String indiceName = "water-" + logger + "-" + nowDatetime.toString("yyyyMMdd_H");

            _db.indice(indiceName).insertList(list);
        } else {
            String indiceName = "water-" + logger + "-" + nowDatetime.toString("yyyyMMdd");

            _db.indice(indiceName).insertList(list);
        }
    }

    @Override
    public void create(String logger) throws Exception {
        Datetime today = Datetime.Now();

        String indiceAliasName = "water-" + logger;

        //添加今天的记录器
        addIndiceByDate(logger, today, _dsl, indiceAliasName);
        //添加明天的记录器
        addIndiceByDate(logger, today.addDay(1), _dsl, indiceAliasName);
    }

    @Override
    public long clear(String logger, int keep_days, int limit_rows) throws Exception {
        Datetime today = Datetime.Now();

        String indiceAliasName = "water-" + logger;

        //添加今天的记录器
        addIndiceByDate(logger, today, _dsl, indiceAliasName);
        //添加明天的记录器
        addIndiceByDate(logger, today.addDay(1), _dsl, indiceAliasName);

        today.addDay(-1); //回到今天
        today.addDay(-keep_days);

        for (int i = 0; i < 10; i++) {
            removeIndiceByDate(logger, today.addDay(-1));
        }

        return 0;
    }

    @Override
    public boolean allowSearch() {
        return true;
    }

    @Override
    public boolean allowHourShard() {
        return _allowHourShard;
    }

    private void addIndiceByDate(String logger, Datetime datetime, String dsl, String alias) throws IOException {
        String indiceName = "water-" + logger + "-" + datetime.toString("yyyyMMdd");

        if (allowHourShard()) {
            for (int i = 0; i < 24; i++) {
                addIndiceItemDo(indiceName + "_" + i, dsl, alias);
            }
        } else {
            addIndiceItemDo(indiceName, dsl, alias);
        }
    }

    private void addIndiceItemDo(String indiceName, String dsl, String alias) throws IOException {
        if (_db.indiceExist(indiceName) == false) {
            _db.indiceCreate(indiceName, dsl);
            _db.indiceAliases(a -> a.add(indiceName, alias));
        }
    }

    private void removeIndiceByDate(String logger, Datetime datetime) throws IOException {
        String indiceName = "water-" + logger + "-" + datetime.toString("yyyyMMdd");

        _db.indiceDrop(indiceName);

        if (allowHourShard()) {
            for (int i = 0; i < 24; i++) {
                _db.indiceDrop(indiceName + "_" + i);
            }
        }
    }

    @Override
    public void close() throws IOException {

    }
}
