package waterapi.models;

import java.util.Date;

/**
 * @author noear 2021/11/12 created
 */
public class GatewayModel {
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
    /** 最后更新时间 */
    public long gmt_modified;
}
