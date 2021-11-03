package wateraide.models.water_bcf;

import org.noear.weed.annotation.Table;

/**
 * @author noear 2021/11/3 created
 */
@Table("bcf_opsx")
public class BcfOpsxModel {
    /** 连接对象 */
    public Integer lk_objt;
    /** 连接对象ID */
    public Integer lk_objt_id;
    /** 标签 */
    public String tags;
    /** JSON值  */
    public String opsx;
}
