package org.noear.water.protocol.model;


public interface LoggerMeta {
    String getTag();

    String getLogger();

    String getSource();

    int getKeepDays();
}
