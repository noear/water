package waterfaas.dso.db;

import org.noear.luffy.model.AFileModel;
import org.noear.luffy.utils.TextUtils;
import org.noear.wood.DbContext;
import waterfaas.Config;

import java.util.ArrayList;
import java.util.List;

public class DbLuffyApi {
    private static DbContext db() {
        return Config.water_paas;
    }

    public static AFileModel fileGet(String path) throws Exception {
        return db().table("luffy_file")
                .whereEq("path", path)
                .selectItem("*", AFileModel.class);
    }

    public static AFileModel fileGet(int file_id) throws Exception {
        return db().table("luffy_file")
                .whereEq("file_id", file_id)
                .selectItem("*", AFileModel.class);
    }

    public static List<AFileModel> fileGetPaths(String tag, String label, boolean isCache) throws Exception {
        if (TextUtils.isEmpty(tag) && TextUtils.isEmpty(label)) {
            return new ArrayList<>();
        }

        return db().table("luffy_file")
                .where("1=1")
                .andIf(TextUtils.isEmpty(tag) == false, "tag=?", tag)
                .andIf(TextUtils.isEmpty(label) == false, "label=?", label)
                .caching(Config.cache_file)
                .usingCache(isCache)
                .selectList("path, note", AFileModel.class);
    }

    public static List<String> fileGetPathAll() throws Exception {
        return db().table("luffy_file")
                .selectArray("path");
    }


    public static List<AFileModel> pathFilters() throws Exception {
        return db().table("luffy_file")
                .where("`label` = ?", Config.faas_filter_path)
                .selectList("path,note", AFileModel.class);

    }
}
