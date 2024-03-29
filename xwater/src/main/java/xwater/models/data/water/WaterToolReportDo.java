package xwater.models.data.water;

import org.noear.wood.annotation.PrimaryKey;
import org.noear.wood.annotation.Table;

/**
 * @author noear 2021/11/3 created
 */
@Table("water_tool_report")
public class WaterToolReportDo {
    /** 简报ID */
    @PrimaryKey
    public Integer row_id;
    /** 分类标签（外部根据标签查询） */
    public String tag;
    /** 查询名称 */
    public String name;
    /** 参数变量 */
    public String args;
    /** 查询代码 */
    public String code;
    /**  */
    public String note;
    /** 创建时间 */
    public Long gmt_create;
    /** 最后修改时间 */
    public Long gmt_modified;
}
