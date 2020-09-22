package org.noear.rubber;

import org.noear.rubber.models.*;
import org.noear.rubber.workflow.Condition;
import org.noear.rubber.workflow.ConditionItem;
import org.noear.rubber.workflow.Workflow;
import org.noear.snack.ONode;
import org.noear.water.WaterClient;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.TextUtils;

import java.sql.SQLException;
import java.util.List;

final class RcCompiler {


    public static String args(String args_json) {

        StringBuilder sb = new StringBuilder();
        sb.append("obj2obj(").append(args_json).append(");");
        return sb.toString();
    }

    public static String session() {
        StringBuilder sb = new StringBuilder();
        sb.append("obj2obj({});");
        return sb.toString();
    }

    //编译表达式->JS执行代码 //表达式，例：$sex_check(m) 或 $sex_check(m.sex); 或 var x=m.sex + m.cc; return x;
    public static String expr(RubberContext context, String expr) { //返回一个 js 代码
        StringBuilder sb = new StringBuilder();
        sb.append("(function(){");
        sb.append(" var c = ").append(context.jsobj()).append(";");
        sb.append(" var m = c.model();");
        if(expr.startsWith("$")){
            sb.append("return ").append(expr.substring(1).replace("/","_")).append(";");
        }else{
            sb.append(expr);
        }
        sb.append("})()");
        return sb.toString();
        /*
        * (()->{var c = ..; var m=..; return xxx(m);})()
        *
        * (()->{var c = ..; var m=..; var xxx = 1; return xxx;})()
        *
        * */
    }

    //编译条件->JS执行代码
    public static String condition(RubberContext context, Condition condition) { //返回一个 js 代码

        StringBuilder sb = new StringBuilder();
        sb.append("return ");
        buildConditionAsJs(sb, condition.items());
        sb.append(";");

        return expr(context, sb.toString());

        /*
        *
        * expr('return m.f1()>1 && m.f2()>fun1() && fun2(m);');
        *
        * */
    }

    public static String blockRun(String block_tagName, ONode args){
        StringBuilder sb  =new StringBuilder();

        sb.append("(function(m){");

        sb.append("return DD_").append(block_tagName.replace("/","_"))
                .append(".scan(m.x,m.cmd);");

        sb.append("})(").append(args.toJson()).append(");");

        return sb.toString();
    }

    public static String modelBuild(RubberContext context, String model_tagName) {

        StringBuilder sb = new StringBuilder();
        sb.append("new MM_").append(model_tagName.replace("/", "_")).append("(").append(context.jsobj()).append(")");
        return sb.toString();
        /*
        * MM_xxx(c)
        * */
    }

    public static String modelRun(RubberContext context){
        StringBuilder sb  =new StringBuilder();
        sb.append("MM_run(").append(context.jsobj()).append(")");
        return sb.toString();
    }

    public static String modelFieldRun(RubberContext context, String field){
        StringBuilder sb  =new StringBuilder();
        sb.append("MM_run_item(").append(context.jsobj()).append(",'").append(field).append("')");
        return sb.toString();
    }

    public static String schemeRun(RubberContext context, String schema_tagName) {
        StringBuilder sb = new StringBuilder();
        sb.append("SR_run(").append(context.jsobj()).append(",").append("SR_").append(schema_tagName.replace("/", "_")).append(")");
        return sb.toString();
    }

    public static String schemeRuleRun(RubberContext context, String schema_tagName, String rule) {
        StringBuilder sb = new StringBuilder();
        if(TextUtils.isEmpty(rule)){
            sb.append("SR_run_rule_all(").append(context.jsobj()).append(",")
                    .append("SR_").append(schema_tagName.replace("/", "_")).append(")");
        }else{
            sb.append("SR_run_rule_one(").append(context.jsobj()).append(",")
                    .append("SR_").append(schema_tagName.replace("/", "_")).append(",")
                    .append("'").append(rule).append("')");
        }

        return sb.toString();
    }

    public static String schemeEventRun(RubberContext context, String schema_tagName) {
        StringBuilder sb = new StringBuilder();
        sb.append("SR_run_event(").append(context.jsobj()).append(",")
                .append("SR_").append(schema_tagName.replace("/", "_")).append(")");

        return sb.toString();
    }


