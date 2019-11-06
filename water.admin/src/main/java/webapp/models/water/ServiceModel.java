package webapp.models.water;

import lombok.Getter;
import lombok.Setter;
import org.noear.water.tools.TextUtils;
import org.noear.water.tools.Timespan;
import org.noear.weed.*;

import java.util.*;

/// <summary>
/// 生成:2017/12/20 02:44:02
/// 
/// </summary>
@Getter
@Setter
public class ServiceModel implements IBinder
{
    public int service_id;
    public String key;
    public String name;
    public String ver;
    public String address;
    public String port;
    public String note;
    public String alarm_mobile;
    public String alarm_sign;
    public int state;
    public int check_type;
    public String check_url;
    public Date check_last_time;
    public int check_last_state;
    public String check_last_note;
    public int is_enabled;

    public boolean isAlarm(){
        if(check_last_state==1)
            return true;

        if(new Timespan(check_last_time).seconds()>=8){
            return true;
        }

        return false;
    }

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        service_id = s.get("service_id").value(0);
        key = s.get("key").value(null);
        name = s.get("name").value(null);
        ver = s.get("ver").value(null);
        address = s.get("address").value(null);
        note = s.get("note").value(null);
        alarm_mobile = s.get("alarm_mobile").value(null);
        alarm_sign = s.get("alarm_sign").value(null);
        state = s.get("state").intValue(0);
        check_type = s.get("check_type").value(0);
        check_url = s.get("check_url").value(null);
        check_last_time = s.get("check_last_time").value(null);
        check_last_state = s.get("check_last_state").value(0);
        check_last_note = s.get("check_last_note").value(null);
        is_enabled = s.get("is_enabled").value(0);

        if (!TextUtils.isEmpty(address)) {
            if (address.contains(":")) {
                port = address.substring(address.indexOf(":") + 1);
            }
        }
	}
	
	public IBinder clone()
	{
		return new ServiceModel();
	}
}