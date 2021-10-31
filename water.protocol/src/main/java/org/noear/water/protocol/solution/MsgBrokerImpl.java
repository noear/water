package org.noear.water.protocol.solution;

import org.noear.water.WW;
import org.noear.water.model.ConfigM;
import org.noear.water.protocol.MsgBroker;
import org.noear.water.protocol.MsgQueue;
import org.noear.water.protocol.MsgSource;
import org.noear.weed.cache.ICacheServiceEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author noear 2021/11/1 created
 */
public class MsgBrokerImpl implements MsgBroker {
    MsgSource source;
    MsgQueue queue;
    Logger log = LoggerFactory.getLogger(WW.water_log_msg);

    public MsgBrokerImpl(ConfigM cfg, ICacheServiceEx cache) {
        if (cfg.value.indexOf("=mongodb") > 0) {
            source = new MsgSourceMongo(cfg.getMg("water_message"), cache, log);
        } else {
            source = new MsgSourceRdb(cfg.getDb(true), cache, log);
        }

        queue = new MsgQueueLocal();
    }

    @Override
    public MsgQueue getQueue() {
        return queue;
    }

    @Override
    public MsgSource getSource() {
        return source;
    }
}
