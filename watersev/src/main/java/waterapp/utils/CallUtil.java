package waterapp.utils;

public class CallUtil {
    public static void asynCall(Runnable call){
        new Thread(()->{
            call.run();
        }).start();
    }
}
