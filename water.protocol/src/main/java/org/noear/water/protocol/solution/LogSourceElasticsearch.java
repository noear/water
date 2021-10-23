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
            _dsl = Utils.getResourceAsString("water/water_log_dsl.json", "utf-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<LogModel> query(String logger, String trace_id, Integer level, int size, String tag, String tag1, String tag2, String tag3, long timestamp) throws Exception {
        String indiceAliasName = "water__" + logger;

        EsIndiceQuery eq = _db.indice(indiceAliasName).where(c -> {
            c.filter(); //用过滤，不打分

            if (TextUtils.isNotEmpty(trace_id)) {
                c.term("trace_id", trace_id);
            }

            if (TextUtils.isNotEmpty(tag)) {
                c.term("tag", tag);
            }

            if (TextUtils.isNotEmpty(tag1)) {
                c.term("tag1", tag1);
            }

            if (TextUtils.isNotEmpty(tag2)) {
                c.term("tag2", tag2);
            }

            if (TextUtils.isNotEmpty(tag3)) {
                c.term("tag3", tag3);
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

        String indiceAliasName = "water__" + logger;

        _db.indice(indiceAliasName).insertList(list);
    }

    @Override
    public void create(String logger) throws Exception {
        Datetime today = Datetime.Now();

        String indiceAliasName = "water__" + logger;

        //添加今天的记录器
        addIndiceByDate(logger, today, _dsl, indiceAliasName);
        //添加明天的记录器
        addIndiceByDate(logger, today.addDay(1), _dsl, indiceAliasName);
    }

    @Override
    public long clear(String logger, int keep_days, int limit_rows) throws Exception {
        Datetime today = Datetime.Now();

        String indiceAliasName = "water__" + logger;

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
        String indiceName2 = "water__" + logger + "_" + datetime.toString("yyyyMMdd");
        if (_db.indiceExist(indiceName2) == false) {
            _db.indiceCreate(indiceName2, dsl);
            _db.indiceAliases(a -> a.add(indiceName2, alias));
        }
    }

    private void removeIndiceByDate(String logger, Datetime datetime) throws IOException {
        String indiceName2 = "water__" + logger + "_" + datetime.toString("yyyyMMdd");
        _db.indiceDrop(indiceName2);
    }
}
