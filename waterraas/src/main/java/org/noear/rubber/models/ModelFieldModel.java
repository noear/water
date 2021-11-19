package org.noear.rubber.models;

import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;

public class ModelFieldModel implements IBinder{
    public int field_id;
    public int model_id;
    public String name;
    public String name_display;
    public String args;
    public String expr;
    public Date last_updatetime;

    public void bind(GetHandlerEx s) {
        //1.source:数据源
        //
        field_id = s.get("field_id").value(0);
        model_id = s.get("model_id").value(0);
        name = s.get("name").value(null);
        name_display = s.get("name_display").value(null);
        args = s.get("args").value(null);
        expr = s.get("expr").value(null);
        last_updatetime = s.get("last_updatetime").dateValue(null);

        if (last_updatetime == null) {
            last_updatetime = new Date();
        }
    }

    public IBinder clone() {
        return new ModelFieldModel();
    }
}
