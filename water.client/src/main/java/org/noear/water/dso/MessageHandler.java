package org.noear.water.dso;

import org.noear.water.model.MessageM;

public interface MessageHandler {
    boolean handler(MessageM msg) throws Exception;
}
