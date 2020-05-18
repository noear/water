package org.noear.water.utils;

import org.noear.water.utils.ext.Act0Ex;

public class CallUtils {
    public static void call(Act0Ex fun){
        try{
            fun.run();
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static void callTry(Act0Ex fun){
        try{
            fun.run();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
