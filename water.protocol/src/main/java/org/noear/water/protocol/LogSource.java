package org.noear.water.protocol;

import org.noear.water.model.LogM;
import org.noear.water.model.TagCountsM;
import org.noear.water.protocol.model.log.LogModel;

import java.io.Closeable;
import java.util.List;

public interface LogSource extends Closeable {
    List<LogModel> query(String logger, Integer level, int size, String tagx, long startLogId, long timestamp) throws Exception;

    List<TagCountsM> queryGroupCountBy(String logger, String group, String service, String filed) throws Exception;

    void writeAll(String logger, List<LogM> list) throws Exception;

    void create(String logger) throws Exception;

    long clear(String logger, int keep_days, int limit_rows) throws Exception;

    /**
     * 充许搜索
     * */
    boolean allowSearch();

    /**
     * 充许以小时分片
     * */
    boolean allowHourShard();
}
