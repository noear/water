package org.noear.water.protocol.solution;

import org.noear.redisx.RedisClient;
import org.noear.water.WW;
import org.noear.water.model.ConfigM;
import org.noear.water.model.PropertiesM;
import org.noear.water.protocol.MsgBroker;
import org.noear.water.protocol.MsgQueue;
import org.noear.water.protocol.MsgSource;
import org.noear.water.utils.DsUtils;
import org.noear.water.utils.TextUtils;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.mongo.MgContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * @author noear 2021/11/1 created
 */
public class MsgBrokerImpl implements MsgBroker {
    MsgSource source;
    MsgQueue queue;
    Logger log = LoggerFactory.getLogger(WW.logger_water_log_msg);

    /**
     * 新的配置
     */
    public MsgBrokerImpl(ConfigM cfg, ICacheServiceEx cache) {
        PropertiesM prop = cfg.getProp();
        PropertiesM storeProp = prop.getProp("store");
        PropertiesM queueProp = prop.getProp("queue");

        if (storeProp.size() < 3) {
            init(cfg, prop, null, cache);
        } else {
            init(cfg, storeProp, queueProp, cache);
        }
    }

    private void init(ConfigM cfg, Properties sourceProp, Properties queueProp, ICacheServiceEx cache) {
        //for source
        if (cfg.value.contains("=mongodb")) {
            String schema = sourceProp.getProperty("schema");
            if (TextUtils.isEmpty(schema)) {
                schema = "water_msg";
            }
            source = new MsgSourceMongo(new MgContext(sourceProp, schema), cache, log);
        } else {
            source = new MsgSourceRdb(DsUtils.getDb(sourceProp, true), cache, log);
        }

        if (cfg.value.contains("=redis") && queueProp != null) {
            String name = queueProp.getProperty("name");

            if (TextUtils.isEmpty(name)) {
                throw new RuntimeException("MsgBroker::Missing name configuration");
            }

            queue = new MsgQueueRedis(name, new RedisClient(queueProp));
        } else {
            queue = MsgQueueLocal.getInstance();
        }
    }

    @Override
    public MsgQueue getQueue() {
        return queue;
    }

    @Override
    public MsgSource getSource() {
        return source;
    }

    @Override
    public void close() throws IOException {
        try {
            source.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            queue.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
