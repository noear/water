package wateradmin.models.water_cfg;

import lombok.Getter;

@Getter
public class KeyModel {
    /** 应用ID */
    public int row_id;
    /** 应用标识-guid */
    public String access_key;
    /** 应用密钥 */
    public String access_secret_key;
    /** 应用密钥盐 */
    public String access_secret_salt;
    /** 分组标签 */
    public String tag;
    /** 名称 */
    public String name;
    /** 描述 */
    public String description;
    /** 是否启用 */
    public int is_enabled;
    /** 创建时间 */
    public long gmt_create;
    /** 最后修改时间 */
    public long gmt_modified;
}
