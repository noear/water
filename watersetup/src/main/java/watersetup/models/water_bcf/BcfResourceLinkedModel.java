package watersetup.models.water_bcf;

import org.noear.weed.annotation.Table;

/**
 * @author noear 2021/11/3 created
 */
@Table("bcf_resource_linked")
public class BcfResourceLinkedModel {
    /** 资源ID */
    public Integer rsid;
    /** 连接对象 */
    public Integer lk_objt;
    /** 连接对象ID */
    public Integer lk_objt_id;
    /** 连操操作符(+,-) */
    public String lk_operate;
    /** 操作表达式(预留) */
    public Integer p_express;
}
