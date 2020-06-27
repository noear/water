package org.noear.water.protocol.solution;

import org.noear.water.model.ConfigM;
import org.noear.water.protocol.*;
import org.noear.water.protocol.model.LoggerModel;
import org.noear.water.utils.TextUtils;
import org.noear.water.utils.ext.Fun1;

import java.util.HashMap;
import java.util.Map;

public class LogSourceFactoryImp implements LogSourceFactory {
    private static String _lock = "";

    private LogSource _def;
    private Fun1<String, LoggerModel> _loggerGetter;

    private Map<String, LogSource> _logMap = new HashMap<>();

    public LogSourceFactoryImp(LogSource def, Fun1<String, LoggerModel> loggerGetter) {
        _def = def;
        _loggerGetter = loggerGetter;
    }


    @Override
    public LogSource getSource(String logger) {
        LogSource log = _logMap.get(logger);

        if (log == null) {
            synchronized (_lock) {
                log = _logMap.get(logger);

                if (log == null) {
                    LoggerModel model = _loggerGetter.run(logger);

                    if (model != null && TextUtils.isEmpty(model.source) == false) {
                        ConfigM cfg = ProtocolHub.config.getByTagKey(model.source);
                        log = ProtocolUtil.createLogSource(cfg);
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
