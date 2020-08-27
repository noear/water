package org.noear.water.utils;
import java.util.Date;

public class MonitorStatus {
    /**
     * 空闲内存
     */
    public long freeMemory;
    /**
     * 内存总量
     */
    public long totalMemory;
    /**
     * java虚拟机允许开启的最大的内存
     */
    public long maxMemory;

    /**
     * 操作系统名称
     */
    public String osName;
    /**
     * 进程号
     */
    public long pid;


    /**
     * 程序启动时间
     */
    public Date startTime;

    /**
     * 类所在路径
     */
    public String classPath;

    public String projectPath;

    /**
     * 程序运行时间，单位毫秒
     */
    public long runtime;
    /**
     * 线程总量
     */
    public int threadCount;
}
