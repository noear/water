package org.noear.water.model;

import java.io.Serializable;

public class KeyM implements Serializable {
    /** 密钥ID */
    public int key_id;
    /** 访问键 */
    public String access_key;
    /** 访问密钥 */
    public String access_secret_key;
    /** 访问密钥盐 */
    public String access_secret_salt;
    /** 分组标签 */
    public String tag;
    /** 标记 */
    public String label;
}
