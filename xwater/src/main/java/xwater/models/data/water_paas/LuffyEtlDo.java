package xwater.models.data.water_paas;

import org.noear.wood.annotation.PrimaryKey;
import org.noear.wood.annotation.Table;

/**
 * @author noear 2021/11/3 created
 */
@Table("luffy_etl")
public class LuffyEtlDo {

    /**  */
    @PrimaryKey
    public Integer etl_id;
    /** 分类标签 */
    public String tag;
    /** 任务名称 */
    public String etl_name;
    /** JSON配置代码 */
    public String code;
    /** 是否启动  */
    public Integer is_enabled;
    /** 是否启用抽取器 */
    public Integer is_extract;
    /** 是否启用加载器 */
    public Integer is_load;
    /** 是否启用转换器 */
    public Integer is_transform;
    /** 0时间；1数值 */
    public Integer cursor_type;
    /** 游标 */
    public Long cursor;
    /** 报警手机号（多个以,隔开） */
    public String alarm_mobile;
    /**  */
    public Integer e_enabled;
    /** 抽取器集群数 */
    public Integer e_max_instance;
    /**  */
    public Long e_last_exectime;
    /**  */
    public Integer t_enabled;
    /** 转换器集群数 */
    public Integer t_max_instance;
    /**  */
    public Long t_last_exectime;
    /**  */
    public Integer l_enabled;
    /** 加载器集群数 */
    public Integer l_max_instance;
    /**  */
    public Long l_last_exectime;
    /** 最后抽取时间 */
    public Long last_extract_time;
    /** 最后加载时间 */
    public Long last_load_time;
    /** 最后转换时间 */
    public Long last_transform_time;
}
