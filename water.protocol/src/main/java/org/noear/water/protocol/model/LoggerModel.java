package org.noear.water.protocol.model;

import lombok.Getter;

@Getter
public class LoggerModel {
    public String tag;
    public String logger;
    public String source;
    public int keep_days;
}
