package org.noear.water.dso;

import org.noear.water.model.ConfigSetM;

@FunctionalInterface
public interface ConfigHandler {
    void handler(ConfigSetM cfgSet);
}
