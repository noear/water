package org.noear.water.protocol;

import org.noear.water.protocol.model.ELineModel;

import java.util.List;

/**
 * 监控查询器
 * */
public interface Monitoring {
    void pull(MonitorType type) throws Exception;
    List<ELineModel> query(MonitorType type, String instanceId,Integer dateType, Integer dataType) throws Exception;
}
