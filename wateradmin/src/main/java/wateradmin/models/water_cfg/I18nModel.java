package wateradmin.models.water_cfg;

import lombok.Getter;


@Getter
public class I18nModel {
    /** 国际化id */
    public int row_id;
    /** 分组标签 */
    public String tag;
    /** 捆名 */
    public String bundle;
    /** 语言 */
    public String lang;
    /** 名称 */
    public String name;
    /** 值  */
    public String value;
    /** 禁用 */
    public int is_enabled;
    /** 创建时间 */
    public long gmt_create;
    /** 最后修改时间 */
    public long gmt_modified;
}
