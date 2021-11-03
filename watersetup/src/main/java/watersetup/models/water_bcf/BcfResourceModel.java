package watersetup.models.water_bcf;

import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

import java.util.Date;

/**
 * @author noear 2021/11/3 created
 */
@Table("bcf_resource")
public class BcfResourceModel {
    /**  */
    @PrimaryKey
    public int rsid;
    /** 资源手工代码 */
    public String rs_code;
    /** 中文名称 */
    public String cn_name;
    /** 英文名称 */
    public String en_name;
    /** 连接地址 */
    public String uri_path;
    /** 连接目标 */
    public String uri_target;
    /** 图标地址 */
    public String ico_path;
    /** 排序值 */
    public int order_index;
    /** 备注 */
    public String note;
    /** 标签 */
    public String tags;
    /** 是否禁用（默认否） */
    public boolean is_disabled;
    /** 创建时间 */
    public Date create_time;
    /** 最后更新时间 */
    public Date last_update;
}
