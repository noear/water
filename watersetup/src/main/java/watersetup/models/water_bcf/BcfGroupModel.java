package watersetup.models.water_bcf;

import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

import java.util.Date;

/**
 * @author noear 2021/11/3 created
 */
@Table("bcf_group")
public class BcfGroupModel {
    /** 组ID */
    @PrimaryKey
    public Integer pgid;
    /** 组的父节点ID */
    public Integer p_pgid;
    /** 组的根节点ID */
    public Integer r_pgid;
    /** 组的手工编码 */
    public String pg_code;
    /** 中文名称 */
    public String cn_name;
    /** 英文名称 */
    public String en_name;
    /** 连接地址 */
    public String uri_path;
    /** 级别 */
    public Integer in_level;
    /** 是否为支线 */
    public Boolean is_branch;
    /** 标签 */
    public String tags;
    /** 排序 */
    public Integer order_index;
    /** 是否禁用 */
    public Boolean is_disabled;
    /** 是否显示 */
    public Boolean is_visibled;
    /** 创建时间 */
    public Date create_time;
    /** 更新时间 */
    public Date last_update;
}
