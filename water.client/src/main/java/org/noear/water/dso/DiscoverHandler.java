package org.noear.water.dso;

import org.noear.water.model.DiscoverM;

/**
 * @author noear 2021/1/18 created
 */
@FunctionalInterface
public interface DiscoverHandler {
    void handler(DiscoverM discover);
}
