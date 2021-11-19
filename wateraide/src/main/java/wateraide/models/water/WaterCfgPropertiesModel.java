package wateraide.models.water;

import lombok.Getter;
import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

import java.util.Date;

@Table("water_cfg_properties")
@Getter
public class WaterCfgPropertiesModel {
    /**  */
    @PrimaryKey
    public Integer row_id;
    /** 分组标签 */
    public String tag;
    /** 属性key */
    public String key;
    /** 类型：0:未知，1:数据库；2:Redis；3:MangoDb；4:Memcached */
    public Integer type;
    /** 属性值 */
    public String value;
    /**  */
    public String edit_mode;
    /** 是否可编辑 */
    public Boolean is_editable;
    /** 是否启用 */
    public Integer is_enabled;
    /** 创建时间 */
    public Long gmt_create;
    /** 最后修改时间 */
    public Long gmt_modified;

}