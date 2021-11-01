package org.noear.water.protocol.solution;

import org.noear.water.model.ConfigM;
import org.noear.water.protocol.*;
import org.noear.water.protocol.model.log.LoggerEntity;
import org.noear.water.protocol.model.log.LoggerMeta;
import org.noear.water.utils.TextUtils;
import org.noear.water.utils.ext.Fun1;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LogSourceFactoryImpl implements LogSourceFactory {
    private static String _lock = "";

    private LoggerEntity _def;
    private Fun1<String, LoggerMeta> _loggerGetter;
    private Map<String, LoggerEntity> _loggerMap = new HashMap<>();

    public LogSourceFactoryImpl(ConfigM def, Fun1<String, LoggerMeta> loggerGetter) {
        _def = new LoggerEntity(createLogSource(def), def);
        _loggerGetter = loggerGetter;
    }

    @Override
    public void updateSource(String logger) throws IOException {
        LoggerEntity entity = _loggerMap.get(logger);
        if (entity == null) {
            return;
        }

        LoggerMeta meta = getLoggerMeta(logger);

        if (meta == null || TextUtils.isEmpty(meta.getSource())) {
            //找不到元信息
            return;
        }

        ConfigM cfg = ProtocolHub.config.getByTagKey(meta.getSource());

        if (entity.sourceConfig.value.equals(cfg.value)) {
            return;
        }

        LogSource source = createLogSource(cfg);
        if (source != null) {
            LogSource oldSource = entity.source;

            entity.source = source;
            entity.sourceConfig = cfg;

            //如果与默认的是同一个源，则不关闭
            if (oldSource != _def.source) {
                oldSource.close();
            }
        }
    }


    @Override
    public LogSource getSource(String logger) {
        LoggerEntity entity = _loggerMap.get(logger);

        if (entity == null) {
            synchronized (_lock) {
                entity = _loggerMap.get(logger);

                if (entity == null) {
                    LoggerMeta model = getLoggerMeta(logger);

                    if (model != null && TextUtils.isEmpty(model.getSource()) == false) {
                        ConfigM cfg = ProtocolHub.config.getByTagKey(model.getSource());
                        entity = new LoggerEntity(createLogSource(cfg), cfg);
                    }
                }

                if (entity == null) {
                    entity = new LoggerEntity(_def.source, _def.sourceConfig);
                }

                _loggerMap.put(logger, entity);
            }
        }

        return entity.source;
    }

    @Override
    public LoggerMeta getLoggerMeta(String logger) {
        return _loggerGetter.run(logger);
    }


    public static LogSource createLogSource(ConfigM cfg) {
        if (cfg == null || TextUtils.isEmpty(cfg.value)) {
            return null;
        }

        if (cfg.value.contains("=mongodb")) {
            String schema = cfg.getProp().getProperty("schema");
            if (TextUtils.isEmpty(schema)) {
                schema = "water_log";
            }

            return new LogSourceMongo(cfg.getMg(schema));
        } else if (cfg.value.contains("=elasticsearch")) {
            return new LogSourceElasticsearch(cfg.getEs());
        } else {
            return new LogSourceRdb(cfg.getDb(true));
        }
    }
}
