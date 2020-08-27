package org.noear.water.protocol;

/**
 * 监控查询器
 * */
public interface MonitorPuller {
    void pull(MonitorType type) throws Exception;
}
