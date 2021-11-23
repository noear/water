package org.noear.rubber.workflow;

import org.noear.snack.ONode;
import org.noear.water.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 条件项（结构：左侧,操作符,右侧,连接符;...）
 * */
public class ConditionItem {
    protected String _left; //左侧
    protected String _operator;//运算符 //=，>，>=，<，<=，L(包含)，F(函数)
    protected String _right; //右则
    protected String _connect; //连接符 //'', '||', '&&'


    public String left_js() {
        if ("m".equals(_left) == false) {
            return "m." + _left + "()";
        } else {
            return _left;
        }
    }

    public String left_sql() {
        if (_left.indexOf(".") > 0) {
            return _left.split(" ")[0];
        } else {
            return "m." + _left.split(" ")[0];
        }
    }
    public String operator_js(){
        if("=".equals(_operator)){
            return "==";
        }else{
            return  _operator;
        }
    }

    public String operator_sql(){
        if("==".equals(_operator)){
            return "=";
        }else{
            return  _operator;
        }
    }

    public String right(){return _right;} //右则
    public String connect(){return _connect;} //连接符 //'', '||', '&&'




    public static List<ConditionItem> parse (String expr){
        List<ConditionItem> list = null;

        if(TextUtils.isEmpty(expr) == false) {
            list = new ArrayList<>();

            ONode oExpr = ONode.load(expr);

            ONode d = null;
            for(String k : oExpr.obj().keySet()){
                d = oExpr.get(k);
                ConditionItem cond = new ConditionItem();

                cond._left = d.get("l").getString();

                if(TextUtils.isEmpty(cond._left)){
                    continue;
                }

                cond._operator = d.get("op").getString();
                cond._right = d.get("r").getString();
                cond._connect = d.get("ct").getString();

                if("=".equals(cond._operator)){
                    cond._operator = "==";
                }

                list.add(cond);
            }
        }

        return list;
    }
}
