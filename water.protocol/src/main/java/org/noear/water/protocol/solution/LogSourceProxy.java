package org.noear.water.protocol.solution;

import org.noear.water.model.LogM;
import org.noear.water.model.ConfigM;
import org.noear.water.protocol.LogSource;
import org.noear.water.protocol.model.log.LogModel;

import java.io.IOException;
import java.util.List;

public class LogSourceProxy implements LogSource {
    LogSource real;

    public LogSourceProxy(ConfigM cfg) {
        if (cfg.value.indexOf("=mongodb") > 0) {
            real = new LogSourceMongo(cfg.getMg("water_log"));
        } else if (cfg.value.indexOf("=elasticsearch") > 0) {
            real = new LogSourceElasticsearch(cfg.getEs());
        } else {
            real = new LogSourceRdb(cfg.getDb(true));
        }
    }

    @Override
    public List<LogModel> query(String logger, Integer level, int size, String tagx, long timestamp) throws Exception {
        return real.query(logger, level, size, tagx, timestamp);
    }

    @Override
    public void writeAll(String logger, List<LogM> list) throws Exception {
        real.writeAll(logger, list);
    }

    @Override
    public void create(String logger) throws Exception {
        real.create(logger);
    }

    @Override
    public long clear(String logger, int keep_days, int limit_rows) throws Exception {
        return real.clear(logger, keep_days, limit_rows);
    }

    @Override
    public void close() throws IOException {
        real.close();
    }
}
