package wateradmin.models.water_tool;

import lombok.Getter;
import org.noear.water.utils.EncryptUtils;
import org.noear.water.utils.Timespan;

import java.util.Date;

/**
 * @author noear
 */
@Getter
public class DetectionModel {

    /**
     *
     */
    public long detection_id;
    /**
     *
     */
    public String tag;
    /**
     * 检测key
     */
    public String key;
    /**
     *
     */
    public String name;
    /**
     *
     */
    public String protocol;
    /**
     *
     */
    public String address;
    /**
     * 0:待检查；1检查中
     */
    public int state;
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
     * 检测间隔时间（s）
     */
    public int check_interval;
    /**
     * 检测异常数量
     */
    public int check_error_num;
    /**
     * 是否为已启用
     */
    public int is_enabled;
    /**
     * 创建时间
     */
    public long gmt_create;
    /**
     * 最后修改时间
     */
    public long gmt_modified;

    public boolean isAlarm() {
        if (check_last_state == 1) {
            return true;
        }

        if (new Timespan(check_last_time).seconds() > (check_interval + 10)) {
            return true;
        }

        return false;
    }


    public String track_md5() {
        return "%7Bmd5%7D" + EncryptUtils.md5(name + "@" + protocol + "://" + address);
    }
}
