package waterapp.dso;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 路由助手，提供路由检测作用；（防止数据库被恶意刷暴）
 * */
public class RouteHelper {
    private static final Set<String> _set = new HashSet<>();

    public static void add(String path){
        if(!_is_loaded){
            reset();
        }

        _set.add(path);
    }

    public static void del(String path){
        if(!_is_loaded){
            reset();
        }

        _set.remove(path);
    }

    public static boolean has(String path){
        if(!_is_loaded){
            reset();
        }

        return _set.contains(path);
    }

    private static boolean _is_loaded;

    public static synchronized void reset() {
        _is_loaded = true;

        List<String> all_paths = null;
        try {
            all_paths = DbPaaSApi.fileGetPathAll();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (all_paths != null) {
            _set.clear();

            all_paths.forEach(path -> {
                add(path);
            });
        }
    }
}
