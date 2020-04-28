package org.noear.water.log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoggerFactory {
    private static Map<String,Logger> _lib = new ConcurrentHashMap<>();
    public static Logger get(String name) {
        if(_lib.containsKey(_lib)){
            return _lib.get(name);
        }

        try {
            Logger tmp = (Logger) Class.forName("org.noear.water.log.WaterLogger").newInstance();
            tmp.setName(name);

            return _lib.putIfAbsent(name,tmp);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
