package org.noear.water.utils;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;

public class MonitorUtils {
    private static MonitorStatus clientStatus = new MonitorStatus();

    /**
     * @return
     */
    public static MonitorStatus getStatus() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        Runtime runtime = Runtime.getRuntime();
        //空闲内存
        long freeMemory = runtime.freeMemory();
        clientStatus.freeMemory = (byteToM(freeMemory));
        //内存总量
        long totalMemory = runtime.totalMemory();
        clientStatus.totalMemory = (byteToM(totalMemory));
        //最大允许使用的内存
        long maxMemory = runtime.maxMemory();
        clientStatus.maxMemory = (byteToM(maxMemory));
        //操作系统
        clientStatus.osName = (System.getProperty("os.name"));


        //程序启动时间
        clientStatus.startTime = new Date(runtimeMXBean.getStartTime());
        //类所在路径
        clientStatus.classPath = (runtimeMXBean.getBootClassPath());
        //程序运行时间
        clientStatus.runtime = (runtimeMXBean.getUptime());
        //线程总数
        clientStatus.threadCount = (ManagementFactory.getThreadMXBean().getThreadCount());
        clientStatus.projectPath = (new File("").getAbsolutePath());
        clientStatus.pid = (getPid());
        return clientStatus;
    }

    /**
     * 把byte转换成M
     *
     * @param bytes
     * @return
     */
    public static long byteToM(long bytes) {
        long kb = (bytes / 1024 / 1024);
        return kb;
    }

    /**
     * 获取进程号，适用于windows与linux
     *
     * @return
     */
    public static long getPid() {
        try {
            String name = ManagementFactory.getRuntimeMXBean().getName();
            String pid = name.split("@")[0];
            return Long.parseLong(pid);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
