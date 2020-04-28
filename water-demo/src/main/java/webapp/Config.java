package webapp;

import org.noear.solon.annotation.XConfiguration;
import org.noear.water.annotation.Water;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;

@XConfiguration
public class Config {
    @Water("water/water")
    DbContext waterDb;

    @Water("water/water_cache")
    ICacheServiceEx cache;

    @Water("water/paas_uri")
    String paas_uri;

    @Water("water/is_debug")
    Integer is_debug;
}
