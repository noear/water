package xwater.models.data.water_paas;

import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

/**
 * @author noear 2021/11/3 created
 */
@Table("luffy_file")
public class LuffyFileDo {
    /** 文件ID */
    @PrimaryKey
    public Integer file_id;
    /** 文件类型(0:api, 1:pln, 2:tml) */
    public Integer file_type;
    /** 分组村签 */
    public String tag;
    /** 标记 */
    public String label;
    /** 文件路径 */
    public String path;
    /** 排列（小的排前） */
    public Integer rank;
    /** 是否静态 */
    public Boolean is_staticize;
    /** 是否可编辑 */
    public Boolean is_editable;
    /** 是否禁用 */
    public Boolean is_disabled;
    /** 排除导入 */
    public Boolean is_exclude;
    /** 连接到 */
    public String link_to;
    /** 编辑模式 */
    public String edit_mode;
    /** 内容类型 */
    public String content_type;
    /** 内容 */
    public String content;
    /** 备注 */
    public String note;
    /** 计划状态 */
    public Integer plan_state;
    /** 计划开始执行时间 */
    public Long plan_begin_time;
    /** 计划最后执行时间 */
    public Long plan_last_time;
    /** 计划最后执行时间长度 */
    public Long plan_last_timespan;
    /** 计划下次执行时间戳 */
    public Long plan_next_time;
    /** 计划执行间隔 */
    public String plan_interval;
    /** 计划执行最多次数 */
    public Integer plan_max;
    /** 计划执行累计次数 */
    public Integer plan_count;
    /** 创建时间 */
    public Long create_fulltime;
    /** 更新时间 */
    public Long update_fulltime;
    /** 安全名单 */
    public String use_whitelist;
}
