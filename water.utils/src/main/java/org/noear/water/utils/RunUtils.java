package org.noear.water.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunUtils {
    private static ExecutorService pools = Executors.newCachedThreadPool();

    public static void runAsyn(Runnable call) {
        pools.execute(call);
    }
}
