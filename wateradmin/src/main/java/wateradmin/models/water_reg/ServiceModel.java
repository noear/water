package wateradmin.models.water_reg;

import lombok.Getter;
import lombok.Setter;
import org.noear.water.utils.EncryptUtils;
import org.noear.water.utils.Timespan;
import org.noear.weed.*;
import org.noear.water.utils.TextUtils;

import java.util.*;

/// <summary>
/// 生成:2017/12/20 02:44:02
/// 
/// </summary>
@Getter
@Setter
public class ServiceModel implements IBinder {
    public long service_id;
    public String tag;
    public String key;
    public String name;
    public String ver;
    public String address;
    public String port;
    public String note;
    public String alarm_mobile;
    public String alarm_sign;
    public int state;
    public String code_location;
    public int check_type;
    public String check_url;
    public Date check_last_time;
    public int check_last_state;
    public String check_last_note;
    public int is_enabled;

    public boolean isAlarm() {
        if (check_last_state == 1)
            return true;

        if (new Timespan(check_last_time).seconds() >= 8) {
            return true;
        }

        return false;
    }

    public void bind(GetHandlerEx s) {
        //1.source:数据源
        //
        service_id = s.get("service_id").longValue(0L);
        tag = s.get("tag").value(null);
        key = s.get("key").value(null);
        name = s.get("name").value(null);
        ver = s.get("ver").value(null);
        address = s.get("address").value(null);
        note = s.get("note").value(null);
        alarm_mobile = s.get("alarm_mobile").value(null);
        alarm_sign = s.get("alarm_sign").value(null);
        state = s.get("state").intValue(0);
        code_location = s.get("code_location").value(null);
        check_type = s.get("check_type").value(0);
        check_url = s.get("check_url").value(null);
        check_last_time = s.get("check_last_time").dateValue(null);
        check_last_state = s.get("check_last_state").value(0);
        check_last_note = s.get("check_last_note").value(null);
        is_enabled = s.get("is_enabled").value(0);

        if(check_last_time == null){
            check_last_time = new Date();
        }

        if (!TextUtils.isEmpty(address)) {
            if (address.contains(":")) {
                port = address.substring(address.indexOf(":") + 1);
            }
        }
    }

    public IBinder clone() {
        return new ServiceModel();
    }

    public String service_md5() {
        return "%7Bmd5%7D" + EncryptUtils.md5(name + "@" + address);
    }
}