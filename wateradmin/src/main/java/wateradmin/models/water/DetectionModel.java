package wateradmin.models.water;

/**
 * @author noear
 */
public class DetectionModel {

    /**  */
    public long detection_id;
    /**  */
    public String tag;
    /** 检测key */
    public String key;
    /**  */
    public String name;
    /**  */
    public String protocol;
    /**  */
    public String address;
    /**  */
    public String alarm_mobile;
    /**  */
    public String alarm_sign;
    /** 0:待检查；1检查中 */
    public int state;
    /** 检查方式（0被检查；1自己签到） */
    public int check_type;
    /** 最后检查时间 */
    public long check_last_time;
    /** 最后检查状态（0：OK；1：error） */
    public int check_last_state;
    /** 最后检查描述 */
    public String check_last_note;
    /** 检测异常数量 */
    public int check_error_num;
    /** 是否为已启用 */
    public int is_enabled;
    /** 创建时间 */
    public long gmt_create;
    /** 最后修改时间 */
    public long gmt_modified;

}
