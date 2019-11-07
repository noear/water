package webapp.models.water;

import lombok.Data;
import org.noear.weed.*;
import java.util.*;

@Data
public class ReportModel{

    /** 查询ID */
    public int id;
    /** 分类标签（外部根据标签查询） */
    public String tag;
    /** 查询名称 */
    public String name;
    /** 查询代码 */
    public String code;
    public String note;
    public String args;
    /**  */
    public Date create_fulltime;

}