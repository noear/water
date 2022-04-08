package wateradmin.models.water_cfg;

import lombok.Getter;

@Getter
public class KeyModel {
    /** 密钥ID */
    public int key_id;
    /** 访问键 */
    public String access_key;
    /** 访问密钥 */
    public String access_secret_key;
    /** 访问密钥盐 */
    public String access_secret_salt;
    /** 分组标签 */
    public String tag;
    /** 标记 */
    public String label;
    /** 描述 */
    public String description;
    /** 是否启用 */
    public int is_enabled;
    /** 创建时间 */
    public long gmt_create;
    /** 最后修改时间 */
    public long gmt_modified;
}
