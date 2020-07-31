package org.noear.water.dso;

import org.noear.water.utils.HttpUtils;

public interface WaterUpstream {
    default void setDefault(String server){}
    HttpUtils xcall(String path);
}
