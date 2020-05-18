package org.noear.water.model;

import org.noear.snack.ONode;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesM  extends Properties {

    public Map<String, Object> toMap() {
        Map<String, Object> tmp = new HashMap<>();
        this.forEach((k, v) -> {
            if (k instanceof String) {
                tmp.put((String) k, v);
            }
        });

        return tmp;
    }

    public ONode toNode() {
        ONode tmp = new ONode();
        this.forEach((k, v) -> {
            if (k instanceof String) {
                tmp.set((String) k, v);
            }
        });

        return tmp;
    }

    public <T> T toObject(Class<T> clz) {
        return toNode().toObject(clz);
    }
}
