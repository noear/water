package wateradmin.models.water_paas;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import lombok.Getter;
import org.noear.weed.*;
import org.noear.water.utils.TextUtils;

import java.util.*;

/// <summary>
/// 生成:2018/06/12 03:49:53
/// 
/// </summary>
@Getter
public class RebberBlockModel implements IBinder
{
    public int block_id;
    public String tag;
    public String name;
    public String name_display;
    public String note;
    public String related_db;
    public String related_tb;
    public int is_editable;
    public String struct;
    public String app_expr;
    public Date last_updatetime;

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        block_id = s.get("block_id").value(0);
        tag = s.get("tag").value(null);
        name = s.get("name").value(null);
        name_display = s.get("name_display").value(null);
        note = s.get("note").value(null);
        related_db = s.get("related_db").value(null);
        related_tb = s.get("related_tb").value(null);
        is_editable = s.get("is_editable").value(0);
        struct = s.get("struct").value(null);
        app_expr = s.get("app_expr").value(null);
        last_updatetime = s.get("last_updatetime").dateValue(null);

        if (last_updatetime == null) {
            last_updatetime = new Date();
        }
	}
	
	public IBinder clone()
	{
		return new RebberBlockModel();
	}

	private transient JSONObject _cols = null;
	private transient String _cols_key = null;
	private transient String _cols_str = null;
    public JSONObject cols() {
        if(_cols == null) {
            if(TextUtils.isEmpty(struct)){
                _cols = new JSONObject();
            }else {
                _cols = JSON.parseObject(struct, Feature.OrderedField);

                for (String k : _cols.keySet()) {
                    _cols_key = k;
                    break;
                }
            }

            _cols_str = String.join(",", cols().keySet());
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