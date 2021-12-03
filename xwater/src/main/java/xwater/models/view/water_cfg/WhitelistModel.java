package xwater.models.view.water_cfg;

import lombok.Getter;
import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

import java.util.Date;

@Table("water_cfg_whitelist")
@Getter
public class WhitelistModel {
    @PrimaryKey
    public int row_id;
    /** 分组标签 */
    public String tag;
    /** 名单类型 */
    public String type;
    /** 值 */
    public String value;
    /** 备注 */
    public String note;
    /** 是否启用 */
    public int is_enabled;
    /** 更新时间 */
    public Date update_fulltime;
}