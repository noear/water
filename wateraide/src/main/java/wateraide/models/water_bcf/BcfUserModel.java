package wateraide.models.water_bcf;

import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

import java.util.Date;

/**
 * @author noear 2021/11/3 created
 */
@Table("bcf_user")
public class BcfUserModel {
    /** 内部用户ID */
    @PrimaryKey
    public Integer puid;
    /** 用户账号 */
    public String user_id;
    /** 外部关系对象 */
    public Integer out_objt;
    /** 外部关系对象ID */
    public Long out_objt_id;
    /** 中文名称 */
    public String cn_name;
    /** 英文名称 */
    public String en_name;
    /** 密码找回邮箱 */
    public String pw_mail;
    /**  */
    public String token;
    /** 标签 */
    public String tags;
    /** 备注 */
    public String note;
    /** 是否禁用 */
    public Boolean is_disabled;
    /** 是否可见 */
    public Boolean is_visibled;
    /** 创建时间 */
    public Date create_time;
    /** 最后更新时间 */
    public Date last_update;
    /** 账号密码 */
    public String pass_wd;
    /** 状态（预留） */
    public Integer state;

}
