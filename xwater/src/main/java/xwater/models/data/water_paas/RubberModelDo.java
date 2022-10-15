package xwater.models.data.water_paas;

import org.noear.wood.annotation.PrimaryKey;
import org.noear.wood.annotation.Table;

/**
 * @author noear 2021/11/3 created
 */
@Table("rubber_model")
public class RubberModelDo {
    /** 模型ID */
    @PrimaryKey
    public Integer model_id;
    /** 分类标签 */
    public String tag;
    /** 代号 */
    public String name;
    /** 显示名 */
    public String name_display;
    /** 相关数据库 */
    public String related_db;
    /**  */
    public Integer field_count;
    /** 构造表达式 */
    public String init_expr;
    /** 调试参数 */
    public String debug_args;
    /** 最后更新时间 */
    public Long last_updatetime;
}
