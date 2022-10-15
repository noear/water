package xwater.models.data.water_paas;

import org.noear.wood.annotation.PrimaryKey;
import org.noear.wood.annotation.Table;

/**
 * @author noear 2021/11/3 created
 */
@Table("rubber_block_item")
public class RubberBlockItemDo {
    /**  */
    @PrimaryKey
    public Integer item_id;
    /**  */
    public Integer block_id;
    /**  */
    public String name;
    /**  */
    public String f1;
    /**  */
    public String f2;
    /**  */
    public String f3;
    /**  */
    public String f4;
    /** 最后更新时间 */
    public Long last_updatetime;
}
