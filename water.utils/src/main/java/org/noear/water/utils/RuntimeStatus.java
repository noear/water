package org.noear.water.utils;

import java.util.*;

public class RuntimeStatus {
    public String name;
    public String address;

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

    public String vm;

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

    public Map<String,Object> system = new LinkedHashMap<>();

    /**
     * java虚拟机允许开启的最大的内存
     */
    public long memoryMax;

    /**
     * 内存总量
     */
    public long memoryTotal;

    public long memoryUsed;


    public List<Map<String,Object>> memoryPools = new ArrayList<>();

}
