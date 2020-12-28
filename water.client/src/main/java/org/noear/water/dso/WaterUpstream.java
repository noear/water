package org.noear.water.dso;

import org.noear.water.utils.HttpUtils;

/**
 * Water 负载器
 *
 * @author noear
 * @since 2.0
 * */
public interface WaterUpstream {
    default void setBackup(String server){}
    HttpUtils xcall(String path);
}