    //编译工作流->java 对象
    public static Workflow schemeFlow(SchemeModel scheme) throws SQLException{
        Workflow workflow = new Workflow();

        List<SchemeNodeModel> nodeList = scheme.nodes();
        nodeList.forEach(n -> {
            workflow.addNode(n.node_key, n.name, n.type, n.prve_key, n.next_key, n.condition, n.tasks);
        });

        return workflow;
    }

    //构建条件JS代码
    private static void buildConditionValCheckAsJs(StringBuilder sb, List<ConditionItem> items){
        StringBuilder sb2 = new StringBuilder();
        items.forEach(c -> {
            String temp = c.left_js();
            if("m".equals(temp)==false && sb2.indexOf(temp)<0) {
                sb2.append("c.v(").append(temp).append(") &&");
            }
        });

        if(sb2.length()>0){
            sb2.insert(0,"return ");
            sb2.deleteCharAt(sb2.length()-1);
            sb2.deleteCharAt(sb2.length()-1);
            sb2.append(";");
        }

        sb.append(sb2);
    }

    private static void buildConditionAsJs(StringBuilder sb, List<ConditionItem> items) {

        items.forEach(c -> {
            //运算符 //=，>，>=，<，<=，L(包含)，F(函数)
            if ("LIKE".equals(c.operator_js())) {
                sb.append(c.left_js()).append(".indexOf(").append(c.right()).append(")>-1");
            } else if ("IN".equals(c.operator_js())) {
                //d-block:: tag/name
                sb.append("DD_").append(c.right().replace("/","_")).append(".scan(").append(c.left_js()).append(")");
            } else {
                sb.append(c.left_js()).append(c.operator_js()).append(c.right());
            }

            //连接符 //'', '&&', '||'
            if (c.connect().length() > 0) {
                sb.append(" ").append(c.connect()).append(" ");
            }
        });
    }

    private static void buildConditionAsSql(StringBuilder sb, List<ConditionItem> items) {
        sb.append(" (");
        items.forEach(c -> {

            //运算符 //=，>，>=，<，<=，L(包含)，F(函数)
            if ("LIKE".equals(c.operator_sql())) {
                sb.append(c.left_sql()).append(" LIKE '%").append(c.right()).append("%'");
            } else if ("IN".equals(c.operator_sql())) {
                //要去掉$号
                sb.append(c.left_sql()).append(" IN ").append(c.right()).append("");
            } else {
                sb.append(c.left_sql()).append(c.operator_sql()).append(c.right());
            }

            //连接符 //'', '&&', '||'
            if (c.connect().length() > 0) {
                if("&&".equals(c.connect())) {
                    sb.append(" AND ");
                }
                else {
                    sb.append(" OR ");
                }
            }
        });
        sb.append(")");
    }

    public static Workflow loadWorkflow(SchemeModel scheme) throws SQLException {
        Workflow workflow = new Workflow();

        scheme.nodes().forEach((n) -> {
            workflow.addNode(n.node_key, n.name, n.type, n.prve_key, n.next_key, n.condition, n.tasks);
        });

        return workflow;
    }

    ///////////////////////////////////////////
    //编译D-Block->JS执行代码
    public static String loadBlockAsFunc(BlockModel block, boolean is_preview) throws SQLException {

        StringBuilder sb = new StringBuilder();

        if(is_preview) {
            sb.append(" /*指标仓库.实例：").append(block.name_display).append("*/ \r\n");
        }

        sb.append("this.DD_").append(block.tag).append("_").append(block.name).append(" = {");

        sb.append("name:\"").append(block.tag).append("/").append(block.name).append("\",");
        sb.append("name_display:\"").append(block.name_display).append("\",");
        sb.append("block_id:").append(block.block_id).append(",");
        sb.append("block_key:\"block_").append(block.block_id).append("\",");
        sb.append("db:\"").append((block.related_db==null?"":block.related_db)).append("\",");
        sb.append("tb:\"").append((block.related_tb==null?"":block.related_tb)).append("\",");
        sb.append("$:function(){");

        if (TextUtils.isEmpty(block.related_db) || TextUtils.isEmpty(block.related_tb)) {

        }else {
            ConfigM cfg = WaterClient.Config.getByTagKey(block.related_db);
            if (cfg.value.indexOf("mysql:") > 0) {
                sb.append("if(!this._$){");
                sb.append("  this._$ = water.db('").append(block.related_db).append("');");
                sb.append("};");
                sb.append("return this._$.table('").append(block.related_tb).append("');");
            } else {
                sb.append("if(!this._$){");
                sb.append("  this._$ = water.rd('")
                        .append(block.related_db).append("',").append(block.related_tb)
                        .append(");");
                sb.append("};");
                sb.append("return this._$;");
            }
        }

        sb.append("},");
        sb.append("_$_scan:function(x,cmd){");
        if(TextUtils.isEmpty(block.app_expr)){
            sb.append("return false;");
        }else {
            sb.append(block.app_expr);
        }
        sb.append("},");
        sb.append("scan:function (x,cmd) {");
        sb.append("  try {");
        sb.append("    return this._$_scan(x,cmd);");
        sb.append("  } catch (err) {");
        sb.append("    throw new Error('D:' + this.name + ' ' + err);");
        sb.append("  }");
        sb.append(" }");

        sb.append("};");

        return sb.toString();
    }

