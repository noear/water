package wateraide.models.water_paas;

import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

import java.util.Date;

/**
 * @author noear 2021/11/3 created
 */
@Table("luffy_etl")
public class LuffyEtlModel {

    /**  */
    @PrimaryKey
    public int etl_id;
    /** 分类标签 */
    public String tag;
    /** 任务名称 */
    public String etl_name;
    /** JSON配置代码 */
    public String code;
    /** 是否启动  */
    public int is_enabled;
    /** 是否启用抽取器 */
    public int is_extract;
    /** 是否启用加载器 */
    public int is_load;
    /** 是否启用转换器 */
    public int is_transform;
    /** 0时间；1数值 */
    public int cursor_type;
    /** 游标 */
    public long cursor;
    /** 报警手机号（多个以,隔开） */
    public String alarm_mobile;
    /**  */
    public int e_enabled;
    /** 抽取器集群数 */
    public int e_max_instance;
    /**  */
    public long e_last_exectime;
    /**  */
    public int t_enabled;
    /** 转换器集群数 */
    public int t_max_instance;
    /**  */
    public long t_last_exectime;
    /**  */
    public int l_enabled;
    /** 加载器集群数 */
    public int l_max_instance;
    /**  */
    public long l_last_exectime;
    /** 最后抽取时间 */
    public long last_extract_time;
    /** 最后加载时间 */
    public long last_load_time;
    /** 最后转换时间 */
    public long last_transform_time;
}
