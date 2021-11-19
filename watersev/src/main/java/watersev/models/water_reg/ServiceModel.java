package watersev.models.water_reg;

import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;

/**
 * Created by noear on 2017/7/28.
 */
public class ServiceModel implements IBinder {
    public long service_id;
    /**
     * md5(name+‘#’+address)
     */
    public String key;
    /**
     *
     */
    public String tag;
    /**
     *
     */
    public String name;
    /**
     * 版本号
     */
    public String ver;
    /**
     *
     */
    public String address;
    /**
     * 源信息
     */
    public String meta;
    /**
     *
     */
    public String alarm_mobile;
    /**
     *
     */
    public String alarm_sign;
    /**
     * 0:待检查；1检查中
     */
    public int state;
    /**
     *
     */
    public String code_location;
    /**
     * 检查方式（0被检查；1自己签到）
     */
    public int check_type;
    /**
     * 状态检查地址
     */
    public String check_url;
    /**
     * 最后检查时间
     */
    public Date check_last_time;
    /**
     * 最后检查状态（0：OK；1：error）
     */
    public int check_last_state;
    /**
     * 最后检查描述
     */
    public String check_last_note;
    /**
     * 检测异常数量
     */
    public int check_error_num;
    /**
     * 是否为不稳定的
     */
    public boolean is_unstable;
    /**
     * 是否为已启用
     */
    public boolean is_enabled;

    @Override
    public void bind(GetHandlerEx s) {
        service_id = s.get("service_id").longValue(0L);
        key = s.get("key").value("");
        tag = s.get("tag").value("");
        name = s.get("name").value(null);
        address = s.get("address").value("");
        meta = s.get("meta").value("");
        alarm_mobile = s.get("alarm_mobile").value("");
        alarm_sign = s.get("alarm_sign").value("");

        state = s.get("state").intValue(0);

        check_url = s.get("check_url").value(null);
        check_type = s.get("check_type").value(0);
        check_error_num = s.get("check_error_num").value(0);
        check_last_time = s.get("check_last_time").value(null);
        check_last_note = s.get("check_last_note").value("");

        is_enabled = s.get("is_enabled").intValue(0) > 0;
        is_unstable = s.get("is_unstable").intValue(0) > 0;

        if (check_last_time == null) {
            check_last_time = new Date();
        }
    }

    @Override
    public IBinder clone() {
        return new ServiceModel();
    }
}
