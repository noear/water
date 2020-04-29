package org.noear.water.log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoggerFactory {
    private static Class<?> _clz;
    private static Map<String, Logger> _lib = new ConcurrentHashMap<>();

    public static Logger get(String name) {
        if (_lib.containsKey(_lib)) {
            return _lib.get(name);
        }

        try {
            if (_clz == null) {
                _clz = Thread.currentThread().getContextClassLoader().loadClass("org.noear.water.log.WaterLogger");
            }

            if (_clz != null) {
                Logger tmp = (Logger) _clz.newInstance();
                tmp.setName(name);

                return _lib.putIfAbsent(name, tmp);
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
            System.err.println("ClassNotFoundException: org.noear.water.log.WaterLogger");

        }

        return _lib.putIfAbsent(name, new LoggerDefault());
    }
}
