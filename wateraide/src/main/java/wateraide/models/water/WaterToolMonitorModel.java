package wateraide.models.water;

import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

/**
 * @author noear 2021/11/3 created
 */
@Table("water_tool_monitor")
public class WaterToolMonitorModel {
    /**  */
    @PrimaryKey
    public int monitor_id;
    /**  */
    public String tag;
    /** 监视项目key */
    public String key;
    /** 监视项目名称 */
    public String name;
    /** 监视类型（0:数据预警；1:数据简报） */
    public int type;
    /** 数据源模型脚本 */
    public String source_query;
    /** 规则（输入m:{d:{},tag:''}） */
    public String rule;
    /** 监视标签 */
    public String task_tag;
    /** 监视标签产生的表达式 */
    public String task_tag_exp;
    /** 报警手机号（多个以,隔开） */
    public String alarm_mobile;
    /** 报警信息产生的表达式 */
    public String alarm_exp;
    /** 报警次数 */
    public Integer alarm_count;
    /** 报警签名 */
    public String alarm_sign;
    /** 是否启用 */
    public Integer is_enabled;
}
