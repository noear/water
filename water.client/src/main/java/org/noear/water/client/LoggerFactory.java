package org.noear.water.client;


import org.noear.water.client.dso.log.Logger;
import org.noear.water.client.dso.log.WaterLogger;

public class LoggerFactory {
    public static Logger getLogger(String name){
        return new WaterLogger(name);
    }
}
