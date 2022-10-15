package xwater.models.data.water;

import org.noear.wood.annotation.PrimaryKey;
import org.noear.wood.annotation.Table;

/**
 * @author noear 2021/11/3 created
 */
@Table("water_tool_synchronous")
public class WaterToolSynchronousDo {
    /**  */
    @PrimaryKey
    public Integer sync_id;
    /**  */
    public String tag;
    /**  */
    public String key;
    /**  */
    public String name;
    /** 0,增量同步；1,更新同步； */
    public Integer type;
    /** 间隔时间（秒） */
    public Integer interval;
    /**  */
    public String target;
    /**  */
    public String target_pk;
    /** 数据源模型 */
    public String source_model;
    /** 同步标识（用于临时存数据） */
    public Long task_tag;
    /**  */
    public String alarm_mobile;
    /**  */
    public String alarm_sign;
    /**  */
    public Integer is_enabled;
    /** 创建时间 */
    public Long gmt_create;
    /** 最后修改时间 */
    public Long gmt_modified;
}
