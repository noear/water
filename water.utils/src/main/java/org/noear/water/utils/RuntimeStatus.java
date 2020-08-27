package org.noear.water.utils;

public class RuntimeStatus {
    /**
     * 内存总量
     */
    public long memoryTotal;
    /**
     * java虚拟机允许开启的最大的内存
     */
    public long memoryMax;

    /**
     * 空闲内存
     */
    public long memoryFree;


    /**
     * 程序启动时间
     */
    public String timeStart;

    /**
     * 程序运行时间，单位毫秒
     */
    public long timeElapsed;

    /**
     * 操作系统名称
     */
    public String os;

    /**
     * 进程号
     */
    public long pid;

    /**
     * 线程总量
     */
    public int threadCount;
}
