package wateradmin.dso;

import org.noear.wood.cache.ICacheServiceEx;
import org.noear.wood.cache.LocalCache;

public class CacheUtil {

   public static ICacheServiceEx local = new LocalCache();
   public static ICacheServiceEx data = local;

}
