package org.noear.water.event;

import org.noear.water.model.ConfigSetM;

@FunctionalInterface
public interface WaterConfigHandler {
    void handler(ConfigSetM cfgSet);
}
