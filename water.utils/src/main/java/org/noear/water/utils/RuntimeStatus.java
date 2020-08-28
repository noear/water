package org.noear.water.utils;

public class RuntimeStatus {
    /**
     * java虚拟机允许开启的最大的内存
     */
    public long memoryMax;

    /**
     * 内存总量
     */
    public long memoryTotal;

    /**
     * 空闲内存
     */
    public long memoryFree;


    /**
     * 老生代
     * */
    public long memoryOldGenMax;
    public long memoryOldGenUsed;
    /**
     * 新生代
     * */
    public long memoryPermGenMax;
    public long memoryPermGenUsed;



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
    public String pid;


    /**
     * 线程总量
     */
    public int threadCount;
    //高峰线程数
    public int threadPeakCount;
    //后台线程数
    public int threadDaemonCount;
}
