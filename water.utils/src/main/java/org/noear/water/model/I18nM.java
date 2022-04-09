package org.noear.water.model;

import java.io.Serializable;

public class I18nM implements Serializable {
    /** 名称 */
    private String name;
    /** 值  */
    private String value;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
