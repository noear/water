package wateraide.models.water_bcf;

import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

/**
 * @author noear 2021/11/3 created
 */
@Table("bcf_config")
public class BcfConfigModel {
    /** 名称 */
    @PrimaryKey
    public String name;
    /** 值 */
    public String value;
    /** 备注 */
    public String note;
}
