package org.noear.water.log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoggerFactory {
    private static Class<?> _clz;
    private static Map<String, Logger> _lib = new ConcurrentHashMap<>();
    private static Logger loggerDefault = new LoggerDefault();

    public static Logger get(String name) {

        Logger logger = _lib.get(name);
        if (logger != null) {
            return logger;
        }

        try {
            if (_clz == null) {
                _clz = Thread.currentThread().getContextClassLoader().loadClass("org.noear.water.log.WaterLogger");
            }

            if (_clz != null) {
                logger = (Logger) _clz.newInstance();
                logger.setName(name);

                Logger l = _lib.putIfAbsent(name, logger);
                if (l != null) {
                    logger = l;
                }
            }else{
                System.err.println("ClassNotFoundException: org.noear.water.log.WaterLogger");
            }
        } catch (Exception ex) {
            System.err.println("ClassNotFoundException: org.noear.water.log.WaterLogger");
        }

        //如果前面没加成功，就加这个
        if (logger == null) {
            logger = loggerDefault;
            _lib.putIfAbsent(name, logger);
        }

        return logger;
    }
}
