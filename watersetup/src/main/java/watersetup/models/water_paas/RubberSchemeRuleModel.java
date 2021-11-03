package watersetup.models.water_paas;

import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

import java.util.Date;

/**
 * @author noear 2021/11/3 created
 */
@Table("rubber_scheme_rule")
public class RubberSchemeRuleModel {
    /** 规则ID */
    @PrimaryKey
    public int rule_id;
    /** 方案ID */
    public int scheme_id;
    /** 显示名 */
    public String name_display;
    /** 评估建议(0无,1交易放行,2审慎审核,3阻断交易) */
    public int advice;
    /** 评估分值 */
    public int score;
    /** 排序 */
    public int sort;
    /** 条件表达式（m.user_day(30),>,15,&&;left,op,right,ct） */
    public String expr;
    /**  */
    public String expr_display;
    /**  */
    public String event;
    /** 状态，(0：禁用、1：启用) */
    public int is_enabled;
    /** 最后更新时间 */
    public Date last_updatetime;
}
