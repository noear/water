package org.noear.water.protocol.solution;

import org.noear.redisx.RedisClient;
import org.noear.water.model.ConfigM;
import org.noear.water.protocol.LogSource;
import org.noear.water.protocol.MsgQueue;
import org.noear.water.utils.TextUtils;

import java.util.Properties;

public class ProtocolUtil {
    public static MsgQueue createMessageQueue(ConfigM cfg) {
        Properties prop = cfg.getProp();

        String name = prop.getProperty("store.name", "").toLowerCase();
        String type = prop.getProperty("store.type", "").toLowerCase();

        if (TextUtils.isEmpty(type) || TextUtils.isEmpty(type)) {
            throw new RuntimeException("ProtocolHub::There was an error in the input configuration");
        }

        if ("redis".equals(type)) {
            //server
            //user
            //password
            //db
            return new MsgQueueRedis(name, new RedisClient(prop));
        }

        throw new RuntimeException("ProtocolHub::There was an error in the input configuration");
    }

    public static LogSource createLogSource(ConfigM cfg) {
        if (cfg == null || TextUtils.isEmpty(cfg.value)) {
            return null;
        }

        return new LogSourceProxy(cfg);
    }
}
