package org.noear.water.model;

import java.io.Serializable;

public class KeyM implements Serializable {
    /** 密钥ID */
    private int key_id;
    /** 访问键 */
    private String access_key;
    /** 访问密钥 */
    private String access_secret_key;
    /** 访问密钥盐 */
    private String access_secret_salt;
    /** 分组标签 */
    private String tag;
    /** 标记 */
    private String label;

    public int key_id() {
        return key_id;
    }

    public String access_key() {
        return access_key;
    }

    public String access_secret_key() {
        return access_secret_key;
    }

    public String access_secret_salt() {
        return access_secret_salt;
    }

    public String tag() {
        return tag;
    }

    public String label() {
        return label;
    }
}