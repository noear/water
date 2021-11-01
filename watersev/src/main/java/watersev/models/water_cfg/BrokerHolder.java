package watersev.models.water_cfg;

import org.noear.water.protocol.MsgBroker;

/**
 * @author noear 2021/11/1 created
 */
public class BrokerHolder {
    /**
     * 是否已停止
     * */
    public boolean stoped;
    public final MsgBroker msgBroker;

    public BrokerHolder(MsgBroker msgBroker) {
        this.msgBroker = msgBroker;
    }
}
