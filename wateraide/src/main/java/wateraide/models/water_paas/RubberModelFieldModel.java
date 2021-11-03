package wateraide.models.water_paas;

import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

import java.util.Date;

/**
 * @author noear 2021/11/3 created
 */
@Table("rubber_model_field")
public class RubberModelFieldModel {
    /** 字段ID */
    @PrimaryKey
    public Integer field_id;
    /** 所属的模型ID */
    public Integer model_id;
    /** 字段名称 */
    public String name;
    /** 显示名 */
    public String name_display;
    /** 字段动态生成代码 */
    public String expr;
    /** 字段 */
    public String note;
    /** 最后更新时间 */
    public Date last_updatetime;
    /** 是否是主键 */
    public Integer is_pk;
}
