package org.noear.water.solon_plugin;

import org.noear.water.model.MessageM;

public interface XMessageHandler {
    boolean handler(MessageM msg) throws Exception;
}
