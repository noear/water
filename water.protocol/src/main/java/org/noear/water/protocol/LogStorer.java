package org.noear.water.protocol;

import org.noear.water.log.LogEvent;

import java.util.List;

public interface LogStorer {
    void writeAll(List<LogEvent> list);
}