    ///////////////////////////////////////////
    //编译模型->JS执行代码
    public static String loadModelAsFunc(ModelModel model, boolean is_preview) throws SQLException {

        StringBuilder sb = new StringBuilder();

        if (is_preview) {
            sb.append(" /*数据模型.类：").append(model.name_display).append("*/ \r\n");
        }

        sb.append("this.MM_").append(model.tag).append("_").append(model.name).append(" = function(c){");
        sb.append(" this._$build(c);");
        sb.append("};");
        sb.append("this.MM_").append(model.tag).append("_").append(model.name).append(".prototype = {");
        model.fields().forEach(f -> {
            if (TextUtils.isEmpty(f.expr) == false) {
                sb.append("_$_").append(f.name).append(":function(){").append(f.expr).append("},");
            }


            sb.append(f.name).append(":function(){");
            if (is_preview) {
                sb.append(" /*").append(f.name_display).append("*/ ");
            }

            /*
            * xxxxx：公开字段  //会记录值，会自动缓存
            * _xxxx：私有字段  //不记录值，会自动缓存 //注：调试时会记录值，调用_$c时不自动缓存
            * _$xxx：私密字段  //不记录值，不自动缓存
            * */

            if (TextUtils.isEmpty(f.expr) == false) {
                if (f.expr.indexOf("this._$c") >= 0) {
                    if (f.name.startsWith("_$") || f.expr.indexOf(".model(") > 0) {
                        sb.append("return this._$_").append(f.name).append("();");
                    } else {
                        sb.append("return this._").append(f.name).append("=this._$md.").append(f.name).append("=this._$_").append(f.name).append("();");
                    }
                } else {
                    if(f.name.startsWith("_$")){
                        sb.append("return this._$_").append(f.name).append("();");
                    }else if (f.name.startsWith("_")) {
                        sb.append("if(!this._$mx.").append(f.name).append("){");
                        sb.append("  this._$mx.").append(f.name).append("=1;");
                        sb.append("  this._").append(f.name).append("=this._$_").append(f.name).append("();");
                        sb.append("  if(this._$is_debug){");
                        sb.append("    this._$md.").append(f.name).append("=this._").append(f.name).append(";");
                        sb.append("  }");
                        sb.append("};");
                        sb.append("return this._").append(f.name).append(";");
                    } else {
                        sb.append("if(!this._$mx.").append(f.name).append("){");
                        sb.append("  this._$mx.").append(f.name).append("=1;");
                        sb.append("  this._").append(f.name).append("=this._$md.").append(f.name).append("=this._$_").append(f.name).append("();");
                        sb.append("};");
                        sb.append("return this._").append(f.name).append(";");
                    }
                }

            } else {
                sb.append("return this._$md.").append(f.name).append("= this._").append(f.name).append(";");
            }

            sb.append("},");
        });

        sb.append("_$bind:function(d){");
        sb.append("  if(d){");
        sb.append("    for(var k in d){this['_'+k] = d[k];};");
        sb.append("  };");
        sb.append("},");

        sb.append("_$build:function(c){");
        sb.append("  this._$md = {};");
        sb.append("  this._$mx = {};");
        sb.append("  this._$c = c;");

        sb.append("  this._$bind(c.args());");
        sb.append("  if(this._$init){");
        sb.append("    var d = db2json(this._$init());");
        sb.append("    this._$bind(d);");
        sb.append("  };");
        sb.append("}");

        if (TextUtils.isEmpty(model.init_expr) == false) {
            sb.append(",_$init:function(){")
                    .append(model.init_expr)
                    .append("}");
        }

        sb.append("};");

        return sb.toString();
    }

