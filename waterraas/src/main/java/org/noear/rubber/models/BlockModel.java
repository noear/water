package org.noear.rubber.models;

import org.noear.snack.ONode;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;

public class BlockModel implements IBinder
{
    public int block_id;
    public String tag;
    public String name;
    public String name_display;
    public String related_db;
    public String related_tb;
    public int is_editable;
    public String struct;
    public String app_expr;
    public Date last_updatetime;
    public long counts;

    public void bind(GetHandlerEx s)
    {
        //1.source:数据源
        //
        block_id = s.get("block_id").value(0);
        tag = s.get("tag").value(null);
        name = s.get("name").value(null);
        name_display = s.get("name_display").value(null);
        related_db = s.get("related_db").value(null);
        related_tb = s.get("related_tb").value(null);
        is_editable = s.get("is_editable").value(0);
        struct = s.get("struct").value(null);
        app_expr = s.get("app_expr").value(null);
        last_updatetime = s.get("last_updatetime").value(null);
        counts = s.get("counts").value(0L);
    }

    public IBinder clone()
    {
        return new BlockModel();
    }

    private ONode _cols = null;
    private String _cols_key = null;
    private String _cols_str = null;
    public ONode cols() {
        if(_cols == null) {
            _cols = ONode.load(struct);

            for(String k: _cols.obj().keySet()){
                _cols_key = k;
                break;
            }

            _cols_str = String.join(",", _cols.obj().keySet());
        }

        return _cols;
    }

    public String cols_str(){
        return _cols_str;
    }

    public String cols_key(){
        return _cols_key;
    }
}
