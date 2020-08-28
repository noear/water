package org.noear.water.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;

public class RuntimeUtils {
    private static RuntimeStatus status = new RuntimeStatus();


    public static RuntimeStatus getStatus() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        Runtime runtime = Runtime.getRuntime();

        status.memoryFree = (byteToM(runtime.freeMemory()));
        status.memoryTotal = (byteToM(runtime.totalMemory()));
        status.memoryMax = (byteToM(runtime.maxMemory()));

        status.timeStart = new Datetime(runtimeMXBean.getStartTime()).toString();
        status.timeElapsed = (runtimeMXBean.getUptime());

        if(status.pid == null) {
            status.pid = runtimeMXBean.getName();
            status.os = (System.getProperty("os.name"));
        }
        status.threadCount = (threadMXBean.getThreadCount());


        return status;
    }

    public static long byteToM(long bytes) {
        long kb = (bytes / 1024 / 1024);
        return kb;
    }
}
