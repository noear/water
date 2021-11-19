package wateraide.models.water;

import lombok.Getter;
import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

import java.util.Date;

/**
 * @author noear 2021/11/12 created
 */
@Table("water_cfg_gateway")
@Getter
public class WaterCfgGatewayModel {
    /** 网关id */
    @PrimaryKey
    public int gateway_id;
    /** 标签 */
    public String tag;
    /** 名称 */
    public String name;
    /** 代理 */
    public String agent;
    /** 策略 */
    public String policy;
    /**  */
    public int is_enabled;
    /** 创建时间 */
    public long gmt_create;
    /** 最后修改时间 */
    public long gmt_modified;
}
