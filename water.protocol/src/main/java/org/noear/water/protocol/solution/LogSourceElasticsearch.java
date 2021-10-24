package org.noear.water.protocol.solution;

import org.noear.esearchx.EsContext;
import org.noear.esearchx.EsIndiceQuery;
import org.noear.solon.Utils;
import org.noear.water.log.LogEvent;
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

    public LogSourceElasticsearch(EsContext db) {
        _db = db;
        try {
            _dsl = Utils.getResourceAsString("water/water_log_es_dsl.json", "utf-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<LogModel> query(String logger, Integer level, int size, String tagx, long timestamp) throws Exception {
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
                }
            }

            if (level != null && level > 0) {
                c.term("level", level);
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
    public void writeAll(String logger, List<LogEvent> list) throws Exception {
        if (list.size() == 0) {
            return;
        }

        for (LogEvent event : list) {
            Datetime datetime = null;
            if (event.log_fulltime == null) {
                datetime = new Datetime();
            } else {
                datetime = new Datetime(event.log_fulltime);
            }

            event.log_date = datetime.getDate();
            event.class_name = NameUtils.formatClassName(event.class_name);
        }

        String indiceName = "water-" + logger + "-" + Datetime.Now().toString("yyyyMMdd");

        _db.indice(indiceName).insertList(list);
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

    private void addIndiceByDate(String logger, Datetime datetime, String dsl, String alias) throws IOException {
        String indiceName = "water-" + logger + "-" + datetime.toString("yyyyMMdd");
        if (_db.indiceExist(indiceName) == false) {
            _db.indiceCreate(indiceName, dsl);
            _db.indiceAliases(a -> a.add(indiceName, alias));
        }
    }

    private void removeIndiceByDate(String logger, Datetime datetime) throws IOException {
        String indiceName = "water-" + logger + "-" + datetime.toString("yyyyMMdd");
        _db.indiceDrop(indiceName);
    }
}
