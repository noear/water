package watersev.dso;

import org.noear.luffy.model.AFileModel;
import org.noear.luffy.utils.TextUtils;
import org.noear.weed.DbContext;
import watersev.Config;

import java.util.ArrayList;
import java.util.List;

public class DbLuffyApi {
    private static DbContext db() {
        return Config.water_paas;
    }

    public static AFileModel fileGet(String path) throws Exception {
        return db().table("luffy_file")
                .where("path=?", path)
                .select("*")
                .getItem(AFileModel.class);
    }

    public static AFileModel fileGet(int file_id) throws Exception {
        return db().table("luffy_file")
                .whereEq("file_id", file_id)
                .select("*")
                .getItem(AFileModel.class);
    }

    public static List<AFileModel> fileGetPaths(String tag, String label, boolean isCache) throws Exception {
        if (TextUtils.isEmpty(tag) && TextUtils.isEmpty(label)) {
            return new ArrayList<>();
        }

        return db().table("luffy_file")
                .where("1=1")
                .andIf(TextUtils.isEmpty(tag) == false, "tag=?", tag)
                .andIf(TextUtils.isEmpty(label) == false, "label=?", label)
                .select("path, note")
                .caching(Config.cache_file)
                .usingCache(isCache)
                .getList(AFileModel.class);
    }
}
