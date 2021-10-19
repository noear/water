package org.noear.water.protocol.solution;

import org.noear.water.WW;
import org.noear.water.model.ConfigM;
import org.noear.water.protocol.MessageSource;
import org.noear.water.protocol.MessageSourceFactory;
import org.noear.weed.cache.ICacheServiceEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author noear 2021/2/5 created
 */
public class MessageSourceFactoryImp implements MessageSourceFactory {
    MessageSource source;
    Logger log = LoggerFactory.getLogger(WW.water_log_msg);

    public MessageSourceFactoryImp(ConfigM cfg, ICacheServiceEx cache) {
        if (cfg.value.indexOf("=mongodb") > 0) {
            source = new MessageSourceMongo(cfg.getMg("water_message"), cache, log);
        } else {
            source = new MessageSourceRdb(cfg.getDb(true), cache, log);
        }
    }

    @Override
    public MessageSource getSource() {
        return source;
    }
}
