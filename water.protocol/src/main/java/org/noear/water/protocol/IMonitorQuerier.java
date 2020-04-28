package org.noear.water.protocol;

/**
 * 监控查询器s
 * */
public interface IMonitorQuerier {
    void blsPull(String region, String instanceId);

    void ecsPull(String region, String instanceId);

    void rdsPull(String region, String instanceId);

    void memcachedPull(String region, String instanceId);

    void redisPull(String region, String instanceId);
}
