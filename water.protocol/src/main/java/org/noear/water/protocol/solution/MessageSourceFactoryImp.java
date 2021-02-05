package org.noear.water.protocol.solution;

import org.noear.water.log.Logger;
import org.noear.water.model.ConfigM;
import org.noear.water.protocol.MessageSource;
import org.noear.water.protocol.MessageSourceFactory;
import org.noear.weed.cache.ICacheServiceEx;

/**
 * @author noear 2021/2/5 created
 */
public class MessageSourceFactoryImp implements MessageSourceFactory {
    MessageSource source;

    public MessageSourceFactoryImp(ConfigM cfg, ICacheServiceEx cache, Logger log) {
        source = new MessageSourceRdb(cfg.getDb(true), cache, log);
    }

    @Override
    public MessageSource getSource() {
        return source;
    }
}
