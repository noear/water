package org.noear.water.dso;

import org.noear.water.utils.HttpUtils;

/**
 * Water 负载器
 *
 * @author noear
 * @since 2.0
 * */
public interface Upstream {
    default void setBackup(String server){}
    HttpUtils http(String path);
}