    ////编译替则->JS执行代码
    public static String loadSchemeRuleAsFunc(SchemeModel scheme,boolean is_preview) throws SQLException {

        List<SchemeRuleModel> ruleList = scheme.rules();

        scheme.rule_count = ruleList.size();


        StringBuilder sb = new StringBuilder();

        if(is_preview) {
            sb.append(" /*计算方案.实例：").append(scheme.name_display).append("*/ \r\n");
        }

        sb.append("this.SR_").append(scheme.tag).append("_").append(scheme.name).append(" = {");
        sb.append("name:\"").append(scheme.tag).append("/").append(scheme.name.replace("\"","\'")).append("\",");
        sb.append("name_display:\"").append(scheme.name_display.replace("\"","\'")).append("\",");
        sb.append("total:").append(scheme.rule_count).append(",");
        sb.append("relation:").append(scheme.rule_relation).append(",");
        sb.append("model:\"").append(scheme.related_model).append("\",");

        sb.append("rule:{");
        ruleList.forEach(r -> {
            RcWarehouse.ruleAdd(r);


            sb.append("r").append(r.rule_id).append(":{");

            if(is_preview){
                sb.append(" /*").append(r.name_display).append("*/ ");
            }

            sb.append("id:").append(r.rule_id).append(",");
            sb.append("check:function(c,m){");
            buildConditionValCheckAsJs(sb,r.items());
            sb.append("},");
            sb.append("test:function(c,m){");

            sb.append("return ");
            buildConditionAsJs(sb, r.items());
            sb.append(";}");

            sb.append("},");
        });

        if(scheme.rule_count>0){
            sb.deleteCharAt(sb.length()-1);
        }
        sb.append("},");
        sb.append("event:function(c,m,sm){");
        if (TextUtils.isEmpty(scheme.event) == false) {
            sb.append(scheme.event);
        }
        sb.append("}");

        sb.append("};");

        return sb.toString();
    }

    ////编译替则->JS执行代码
    public static String loadSchemeRuleAsTsql(SchemeModel scheme, ModelModel model, int limit) throws Exception {

        List<SchemeRuleModel> ruleList = scheme.rules();

        scheme.rule_count = ruleList.size();

        StringBuilder sb = new StringBuilder();
        if (scheme.rule_count > 0) {
            sb.append("SELECT ");

            model.fields().forEach((f) -> {
                if(f.name.indexOf(".")>0 || f.name.indexOf(" ")>0){//例：a.x 或 x as b
                    sb.append(f.name).append(" AS ");
                }
                else{
                    sb.append("m.`").append(f.name).append("` AS ");
                }
                sb.append(" `").append(f.name_display).append("`,");
            });

            sb.deleteCharAt(sb.length() - 1);


            if (TextUtils.isEmpty(model.init_expr) == false && model.init_expr.indexOf("return ") < 0) {
                String init_expr_up = model.init_expr.toUpperCase();

                if(init_expr_up.indexOf("SELECT ")>=0){
                    sb.append(" FROM ");
                    sb.append("(").append(model.init_expr).append(" ) m");// )前一定要加空隔，方便检查代码
                }else {
                    sb.append(" ").append(model.init_expr);
                }

            } else {
                sb.append(" FROM ");
                if(model.name.indexOf(" ")>0){
                    sb.append(model.name);
                }else {
                    sb.append(model.name).append(" m");
                }
            }

            if (scheme.rule_relation > 0) {
                sb.append(" WHERE 1<>1"); //or
            } else {
                sb.append(" WHERE 1=1");//and
            }

            ruleList.forEach(r -> {
                if (r.items().size() > 0) {
                    if (scheme.rule_relation > 0) {
                        sb.append(" OR");
                    } else {
                        sb.append(" AND");
                    }

                    buildConditionAsSql(sb, r.items());
                }
            });

            if (limit > 0) {
                sb.append(" LIMIT ").append(limit);
            }
        }

        return sb.toString();
    }
}
