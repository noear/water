package watersev.utils;

import watersev.Config;

public class CallUtil {
    public static void asynCall(Runnable call){
        Config.pools.execute(call);
    }
}
