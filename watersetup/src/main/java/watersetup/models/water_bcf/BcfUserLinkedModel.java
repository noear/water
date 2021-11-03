package watersetup.models.water_bcf;

import org.noear.weed.annotation.Table;

/**
 * @author noear 2021/11/3 created
 */
@Table("bcf_user_linked")
public class BcfUserLinkedModel {
    /** 内部用户ID */
    public Integer puid;
    /** 连接对象 */
    public Integer lk_objt;
    /** 连接对象ID */
    public Integer lk_objt_id;
    /** 连接操作符（+,-） */
    public String lk_operate;

}
