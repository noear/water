package org.noear.water.log;


import com.lmax.disruptor.EventHandler;
import org.noear.water.WaterClient;

public class WaterLogEventHandler  implements EventHandler<WaterLogEvent> {

    @Override
    public void onEvent(WaterLogEvent event, long sequence, boolean endOfBatch) throws Exception {
        WaterClient.Log.append(
                event.logger,
                event.level,
                event.tag,
                event.tag1,
                event.tag2,
                event.tag3,
                event.summary,
                event.toString());
    }
}
