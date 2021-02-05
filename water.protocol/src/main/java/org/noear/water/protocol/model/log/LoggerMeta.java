package org.noear.water.protocol.model.log;


public interface LoggerMeta {
    String getTag();

    String getLogger();

    String getSource();

    int getKeepDays();
}
