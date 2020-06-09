package waterapp.dso;

import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;

public class CacheUtil {

   public static ICacheServiceEx local = new LocalCache();
   public static ICacheServiceEx data = local;

}
