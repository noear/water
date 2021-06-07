package org.noear.water.dso;

import org.noear.water.model.DiscoverM;

/**
 * 发现处理
 *
 * @author noear
 * @since 2.0
 */
@FunctionalInterface
public interface DiscoverHandler {
    void handler(DiscoverM discover);
}
