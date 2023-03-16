package org.noear.water.model;

import org.noear.water.utils.TextUtils;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Properties;

public class KeyM implements Serializable {
    /**
     * 密钥ID
     */
    private int key_id;
    /**
     * 访问键
     */
    private String access_key;
    /**
     * 访问密钥
     */
    private String access_secret_key;
    /**
     * 访问密钥盐
     */
    private String access_secret_salt;
    /**
     * 分组标签
     */
    private String tag;
    /**
     * 标记
     */
    private String label;
    /**
     * 元信息
     */
    private String metainfo;

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

    public String metainfo() {
        return metainfo;
    }

    public boolean metainfoHas(String name){
        return metainfoMap().containsKey(name);
    }

    public String metainfoGet(String name){
        return metainfoMap().getProperty(name);
    }

    public String metainfoGet(String name, String def){
        return metainfoMap().getProperty(name, def);
    }

    private transient Properties metainfoMap;

    public Properties metainfoMap() {
        if (metainfoMap == null) {
            synchronized (this) {
                if (metainfoMap == null) {
                    metainfoMap = new Properties();

                    if (TextUtils.isNotEmpty(metainfo)) {
                        try {
                            metainfoMap.load(new StringReader(metainfo));
                        } catch (IOException e) {

                        }
                    }
                }
            }
        }

        return metainfoMap;
    }
}
