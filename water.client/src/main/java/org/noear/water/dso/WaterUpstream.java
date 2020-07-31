package org.noear.water.dso;

import org.noear.water.utils.HttpUtils;

public interface WaterUpstream {
    default void setBackup(String server){}
    HttpUtils xcall(String path);
}
