package watersev.controller;

import java.util.Date;

/**
 * 下一次执行
 *
 * @author noear 2021/7/5 created
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
