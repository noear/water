package org.noear.water.dso;

import org.noear.water.model.ConfigSetM;

/**
 * 配置处理
 *
 * @author noear
 * @since 2.0
 * */
@FunctionalInterface
public interface ConfigHandler {
    void handle(ConfigSetM cfgSet);
}
