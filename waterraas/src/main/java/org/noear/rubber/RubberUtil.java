package org.noear.rubber;

public class RubberUtil {
    private static ThreadLocal<RubberContext> localContext = new ThreadLocal<>();
    protected static void registerContext(RubberContext context){
        localContext.set(context);
    }

    protected static void unregisterContext(RubberContext context){
        localContext.remove();
    }

    public RubberContext currentContext(){
        return localContext.get();
    }

}
