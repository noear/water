package wateraide.models.water_paas;

import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

/**
 * @author noear 2021/11/3 created
 */
@Table("rubber_scheme_node_design")
public class RubberSchemeNodeDesignModel {
    /**  */
    @PrimaryKey
    public int scheme_id;
    /**  */
    public String details;
    /** 最后更新时间 */
    public long last_updatetime;
}
