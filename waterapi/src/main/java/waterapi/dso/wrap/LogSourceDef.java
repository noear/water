package waterapi.dso.wrap;

import org.noear.water.protocol.solution.LogSourceDb;
import waterapi.Config;

public class LogSourceDef extends LogSourceDb {
    public LogSourceDef() {
        super(Config.water_log);
    }
}
