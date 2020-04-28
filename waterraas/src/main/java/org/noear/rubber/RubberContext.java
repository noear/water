package org.noear.rubber;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.noear.rubber.models.SchemeRuleModel;
import org.noear.snack.ONode;
import org.noear.water.WaterClient;
import org.noear.water.log.Level;
import org.noear.water.utils.TextUtils;

import java.util.HashMap;
import java.util.Map;

//上下文 （必须要 public ，不然js引擎调用失败）
public final class RubberContext {
    //
    //计算基础
    private final String _context_id;//上下ID（要求全局唯一，用guid生成）
    public String context_id(){return _context_id;}
    private RubberResponse _response;
    public RubberResponse response(){return _response;}

    public int score(){ //评估分数
        return response().evaluation.score;
    }
    public int advice(){ //评估建议
        return response().evaluation.advice;
    }

    //是否为debug状态
    public boolean is_debug = false;

    //
    //引擎输入部份
    private ScriptObjectMirror _args;
    private ScriptObjectMirror _model; //主模型；需要生成
    private Map<String,ScriptObjectMirror> _modelLib = new HashMap<>();//模型库

    public ScriptObjectMirror args(){
        return _args;
    }
    public ScriptObjectMirror model(){
        return _model;
    }  //数据模型：为js model，这里只存一个key
    public ScriptObjectMirror model(String tagName) throws Exception{ //::tag/name
        if(TextUtils.isEmpty(tagName))
            return model();
        else {
            if (_modelLib.containsKey(tagName) == false) {
                do_loadModel(tagName);
            }

            return _modelLib.get(tagName);
        }
    }

    public ONode model_json() {
        ONode temp = new ONode();
        _modelLib.forEach((k,v)->{
            temp.set(k, ONode.load((ScriptObjectMirror) v.get("_$md")));
        });

        return temp;
    }

    //
    //引擎操作部份
    public ScriptObjectMirror session = null; //不能被移走!!!

    //
    //中间控制
    public boolean is_cancel; //是否取消


    public RubberContext(RubberRequest request) throws Exception {
        _context_id = Rubber.guid();

        _response = new RubberResponse(request);

        _args = RcRunner.buildArgs(request.args);

        session = RcRunner.buildSession(this);
    }


    protected void loadModel(String tagName) throws Exception {
        if(TextUtils.isEmpty(tagName)){
            return;
        }

        if(_modelLib.containsKey(tagName)){
            return;
        }

        do_loadModel(tagName);
    }

    private void do_loadModel(String tagName) throws Exception{
        ScriptObjectMirror temp = RcRunner.buildModel(this, tagName);
        if (_model == null) { //第一个加载的模型，定义为主模型
            _model = temp;
        }

        _modelLib.put(tagName, temp);
    }


    public RubberResponse.MatcherItem start(String type, String name, String name_display, int relation, int ruleNum) {
        _response.logMatcher(ruleNum, 0);
        return _response.addMatcherItem(name, name_display, relation, ruleNum);
    }

    public void end(String type, RubberResponse.MatcherItem sm) throws Exception {
        if (sm.relation > 0) { //0并且关系；1或者关系
            sm.is_match = sm.value > 0;
        } else {
            sm.is_match = (sm.total > 0 && sm.value == sm.total);
        }
        sm.time_end = System.currentTimeMillis();
    }

    //记录列的值(提前记录，并让异常的情况暴发)
    //
    StringBuilder valLoger =new StringBuilder();
    public boolean v(Object val) {
        valLoger.append(val).append(";");

        return val != null;
    }

    //记录规则异常
    //
    public void e(String scheme,int rule_id, String err) {
        WaterClient.Log.append("water_log_raas_error", Level.ERROR, "s", scheme,
                scheme + "/" + rule_id + "::" + response().request.args.toJson(), err);
    }

    public RcState t(){
        return new RcState().start();
    }

    //规则审判
    //@return : 是否中断
    public boolean judge(RubberResponse.MatcherItem sm, int rule_id, RcState state) {
        if (state.is_match) {
            sm.value++;

            //1.匹配记录
            _response.logMatcher(0, 1);

            //2.评估记录
            SchemeRuleModel rule = RcWarehouse.ruleGet(rule_id);
            _response.logEvaluation(rule);

            //3.详情记录
            _response.logEvaluationItem(sm.scheme, rule, rule.advice, valLoger.toString(), state.timespan, true);
        } else {
            if (state.is_error) {
                SchemeRuleModel rule = RcWarehouse.ruleGet(rule_id);
                _response.logEvaluationItem(sm.scheme, rule, -1, valLoger.toString(), state.timespan, false);
                _response.evaluation.exception++;
            } else {
                if (is_debug) {
                    SchemeRuleModel rule = RcWarehouse.ruleGet(rule_id);
                    _response.logEvaluationItem(sm.scheme, rule, rule.advice, valLoger.toString(), state.timespan, false);
                }
            }
        }

        valLoger = new StringBuilder();

        return false; //不中断
    }


    public String jsobj(){
        return "RubberUtil.currentContext()";
    }
}
