package org.noear.water.solon_plugin;

import org.noear.water.model.MessageM;

public interface XMessageSubscriber {
    boolean handler(MessageM msg) throws Exception;
}
