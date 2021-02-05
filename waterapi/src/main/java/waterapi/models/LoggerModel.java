package waterapi.models;

import org.noear.water.protocol.model.log.LoggerMeta;

public class LoggerModel implements LoggerMeta {
    public String tag;
    public String logger;
    public String source;
    public int keep_days;

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public String getLogger() {
        return logger;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public int getKeepDays() {
        return keep_days;
    }
}
