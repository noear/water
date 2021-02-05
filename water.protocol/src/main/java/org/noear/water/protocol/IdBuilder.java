package org.noear.water.protocol;

public interface IdBuilder {
    long getId(String tag);

    default long getMsgId() {
        return getId("msg_id");
    }

    default long getLogId(String logger) {
        return getId("log_id_" + logger);
    }
}
