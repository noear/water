package org.noear.rubber;

import org.noear.rubber.models.SchemeRuleModel;
import org.noear.snack.ONode;

import java.util.*;

public final class RubberResponse {

    public RubberRequest request;

    //
    //引擎产出部份
    //>匹配报告
    public Matcher matcher = new Matcher();
    //>评估报告
    public Evaluation evaluation = new Evaluation();


    public ONode session_json;
    public ONode model_json;//用于log

    private ONode _matcher_json;
    public ONode matcher_json() {
        if (_matcher_json == null) {
            ONode data = new ONode();
            data.set("total", matcher.total);
            data.set("value", matcher.value);

            ONode details = data.get("details").asObject();
            matcher.details.forEach((k, v) -> {
                ONode temp = details.get(k);

                temp.set("ok", v.is_match ? 1 : 0);
                temp.set("s", v.scheme);
                temp.set("t", v.total);
                temp.set("v", v.value);
                temp.set("r", v.relation);
                temp.set("ts", v.time_end-v.time_start);

            });

            _matcher_json = data;
        }

        return _matcher_json;
    }

    private ONode _evaluation_json;
    public ONode evaluation_json() {
        if(_evaluation_json == null) {
            ONode data = new ONode();
            data.set("score", evaluation.score);
            data.set("advise", evaluation.advice);
            data.set("exception", evaluation.exception);

            ONode details = data.get("details").asArray();

            evaluation.details.forEach(p -> {
                ONode n = new ONode();
                n.set("id", p.rule_id);
                n.set("s", p.scheme);
                n.set("r", p.rule_note);
                n.set("n", p.score);
                n.set("a", p.advice);
                n.set("m", p.is_match);
                n.set("v", p.field_vals);
                n.set("ts",p.timespan);

                details.add(n);
            });

            _evaluation_json = data;
        }

        return _evaluation_json;
    }

    //
    //计时
    public Date start_time;
    public Date end_time;

    public long timespan(){
        return end_time.getTime() - start_time.getTime();
    }


    protected RubberResponse(RubberRequest request){
        this.request = request;
    }

    protected void start() {
        if(start_time == null){
            start_time = new Date();
        }
    }

    public void end(RubberContext context) throws Exception {
        model_json = context.model_json();
        session_json = ONode.load(context.session); //OMapper.tran(context.session);
        end_time = new Date();
    }

    public void end(){
        model_json = new ONode();
        session_json = new ONode();
        end_time = new Date();
    }

    protected void logMatcher(int totalAdd, int valAdd) {
        matcher.total += totalAdd;
        matcher.value += valAdd;
    }

    protected MatcherItem addMatcherItem(String scheme, String name_display, int relation, int total){
        MatcherItem item = new MatcherItem(name_display, relation, total);
        matcher.details.put(scheme, item);

        return item;
    }

    protected void logMatcherItem(String scheme,  int val) {
        if (val > 0) {
            matcher.details.get(scheme).value = val;
        }
    }

    protected  void logEvaluation(SchemeRuleModel rule) {
        if (rule != null) {
            evaluation.score += rule.score;
            if (evaluation.advice < rule.advice) {
                evaluation.advice = rule.advice;
            }
        }
    }

    protected void logEvaluationItem(String scheme, SchemeRuleModel rule, int advice, String fval,long timespan, boolean is_match) {
        EvaluationItem p = new EvaluationItem();

        if (rule != null) {
            p.rule_id = rule.rule_id;
            p.rule_note = rule.name_display;
            p.score = rule.score;
            p.advice = advice;
        }

        p.scheme = scheme;
        p.field_vals = fval;
        p.is_match = is_match;
        p.timespan = timespan;

        evaluation.details.add(p);
    }


    //匹配报告
    public static class Matcher {
        public int total;
        public int value;

        public Map<String, MatcherItem> details = new HashMap<>();
    }

    //评估报告
    public static class Evaluation{
        public int score;
        public int advice;
        public int exception;

        //>详情条目
        public List<EvaluationItem> details  = new ArrayList<>();
    }

    //触发项目
    public static class EvaluationItem {
        public String scheme;
        public int rule_id;
        public String rule_note;
        public int advice;
        public int score;
        public String field_vals;
        public boolean is_match;
        public long timespan;
    }

    public static class MatcherItem{
        public MatcherItem(String scheme, int relation, int total){
            this.scheme = scheme;
            this.relation = relation;
            this.total = total;
            this.time_start = System.currentTimeMillis();
        }

        public String scheme;
        public int total;
        public int value;
        public int relation;//0并且；1或者
        public boolean is_match;
        public long time_start;
        public long time_end;
    }
}
