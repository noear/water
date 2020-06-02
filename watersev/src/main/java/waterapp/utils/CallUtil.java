package waterapp.utils;

import waterapp.Config;

public class CallUtil {
    public static void asynCall(Runnable call){
        Config.pools.execute(call);
    }
}
