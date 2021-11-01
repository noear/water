package org.noear.water.protocol.model.message;


public interface BrokerMeta {
    String getTag();

    String getBroker();

    String getSource();

    int getKeepDays();
}
