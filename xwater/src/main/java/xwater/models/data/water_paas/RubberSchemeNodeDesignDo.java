package xwater.models.data.water_paas;

import org.noear.wood.annotation.PrimaryKey;
import org.noear.wood.annotation.Table;

/**
 * @author noear 2021/11/3 created
 */
@Table("rubber_scheme_node_design")
public class RubberSchemeNodeDesignDo {
    /**  */
    @PrimaryKey
    public Integer scheme_id;
    /**  */
    public String details;
    /** 最后更新时间 */
    public Long last_updatetime;
}
