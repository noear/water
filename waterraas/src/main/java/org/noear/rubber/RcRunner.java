package org.noear.rubber;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.noear.snack.ONode;
import org.noear.luffy.executor.ExecutorFactory;
import org.noear.water.utils.TextUtils;
import org.noear.weed.ext.Fun0Ex;
import org.noear.rubber.models.BlockModel;
import org.noear.rubber.workflow.*;
import org.noear.rubber.models.ModelModel;
import org.noear.rubber.models.SchemeModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class RcRunner {
    private static String language = "javascript";
    private static List<String> obj_loaded =  Collections.synchronizedList(new ArrayList<>());

    private static boolean _inited = false;
    public static void tryInit() throws Exception {
        if (!_inited) {
            _inited = true;

            //不用写任何代码
            StringBuilder sb = new StringBuilder();

            sb.append("this.$dblock=function(b,x,t){var fk='DD_'+b.replace('/','_');var fo=this[fk];if(!fo){include('dd:'+b);this[fk].scan(x,t)}else{return fo.scan(x,t)}};");

            sb.append("this.MM_run=function(c){var m=c.model();m._$is_debug=c.is_debug;for(var f in m){if(!(f.startsWith('_'))){try{m[f]()}catch(err){throw f+' error:'+err;}}}};");
            sb.append("this.MM_run_item=function(c,key){var m=c.model();m._$is_debug=c.is_debug;m[key]()};");

            //sb.append("this.SR_test=function(sr,r,c,m){var st=c.t();if(c.is_debug){try{r.check(c,m)}catch(err){throw new Error('S:'+sr.name+'/r'+r.id+' '+err);}}try{if(c.is_debug==false){r.check(c,m)}return st.stop(r.test(c,m),false)}catch(err){c.e(sr.name_display,r.id,err.message);return st.stop(false,true)}};");
            sb.append("this.SR_test=function(sr,r,c,m){var st=c.t();var xr=false;if(c.is_debug){try{xr=r.check(c,m)}catch(err){throw new Error('S:'+sr.name+'/r'+r.id+' '+err);}}try{if(c.is_debug==false){xr=r.check(c,m)}if(xr){return st.stop(r.test(c,m),false)}else{return st.stop(false,true)}}catch(err){c.e(sr.name_display,r.id,err.message);return st.stop(false,true)}};");
            sb.append("this.SR_run=function(c,sr){var m=c.model(sr.model);var sm=c.start('R',sr.name,sr.name_display,sr.relation,sr.total);for(var k in sr.rule){var r=sr.rule[k];if(c.judge(sm,r.id,SR_test(sr,r,c,m))){break}}c.end('R',sm);sr.event(c,m,sm)};");
            sb.append("this.SR_run_rule_all=function(c,sr){var m=c.model(sr.model);var sm=c.start('R',sr.name,sr.name_display,sr.relation,sr.total);for(var k in sr.rule){var r=sr.rule[k];if(c.judge(sm,r.id,SR_test(sr,r,c,m))){break}}c.end('R',sm)};");
            sb.append("this.SR_run_rule_one=function(c,sr,key){var m=c.model(sr.model);m._$is_debug=c.is_debug;var r=sr.rule[key];if(r){var sm=c.start('R',sr.name,sr.name_display,sr.relation,1);c.judge(sm,r.id,SR_test(sr,r,c,m));c.end('R',sm)}else{c.session.error=\"doesn't exist R:\"+key}};");
            sb.append("this.SR_run_event=function(c,sr){var m=c.model(sr.model);var sm=c.start('R',sr.name,sr.name_display,sr.relation,0);sr.event(c,m,sm);c.end('R',sm)};");

            //
            // 与旧代码兼容
            //
            sb.append("function obj2obj(obj){return obj;};");
            sb.append("function db2json(_d){if(_d&&_d.getClass().getSimpleName()=='DbQuery'){_d=_d.getDataList()};if(_d&&typeof(_d)=='object'){if(_d.getRow){if(_d.getRowCount()==0){return[]};var item=_d.getRow(0);var keys=item.keys().toArray();var ary=[];if(item.count()==1){var list=_d.toArray(0);for(var i in list){ary.push(list[i])}}else{var rows=_d.getRows();for(var j in rows){var m1=rows.get(j);var obj={};for(var i in keys){var k1=keys[i];obj[k1]=m1.get(k1)};ary.push(obj)}};return ary};if(_d.getVariate){var keys=_d.keys().toArray();var obj={};for(var i in keys){var k1=keys[i];obj[k1]=_d.get(k1)};return obj};return _d}else{return _d}};");

            ExecutorFactory.exec(language, sb.toString(),null);
        }
    }

    private static Object eval(String code) throws Exception{
        return ExecutorFactory.exec(language, code,null);
    }

    private static void loadFunc(String name, Fun0Ex<String,Exception> code) throws Exception{
        if(isLoaded(name) == false){
            obj_loaded.add(name);

            ExecutorFactory.exec(language, code.run(),null);
        }
    }

    private static boolean isLoaded(String name){
        return obj_loaded.contains(name);
    }

    private static void delLoaded(String name){
        obj_loaded.remove(name);
    }

    //==================

    protected static void tryInitBlock(String tagName, boolean forUpdate) throws Exception {
        String[] ss = tagName.split("/");

        if(forUpdate) {
            String block_key = "DD_" + ss[0] + "_" + ss[1];
            delLoaded(block_key);
        }

        if ("*".equals(ss[1])) {
            List<BlockModel> list = DbApi.getBlockByTag(ss[0]);
            for (BlockModel block : list) {
                String dd_key = "DD_" + block.tag + "_" + block.name;
                loadFunc(dd_key, () -> RcCompiler.loadBlockAsFunc(block, false));
            }
        } else {
            String dd_key = "DD_" + ss[0] + "_" + ss[1];

            if (isLoaded(dd_key) == false) {
                BlockModel block = DbApi.getBlock(ss[0], ss[1]);
                loadFunc(dd_key, () -> RcCompiler.loadBlockAsFunc(block, false));
            }
        }
    }

    protected static void tryInitScheme(SchemeModel scheme, boolean forUpdate) throws Exception {
        String fun_key = "SR_" + scheme.tag + "_" + scheme.name;

        if(forUpdate) {
            delLoaded(fun_key);
            return;
        }

        if (isLoaded(fun_key) == false) {
            if (scheme.rule_count > 0 || (scheme.hasEvent())) {
                loadFunc(fun_key, () -> RcCompiler.loadSchemeRuleAsFunc(scheme,false));
            }

            if (TextUtils.isEmpty(scheme.related_block) == false) {
                String[] ss = scheme.related_block.split(";");

                for (int i = 0, len = ss.length; i < len; i++) {
                    tryInitBlock(ss[i], false);
                }
            }
        }
    }

    protected static void tryInitModel(String model_tagName, boolean forUpdate) throws Exception {
        if (TextUtils.isEmpty(model_tagName) == false) {
            String[] ss = model_tagName.split("/");
            String fun_key = "MM_" + ss[0] + "_" + ss[1];

            if(forUpdate) {
                delLoaded(fun_key);
                return;
            }

            loadFunc(fun_key, () -> {
                ModelModel mm = DbApi.getModel(ss[0], ss[1]);
                return RcCompiler.loadModelAsFunc(mm, false);
            });
        }
    }

    //
    //
    //
    protected static ScriptObjectMirror buildArgs(ONode args) throws Exception {
        return (ScriptObjectMirror) eval(RcCompiler.args(args.toJson()));
    }

    protected static ScriptObjectMirror buildModel(RubberContext context, String model_tagName) throws Exception{
        //2.4.2.尝试初始函数（已初始化的不再处理）
        RcRunner.tryInitModel(model_tagName, false);

        String code = RcCompiler.modelBuild(context, model_tagName);
        return (ScriptObjectMirror)eval(code);
    }

    protected static ScriptObjectMirror buildSession(RubberContext context) throws Exception{
        return (ScriptObjectMirror)eval(RcCompiler.session());
    }

    //
    //==================
    //


    //运行一个条件
    protected static boolean runCondition(RubberContext context, Condition condition) throws Exception{
        String code = RcCompiler.condition(context, condition);
        return (Boolean)eval(code);
    }
    //运行一个表达式，例：$sex_check(m) 或 $sex_check(m.sex); 或 var x=m.sex + m.cc; return x;
    protected static Object runExpr(RubberContext context, String name) throws Exception{
        String code = RcCompiler.expr(context, name);
        return eval(code);
    }

    //
    // 运行三大组件
    //

    protected static Object runBlock(String block_tagName, ONode args) throws Exception {
        return eval(RcCompiler.blockRun(block_tagName, args));
    }


    protected static void runModel(RubberContext context) throws Exception{
        eval(RcCompiler.modelRun(context));
    }

    protected static void runModelField(RubberContext context, String field) throws Exception{
        eval(RcCompiler.modelFieldRun(context, field));
    }

    public static void runScheme(RubberContext context, String scheme_tagName) throws Exception {
        String[] ss = scheme_tagName.split("/");
        SchemeModel scheme = DbApi.getScheme(ss[0], ss[1]);


        //tryInitScheme(scheme, false);

        runScheme(context, scheme);

    }

    public static void runScheme(RubberContext context, SchemeModel scheme) throws Exception {
        try {
            //尝试初始化
            RcRunner.tryInitScheme(scheme, false);
            RcWarehouse.schemeAdd(scheme);

            //2.4.2.加载数据模型
            context.loadModel(scheme.related_model);

            if (scheme.node_count > 0) {
                Workflow workflow = RcCompiler.loadWorkflow(scheme);
                WorkflowRunner flow = new WorkflowRunner(workflow, new RcWorkflowAdapterImp(context));
                flow.run();

            } else {
                eval(RcCompiler.schemeRun(context, scheme.tag + "/" + scheme.name));
            }
        } catch (Exception ex) {
            throw new Exception("S:" + scheme.tag + "/" + scheme.name + " 运行出错", ex);
        }
    }

    public static void runSchemeFlow(RubberContext context, SchemeModel scheme) throws Exception{
        RcRunner.tryInitScheme(scheme, false);

        //2.4.2.加载数据模型
        context.loadModel(scheme.related_model);

        if (scheme.node_count > 0) {
            Workflow workflow = RcCompiler.loadWorkflow(scheme);
            WorkflowRunner flow = new WorkflowRunner(workflow, new RcWorkflowAdapterImp(context));
            flow.run();
        }
    }

    public static void runSchemeRule(RubberContext context, SchemeModel scheme, String rule) throws Exception{
        RcRunner.tryInitScheme(scheme, false);

        //2.4.2.加载数据模型
        context.loadModel(scheme.related_model);

        eval(RcCompiler.schemeRuleRun(context, scheme.tag + "/" + scheme.name, rule));
    }

    public static void runSchemeEvent(RubberContext context, SchemeModel scheme) throws Exception{
        RcRunner.tryInitScheme(scheme, false);

        //2.4.2.加载数据模型
        context.loadModel(scheme.related_model);

        eval(RcCompiler.schemeEventRun(context, scheme.tag + "/" + scheme.name));
    }
}
