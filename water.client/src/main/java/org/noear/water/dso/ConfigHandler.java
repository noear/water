package org.noear.water.dso;

import org.noear.water.model.ConfigSetM;

/**
 * 配置处理
 * */
@FunctionalInterface
public interface ConfigHandler {
    void handler(ConfigSetM cfgSet);
}
