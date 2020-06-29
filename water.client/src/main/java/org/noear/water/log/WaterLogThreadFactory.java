package org.noear.water.log;

import java.util.concurrent.ThreadFactory;

class WaterLogThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        return new Thread("water-log") {
            @Override
            public void run() {
                r.run();
            }
        };
    }
}
