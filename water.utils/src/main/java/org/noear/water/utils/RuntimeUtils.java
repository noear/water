package org.noear.water.utils;

import java.lang.management.*;

public class RuntimeUtils {
    private static RuntimeStatus status = new RuntimeStatus();


    public static RuntimeStatus getStatus() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        Runtime runtime = Runtime.getRuntime();

        status.memoryFree = (byteToM(runtime.freeMemory()));
        status.memoryTotal = (byteToM(runtime.totalMemory()));

        final MemoryPoolMXBean oldGen = getOldGenMemoryPool();
        if(oldGen != null) {
            final MemoryUsage usage = oldGen.getUsage();
            status.memoryOldGenMax = (byteToM(usage.getMax()));
            status.memoryOldGenUsed = (byteToM(usage.getUsed()));
        }

        final MemoryPoolMXBean permGen = getPermGenMemoryPool();
        if(permGen != null) {
            final MemoryUsage usage = permGen.getUsage();
            status.memoryPermGenMax = (byteToM(usage.getMax()));
            status.memoryPermGenUsed = (byteToM(usage.getUsed()));
        }


        status.timeElapsed = (runtimeMXBean.getUptime());

        status.threadCount = (threadMXBean.getThreadCount());
        status.threadPeakCount = (threadMXBean.getPeakThreadCount());
        status.threadDaemonCount = (threadMXBean.getDaemonThreadCount());

        if(status.pid == null) {
            //这些只需要取一次
            //
            status.memoryMax = (byteToM(runtime.maxMemory()));

            status.timeStart = new Datetime(runtimeMXBean.getStartTime()).toString();

            status.pid = runtimeMXBean.getName();
            status.os = (System.getProperty("os.name"));
        }


        return status;
    }

    public static long byteToM(long bytes) {
        long kb = (bytes / 1024 / 1024);
        return kb;
    }

    private static MemoryPoolMXBean getEdenSpacePool() {
        for (final MemoryPoolMXBean memoryPool : ManagementFactory.getMemoryPoolMXBeans()) {
            if (memoryPool.getName().endsWith("Eden Space")) {
                return memoryPool;
            }
        }
        return null;
    }

    private static MemoryPoolMXBean getOldGenMemoryPool() {
        for (final MemoryPoolMXBean memoryPool : ManagementFactory.getMemoryPoolMXBeans()) {
            if (memoryPool.getName().endsWith("Old Gen")) {
                return memoryPool;
            }
        }
        return null;
    }

    private static MemoryPoolMXBean getPermGenMemoryPool() {
        for (final MemoryPoolMXBean memoryPool : ManagementFactory.getMemoryPoolMXBeans()) {
            if (memoryPool.getName().endsWith("Perm Gen")) {
                return memoryPool;
            }
        }
        return null;
    }
}
