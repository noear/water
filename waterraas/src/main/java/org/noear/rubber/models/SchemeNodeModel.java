package org.noear.rubber.models;

import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;

public class SchemeNodeModel implements IBinder
{
    public int node_id;
    public String node_key;
    public int scheme_id;
    public int type;
    public String name;
    public String prve_key;
    public String next_key;
    public String condition;
    public String tasks;
    public String actor;
    public Date last_updatetime;

    public void bind(GetHandlerEx s)
    {
        //1.source:数据源
        //
        node_id = s.get("node_id").value(0);
        node_key = s.get("node_key").value(null);
        scheme_id = s.get("scheme_id").value(0);
        type = s.get("type").value(0);
        name = s.get("name").value(null);
        prve_key = s.get("prve_key").value(null);
        next_key = s.get("next_key").value(null);
        condition = s.get("condition").value(null);
        tasks = s.get("tasks").value(null);
        actor = s.get("actor").value(null);
        last_updatetime = s.get("last_updatetime").dateValue(null);

        if (last_updatetime == null) {
            last_updatetime = new Date();
        }
    }

    public IBinder clone()
    {
        return new SchemeNodeModel();
    }
}
