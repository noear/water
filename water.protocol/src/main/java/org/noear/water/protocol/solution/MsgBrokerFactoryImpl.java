package org.noear.water.protocol.solution;

import org.noear.water.model.ConfigM;
import org.noear.water.protocol.MsgBroker;
import org.noear.water.protocol.MsgBrokerFactory;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.model.message.BrokerEntity;
import org.noear.water.protocol.model.message.BrokerMeta;
import org.noear.water.utils.TextUtils;
import org.noear.water.utils.ext.Fun1;
import org.noear.weed.cache.ICacheServiceEx;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author noear 2021/11/1 created
 */
public class MsgBrokerFactoryImpl implements MsgBrokerFactory {
    private static String _lock = "";

    BrokerEntity _def;
    ICacheServiceEx _cache;
    Fun1<String, BrokerMeta> _brokerGetter;
    private Map<String, BrokerEntity> _brokerMap = new HashMap<>();

    public MsgBrokerFactoryImpl(ConfigM def, ICacheServiceEx cache, Fun1<String, BrokerMeta> brokerGetter) {
        _def = new BrokerEntity(new MsgBrokerImpl(def, cache), def);
        _cache = cache;
        _brokerGetter = brokerGetter;
    }

    @Override
    public void updateBroker(String broker) throws IOException {
        BrokerEntity entity = _brokerMap.get(broker);
        if (entity == null) {
            return;
        }

        BrokerMeta meta = getBrokerMeta(broker);

        if (meta == null || TextUtils.isEmpty(meta.getSource())) {
            //找不到元信息
            return;
        }

        ConfigM cfg = ProtocolHub.config.getByTagKey(meta.getSource());

        if (entity.sourceConfig.value.equals(cfg.value)) {
            return;
        }

        MsgBroker source = new MsgBrokerImpl(cfg, _cache);
        if (source != null) {
            MsgBroker oldSource = entity.source;

            entity.source = source;
            entity.sourceConfig = cfg;

            //如果与默认的是同一个源，则不关闭
            if (oldSource != _def.source) {
                oldSource.close();
            }
        }
    }

    @Override
    public MsgBroker getBroker(String broker) {
        if (TextUtils.isEmpty(broker)) {
            return _def.source;
        }

        BrokerEntity entity = _brokerMap.get(broker);

        if (entity == null) {
            synchronized (_lock) {
                entity = _brokerMap.get(broker);

                if (entity == null) {
                    BrokerMeta model = getBrokerMeta(broker);

                    if (model != null && TextUtils.isEmpty(model.getSource()) == false) {
                        ConfigM cfg = ProtocolHub.config.getByTagKey(model.getSource());
                        entity = new BrokerEntity(new MsgBrokerImpl(cfg, _cache), cfg);
                    }
                }

                if (entity == null) {
                    entity = new BrokerEntity(_def.source, _def.sourceConfig);
                }

                _brokerMap.put(broker, entity);
            }
        }

        return entity.source;
    }

    @Override
    public BrokerMeta getBrokerMeta(String broker) {
        return _brokerGetter.run(broker);
    }
}
