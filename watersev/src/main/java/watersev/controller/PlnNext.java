package watersev.controller;

import java.util.Date;

/**
 * 定时任务下一个执行点
 *
 * @author noear
 */
public class PlnNext {
    /**
     * 执行时间
     * */
    public Date datetime;

    /**
     * 以天为间隔
     * */
    public boolean intervalOfDay;

    /**
     * 允许执行（默认为：true）
     * */
    public boolean allow = true;
}
