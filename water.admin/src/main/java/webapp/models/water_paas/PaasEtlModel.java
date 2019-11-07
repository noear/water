package webapp.models.water_paas;

import lombok.Getter;
import org.noear.water.tools.Datetime;
import org.noear.weed.*;

import java.util.*;

@Getter
public class PaasEtlModel implements IBinder
{
    public int etl_id;
    public String tag;
    public String etl_name;
    public String code;
    public int state;
    public int is_enabled;
    public int is_transform;
    public int e_enabled;
    public int e_max_instance;
    public Date e_last_exectime;
    public int t_enabled;
    public int t_max_instance;
    public Date t_last_exectime;
    public int l_enabled;
    public int l_max_instance;
    public Date l_last_exectime;
    public int cursor_type;
    public long cursor;
    public String alarm_mobile;
    public long counts;

    public void bind(GetHandlerEx s)
    {
        //1.source:数据源
        //
        etl_id = s.get("etl_id").value(0);
        tag = s.get("tag").value(null);
        etl_name = s.get("etl_name").value(null);
        code = s.get("code").value(null);
        state = s.get("state").value(0);
        is_enabled = s.get("is_enabled").value(0);
        e_enabled = s.get("e_enabled").value(0);
        t_enabled = s.get("t_enabled").value(0);
        l_enabled = s.get("l_enabled").value(0);
        e_max_instance = s.get("e_max_instance").value(0);
        t_max_instance = s.get("t_max_instance").value(0);
        l_max_instance = s.get("l_max_instance").value(0);
        e_last_exectime = s.get("e_last_exectime").value(null);
        t_last_exectime = s.get("t_last_exectime").value(null);
        l_last_exectime = s.get("l_last_exectime").value(null);
        is_transform = s.get("is_transform").value(0);
        cursor_type = s.get("cursor_type").value(0);
        cursor = s.get("cursor").value(0l);
        alarm_mobile = s.get("alarm_mobile").value(null);
        counts = s.get("counts").value(0L);
    }

    public IBinder clone()
    {
        return new PaasEtlModel();
    }

    public String getStausName(int status) {
        String statusName = "";
        switch (status){
            case 0:statusName = "等待处理";
                break;
            case 1:statusName = "处理中";
                break;
            case 8:statusName = "处理失败";
                break;
            case 9:statusName = "处理成功";
                break;
            default:break;
        }
        return statusName;
    }

    public String getBoolean(int status) {
        if (status == 0) {
            return "否";
        } else {
            return "是";
        }
    }

    public String cursor_str(){
        if(cursor_type==0){
            return new Datetime(cursor).toString("yyyy-MM-dd HH:mm");
        }else {
            return cursor + "";
        }
    }

}