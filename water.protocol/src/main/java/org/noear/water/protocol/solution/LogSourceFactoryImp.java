package org.noear.water.protocol.solution;

import org.noear.water.model.ConfigM;
import org.noear.water.protocol.*;
import org.noear.water.protocol.model.LoggerModel;
import org.noear.water.utils.TextUtils;
import org.noear.water.utils.ext.Fun1;

import java.util.HashMap;
import java.util.Map;

public class LogSourceFactoryImp implements ILogSourceFactory {
    private static String _lock = "";

    private ILogSource _def;
    private Fun1<String, LoggerModel> _loggerGetter;

    private Map<String, ILogSource> _logMap = new HashMap<>();

    public LogSourceFactoryImp(ILogSource def, Fun1<String, LoggerModel> loggerGetter) {
        _def = def;
        _loggerGetter = loggerGetter;
    }

    private ILogSource build(ConfigM cfg) {
        if (cfg == null || TextUtils.isEmpty(cfg.value)) {
            return null;
        }

        //String type = cfg.getProp().getProperty(WW.type_logger);
        return new LogSourceDb(cfg.getDb(true));
    }

    @Override
    public ILogSource getSource(String logger) {
        ILogSource log = _logMap.get(logger);

        if (log == null) {
            synchronized (_lock) {
                log = _logMap.get(logger);

                if (log == null) {
                    LoggerModel model = _loggerGetter.run(logger);

                    if (model != null && TextUtils.isEmpty(model.source) == false) {
                        ConfigM cfg = ProtocolHub.config.getByTagKey(model.source);
                        log = build(cfg);
                    }
                }

                if (log == null) {
                    log = _def;
                }

                _logMap.put(logger, log);
            }
        }

        return log;
    }
}
