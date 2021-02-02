package wateradmin.dso.wrap;

import org.noear.water.protocol.solution.LogSourceRdb;
import wateradmin.Config;

public class LogSourceDef extends LogSourceRdb {
    public LogSourceDef() {
        super(Config.water_log);
    }
}
