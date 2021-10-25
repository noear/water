package org.noear.water.protocol;

import org.noear.water.model.LogM;

import java.util.List;

public interface LogStorer {
    void writeAll(List<LogM> list);
}
