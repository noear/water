package org.noear.water.protocol.model.message;

/**
 * @author noear 2021/11/4 created
 */
public class BrokerVo implements BrokerMeta {
    public String tag;
    public String broker;
    public String source;
    public int keep_days;

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public String getBroker() {
        return broker;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public int getKeepDays() {
        return keep_days;
    }

}