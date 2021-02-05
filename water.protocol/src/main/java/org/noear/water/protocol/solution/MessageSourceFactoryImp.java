package org.noear.water.protocol.solution;

import org.noear.water.model.ConfigM;
import org.noear.water.protocol.MessageSource;
import org.noear.water.protocol.MessageSourceFactory;
import org.noear.weed.cache.ICacheServiceEx;

/**
 * @author noear 2021/2/5 created
 */
public class MessageSourceFactoryImp implements MessageSourceFactory {
    MessageSource source;

    public MessageSourceFactoryImp(ConfigM cfg, ICacheServiceEx cache) {
        source = new MessageSourceRdb(cfg.getDb(true), cache);
    }

    @Override
    public MessageSource getSource() {
        return source;
    }
}
