package watersev.controller;

import org.noear.redisx.RedisClient;
import org.noear.redisx.plus.RedisQueue;
import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.log.LogEvent;
import org.noear.water.model.ConfigM;
import org.noear.water.protocol.ProtocolHub;
import watersev.Config;
import java.util.List;

/**
 * @author noear 2021/10/21 created
 */
@Component
public class LogExchangeController implements IJob {
    final int _interval_def = 1000;
    int _interval = 1000;
    RedisClient redisClient;

    @Override
    public String getName() {
        return "log";
    }

    @Override
    public int getInterval() {
        return _interval;
    }

    @Override
    public void exec() throws Throwable {
        if (redisClient == null) {
            ConfigM cfg = Config.cfg("water_log_queue");
            if (cfg == null) {
                _interval = _interval_def * 60;
            } else {
                _interval = _interval_def;
            }

            redisClient = cfg.getRd();
        }

        RedisQueue redisQueue = redisClient.getQueue("water_log");

        redisQueue.popAll(item -> {
            writeAll(item);
        });
    }

    private void writeAll(String list_json) {
        if (Utils.isEmpty(list_json)) {
            return;
        }

        if (list_json.startsWith("[") == false) {
            return;
        }

        List<LogEvent> list = ONode.load(list_json).toObjectList(LogEvent.class);

        ProtocolHub.logStorer.writeAll(list);
    }
}
