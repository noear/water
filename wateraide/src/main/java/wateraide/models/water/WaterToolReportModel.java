package wateraide.models.water;

import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

import java.util.Date;

/**
 * @author noear 2021/11/3 created
 */
@Table("water_tool_report")
public class WaterToolReportModel {
    /** 简报ID */
    @PrimaryKey
    public int row_id;
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
    public Date create_fulltime;
}
