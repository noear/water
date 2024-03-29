package xwater.models.data.water;

import lombok.Getter;
import org.noear.wood.annotation.PrimaryKey;
import org.noear.wood.annotation.Table;

@Table("water_cfg_whitelist")
@Getter
public class WaterCfgWhitelistDo {
    @PrimaryKey
    public Integer row_id;
    /** 分组标签 */
    public String tag;
    /** 名单类型 */
    public String type;
    /** 值 */
    public String value;
    /** 备注 */
    public String note;
    /** 是否启用 */
    public Integer is_enabled;
    /** 创建时间 */
    public Long gmt_create;
    /** 更新时间 */
    public Long gmt_modified;
}