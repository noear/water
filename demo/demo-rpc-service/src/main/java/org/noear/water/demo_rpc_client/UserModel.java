package org.noear.water.demo_rpc_client;

import java.util.Date;

public class UserModel {
    /** 内部用户ID */
    public int puid;
    /** 用户账号 */
    public String user_id;
    /** 外部关系对象 */
    public int out_objt;
    /** 外部关系对象ID */
    public long out_objt_id;
    /** 中文名称 */
    public String cn_name;
    /** 英文名称 */
    public String en_name;
    /** 密码找回邮箱 */
    public String pw_mail;
    /** 标签 */
    public String tags;
    /** 备注 */
    public String note;
    /** 是否禁用 */
    public boolean is_disabled;
    /** 是否可见 */
    public boolean is_visibled;
    /** 创建时间 */
    public Date create_time;
    /** 最后更新时间 */
    public Date last_update;
    /** 账号密码 */
    public String pass_wd;
    /** 状态（预留） */
    public int state;
}