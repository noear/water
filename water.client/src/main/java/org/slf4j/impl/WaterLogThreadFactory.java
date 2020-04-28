package org.slf4j.impl;

import java.util.concurrent.ThreadFactory;

public class WaterLogThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        return new Thread("water-log") {
            @Override
            public void run() {
                System.err.println("disruptor thread is started, output log water");
                r.run();
                System.err.println("disruptor thread is terminated");
            }
        };
    }
}
