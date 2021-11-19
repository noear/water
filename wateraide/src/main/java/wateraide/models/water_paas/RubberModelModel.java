package wateraide.models.water_paas;

import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

import java.util.Date;

/**
 * @author noear 2021/11/3 created
 */
@Table("rubber_model")
public class RubberModelModel {
    /** 模型ID */
    @PrimaryKey
    public int model_id;
    /** 分类标签 */
    public String tag;
    /** 代号 */
    public String name;
    /** 显示名 */
    public String name_display;
    /** 相关数据库 */
    public String related_db;
    /**  */
    public int field_count;
    /** 构造表达式 */
    public String init_expr;
    /** 调试参数 */
    public String debug_args;
    /** 最后更新时间 */
    public long last_updatetime;
}
