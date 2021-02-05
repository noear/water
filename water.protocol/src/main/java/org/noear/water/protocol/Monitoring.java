package org.noear.water.protocol;

import org.noear.water.protocol.model.monitor.ELineModel;
import org.noear.water.protocol.model.monitor.ETimeType;

import java.util.List;

/**
 * 监控查询器
 * */
public interface Monitoring {
    void pull(MonitorType type) throws Exception;
    List<ELineModel> query(MonitorType type, String instanceId, ETimeType timeType, Integer dataType) throws Exception;
}
