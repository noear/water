package watersetup.models.water_paas;

import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

import java.util.Date;

/**
 * @author noear 2021/11/3 created
 */
@Table("rubber_block_item")
public class RubberBlockItemModel {
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
    /**  */
    public Date last_updatetime;
}
