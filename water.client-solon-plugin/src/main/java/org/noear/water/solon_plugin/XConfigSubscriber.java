package org.noear.water.solon_plugin;

import org.noear.water.model.ConfigSetM;

public interface XConfigSubscriber {
    void handler(ConfigSetM cfgSet);
}
