package org.noear.rubber.models;

import org.noear.rubber.workflow.ConditionItem;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;
import java.util.List;

public class SchemeRuleModel implements IBinder
{
    public int rule_id;
    public int scheme_id;
    public String group;
    public String name_display;
    public int advice;
    public int score;
    public String expr;
    public Date last_updatetime;

    public void bind(GetHandlerEx s)
    {
        //1.source:数据源
        //
        rule_id = s.get("rule_id").value(0);
        scheme_id = s.get("scheme_id").value(0);
        group = s.get("group").value(null);
        name_display = s.get("name_display").value(null);
        advice = s.get("advice").value(0);
        score = s.get("score").value(0);
        expr = s.get("expr").value(null);
        last_updatetime = s.get("last_updatetime").dateValue(null);

        if (last_updatetime == null) {
            last_updatetime = new Date();
        }

        _items = ConditionItem.parse(expr);
    }

    public IBinder clone()
    {
        return new SchemeRuleModel();
    }

    ////////////////////
    private List<ConditionItem> _items = null;
    public boolean isEmpty(){
        return _items == null || _items.size()==0;
    }
    public List<ConditionItem> items(){return _items;}
}
