package org.noear.water.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String... args) throws InterruptedException {
        LOG.trace("test");
        LOG.debug("test1");
        LOG.info("test2");
        Thread.sleep(100);
        LOG.warn("test2");
        LOG.error("test3");
    }
}
