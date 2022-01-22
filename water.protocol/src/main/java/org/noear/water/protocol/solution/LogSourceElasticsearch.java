package org.noear.water.protocol.solution;

import org.noear.esearchx.EsContext;
import org.noear.esearchx.EsQuery;
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
import java.util.*;

/**
 * @author noear 2021/10/20 created
 */
public class LogSourceElasticsearch implements LogSource {
    final EsContext _db;
    final String _stream_dsl;
    final String _policy_dsl;

    public LogSourceElasticsearch(EsContext db) {
        _db = db;

        try {
            _stream_dsl = Utils.getResourceAsString("water/water_log_es_stream_dsl.json");
            _policy_dsl = Utils.getResourceAsString("water/water_log_es_policy_dsl.json");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<LogModel> query(String logger, Integer level, int size, String tagx, long startLogId, long timestamp) throws Exception {
        if (TextUtils.isEmpty(logger)) {
            return new ArrayList<>();
        }

        String streamName = "water." + logger + ".stream";

        EsQuery eq = _db.stream(streamName).where(c -> {
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
        String streamName = "water." + logger + ".stream";

        EsQuery query = _db.stream(streamName);


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
        List<ONode> docs = new ArrayList<>();

        for (LogM event : list) {

            if (event.log_fulltime == null) {
                event.log_date = nowDatetime.getDate();
            } else {
                event.log_date = new Datetime(event.log_fulltime).getDate();
            }

            event.class_name = NameUtils.formatClassName(event.class_name);

            ONode doc = ONode.loadObj(event).build(n -> {
                n.set("@timestamp", event.log_fulltime);
            });
            docs.add(doc);
        }


        String streamName = "water." + logger + ".stream";

        _db.stream(streamName).insertList(docs);
    }

    @Override
    public void create(String logger, int keep_days) throws Exception {
        if (keep_days < 1) {
            keep_days = 15; //默认处理
        }

        String streamPatterns = "water." + logger + ".*";
        String streamName = "water." + logger + ".stream";
        String templateName = "water." + logger + ".tml";
        String policyName = "water." + logger + ".policy";

        //创建或修改策略（主要是时间可能会变化）
        ONode policyDslNode = ONode.loadStr(_policy_dsl);
        policyDslNode.select("policy.phases.delete").get("min_age").val(keep_days + "d");
        _db.policyCreate(policyName, policyDslNode.toJson());

        //创建模板（如果存在，则不管）
        if (_db.templateExist(templateName) == false) {
            ONode tmlDslNode = ONode.loadStr(_stream_dsl);
            //设定匹配模式
            tmlDslNode.getOrNew("index_patterns").val(streamPatterns);
            //设定策略
            tmlDslNode.get("template").get("settings").get("index.lifecycle.name").val(policyName);
            //设定翻转别名
            tmlDslNode.get("template").get("settings").get("index.lifecycle.rollover_alias").val(streamName);

            _db.templateCreate(templateName, tmlDslNode.toJson());
        }

        //如果有同名索引则删掉 //否则数据流不会自动创建
        _db.indiceDrop(streamName);
    }

    @Override
    public long clear(String logger, int keep_days, int limit_rows) throws Exception {
        //根据策略自动清除

        return 0;
    }

    @Override
    public boolean allowSearch() {
        return true;
    }


    @Override
    public void close() throws IOException {

    }
}