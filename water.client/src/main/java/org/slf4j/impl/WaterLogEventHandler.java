package org.slf4j.impl;


import com.lmax.disruptor.EventHandler;
import org.noear.water.WaterClient;

public class WaterLogEventHandler implements EventHandler<WaterLogEvent> {

    @Override
    public void onEvent(WaterLogEvent event, long sequence, boolean endOfBatch) throws Exception {

        System.out.printf("%d [%s] %s%n", sequence, event.getLevel(), event);

//        System.out.printf("%d [%s] Thread %d-%s: %s%n",
//                sequence,
//                event.getLevel(),
//                Thread.currentThread().getId(),
//                Thread.currentThread().getName(),
//                event);

        if (event.getName().indexOf(".") < 0) {
            WaterClient.Log.append(
                    event.getName(),
                    event.getLevel(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    event.toString(),
                    false);
        }

    }
}
