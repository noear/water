package org.noear.rubber.models;

import org.noear.rubber.DbApi;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class SchemeModel implements IBinder
{
    public int scheme_id;
    public String tag;
    public String name;
    public String name_display;
    public String related_model;
    public String event;
    public String related_block;
    public int node_count;
    public int rule_count;
    public int rule_relation;//规则关系（0并且关系，1或者关系）
    public int is_enabled;
    public Date last_updatetime;

    public boolean _is_matched;

    public void bind(GetHandlerEx s)
    {
        //1.source:数据源
        //
        scheme_id = s.get("scheme_id").value(0);
        tag = s.get("tag").value(null);
        name = s.get("name").value(null);
        name_display = s.get("name_display").value(null);
        related_model = s.get("related_model").value(null);
        event = s.get("event").value(null);
        related_block = s.get("related_block").value(null);
        node_count = s.get("node_count").value(0);
        rule_count = s.get("rule_count").value(0);
        rule_relation = s.get("rule_relation").value(0);
        is_enabled = s.get("is_enabled").value(0);
        last_updatetime = s.get("last_updatetime").value(null);
    }

    public IBinder clone()
    {
        return new SchemeModel();
    }

    private List<SchemeRuleModel> _rules;
    public List<SchemeRuleModel> rules() throws SQLException{
        if(_rules == null){
            _rules = DbApi.getSchemeRules(scheme_id,tag,name);
        }

        return _rules;
    }

    private List<SchemeNodeModel> _nodes;
    public List<SchemeNodeModel> nodes() throws SQLException{
        if(_nodes == null){
            _nodes = DbApi.getSchemeNodes(scheme_id,tag,name);
        }

        return _nodes;
    }

    public boolean hasEvent(){
        return event != null && event.length()>4;
    }
}