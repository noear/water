package waterapp.dso;

import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;

public class CacheUtil {

   public static ICacheServiceEx data = new LocalCache();

}
