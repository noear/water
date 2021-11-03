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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private List<BrokerEntity> _brokerAry = new ArrayList<>();

    public MsgBrokerFactoryImpl(ConfigM def, ICacheServiceEx cache, Fun1<String, BrokerMeta> brokerGetter) {
        _def = new BrokerEntity(new MsgBrokerImpl(def, cache), def);
        _cache = cache;
        _brokerGetter = brokerGetter;
    }

    @Override
    public void updateBroker(String broker) throws IOException {
        BrokerEntity entity = _brokerMap.get(broker);
        if (entity == null) {
            //如果没有，则不用更新
            return;
        }

        BrokerMeta meta = getBrokerMeta(broker);

        if (meta == null) {
            //找不到元信息
            return;
        }

        MsgBroker source = null;
        ConfigM cfg;

        if (TextUtils.isEmpty(meta.getSource())) {
            //如果是空配置，是否为默认？
            if (entity.source == _def.source) {
                return; //说明没变
            }

            source = _def.source;
            cfg = _def.sourceConfig;
        } else {
            cfg = ProtocolHub.config.getByTagKey(meta.getSource());

            if (entity.sourceConfig.value.equals(cfg.value)) {
                return;//说明没变
            }

            source = new MsgBrokerImpl(cfg, _cache);
        }

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

    int brokerIndex;

    @Override
    public MsgBroker getBroker(String broker) {
        if (TextUtils.isEmpty(broker)) {
            return _def.source;
        }

        if("*".equals(broker)) {
            //
            // * 表示自动负载均衡
            //
            int size = _brokerAry.size();
            if (size > 0) {
                if (brokerIndex > 9999999) {
                    brokerIndex = 0;
                } else {
                    brokerIndex++;
                }

                return _brokerAry.get(brokerIndex % size).source;
            } else {
                return _def.source;
            }
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
                _brokerAry.add(entity);
            }
        }

        return entity.source;
    }

    @Override
    public BrokerMeta getBrokerMeta(String broker) {
        return _brokerGetter.run(broker);
    }
}
