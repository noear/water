package waterapi.dso.wrap;

import org.noear.water.protocol.solution.LogSourceRdb;
import waterapi.Config;

public class LogSourceDef extends LogSourceRdb {
    public LogSourceDef() {
        super(Config.water_log);
    }
}
