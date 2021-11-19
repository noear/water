package org.noear.rubber.models;

import org.noear.rubber.DbApi;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class ModelModel implements IBinder
{
    public int model_id;
    public String tag;
    public String name;
    public String name_display;
    public String related_db;
    public String init_expr;
    public Date last_updatetime;

    public void bind(GetHandlerEx s)
    {
        //1.source:数据源
        //
        model_id = s.get("model_id").value(0);
        tag = s.get("tag").value(null);
        name = s.get("name").value(null);
        name_display = s.get("name_display").value(null);
        related_db = s.get("related_db").value(null);
        init_expr = s.get("init_expr").value(null);
        last_updatetime = s.get("last_updatetime").dateValue(null);

        if (last_updatetime == null) {
            last_updatetime = new Date();
        }
    }

    public IBinder clone()
    {
        return new ModelModel();
    }

    private List<ModelFieldModel> _fields;
    public List<ModelFieldModel> fields() throws SQLException{
        if(_fields == null){
            _fields = DbApi.getModelFields(model_id, tag,name);
        }

        return _fields;
    }
}
