package org.slf4j.impl;

import org.slf4j.spi.MDCAdapter;

/**
 * @author noear
 * @since 1.0
 */
public class StaticMDCBinder {
    public static final StaticMDCBinder SINGLETON = new StaticMDCBinder();

    private StaticMDCBinder() {
    }

    public static final StaticMDCBinder getSingleton() {
        return SINGLETON;
    }

    public MDCAdapter getMDCA() {
        return new LocalMDCAdapter();
    }

    public String getMDCAdapterClassStr() {
        return LocalMDCAdapter.class.getName();
    }
}
