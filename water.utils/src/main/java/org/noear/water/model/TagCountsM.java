package org.noear.water.model;

import java.io.Serializable;

/**
 * @author noear 2021/11/1 created
 */
public class TagCountsM implements Serializable {
    public String tag;
    public long counts;

    public String getTag() {
        return tag;
    }

    public long getCounts() {
        return counts;
    }
}
