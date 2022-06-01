package org.noear.water.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunUtils {
    private static ExecutorService pools = Executors.newCachedThreadPool();

    /**
     * 异步运行（适合IO密集型）
     *
     * //CompletableFuture.runAsync 适合计算密集型
     * */
    public static void runAsyn(Runnable call) {
        pools.execute(call);
    }
}
