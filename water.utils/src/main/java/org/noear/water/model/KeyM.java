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

    public int getKeyId() {
        return key_id;
    }

    public String getAccessKey() {
        return access_key;
    }

    public String getAccessSecretKey() {
        return access_secret_key;
    }

    public String getAccessSecretSalt() {
        return access_secret_salt;
    }

    public String getTag() {
        return tag;
    }

    public String getLabel() {
        return label;
    }
}
