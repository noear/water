package org.noear.water.dso;

import org.noear.water.utils.HttpUtils;

public interface WaterUpstream {
    default void setAgentDef(String url){}
    HttpUtils xcall(String path);
}
