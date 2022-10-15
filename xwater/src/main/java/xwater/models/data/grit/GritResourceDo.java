package xwater.models.data.grit;

import lombok.Getter;
import org.noear.wood.annotation.PrimaryKey;
import org.noear.wood.annotation.Table;

import java.io.Serializable;

/**
 * 资源。分3个概念：实体，分组，空间
 *
 * @author noear
 * @since 1.0
 */
@Table("grit_resource")
@Getter
public class GritResourceDo implements Serializable {
    /** 资源ID */
    @PrimaryKey
    public Long resource_id;
    /** 资源父ID */
    public Long resource_pid;
    /** 资源空间ID */
    public Long resource_sid;
    /** 资源类型(0:entity, 1:group, 2:namespace) */
    public Integer resource_type;
    /** 资源代码(例，user:del) */
    public String resource_code;
    /** 显示名 */
    public String display_name;
    /** 排序值 */
    public Integer order_index;
    /** 链接地址(例，/user/add) */
    public String link_uri;
    /** 链接目标 */
    public String link_target;
    /** 链接标签(用,隔开) */
    public String link_tags;
    /** 图标地址 */
    public String icon_uri;
    /** 备注 */
    public String remark;
    /** 节点级别 */
    public Integer level;
    /** 属性(kv) */
    public String attrs;
    /** 是否全屏 */
    public Boolean is_fullview;
    /** 是否可见（可见为页面，不可见为操作） */
    public Boolean is_visibled;
    /** 是否禁用 */
    public Boolean is_disabled;
    /** 创建时间 */
    public Long gmt_create;
    /** 更新时间 */
    public Long gmt_modified;
}
