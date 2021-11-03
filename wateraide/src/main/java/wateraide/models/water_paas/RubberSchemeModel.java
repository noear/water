package wateraide.models.water_paas;

import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

import java.util.Date;

/**
 * @author noear 2021/11/3 created
 */
@Table("rubber_scheme")
public class RubberSchemeModel {
    /** 方案ID */
    @PrimaryKey
    public Integer scheme_id;
    /** 分类标签 */
    public String tag;
    /** 代号 */
    public String name;
    /** 显示名 */
    public String name_display;
    /** 相关模型(tag/name) */
    public String related_model;
    /** 关联模型ID */
    public Integer related_model_id;
    /** 关联模型显示名 */
    public String related_model_display;
    /** 引用函数 */
    public String related_block;
    /** 调试参数 */
    public String debug_args;
    /** 事件 */
    public String event;
    /** 下属工作流节点数据 */
    public Integer node_count;
    /** 下属规则数量 */
    public Integer rule_count;
    /** 规则关系（0并且关系，1或者关系） */
    public Integer rule_relation;
    /** 是否启用 */
    public Integer is_enabled;
    /** 最后更新时间 */
    public Date last_updatetime;
}
