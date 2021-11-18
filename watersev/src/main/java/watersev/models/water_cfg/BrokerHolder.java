package watersev.models.water_cfg;

import org.noear.water.protocol.MsgBroker;
import org.noear.water.protocol.MsgQueue;
import org.noear.water.protocol.MsgSource;

import java.io.IOException;

/**
 * @author noear 2021/11/1 created
 */
public class BrokerHolder implements MsgBroker {
    /**
     * 是否已停止
     */
    public boolean started;
    private final MsgBroker real;

    public BrokerHolder(MsgBroker msgBroker) {
        this.real = msgBroker;
    }

    @Override
    public String getName() {
        return real.getName();
    }

    @Override
    public MsgQueue getQueue() {
        return real.getQueue();
    }

    @Override
    public MsgSource getSource() {
        return real.getSource();
    }

    @Override
    public void close() throws IOException {
        real.close();
    }
}
