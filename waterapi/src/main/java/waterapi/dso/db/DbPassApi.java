package waterapi.dso.db;

import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import waterapi.Config;

import java.sql.SQLException;
import java.util.Date;

/**
 * @author noear 2021/5/26 created
 */
public class DbPassApi {

    private static DbContext db() {
        return Config.water_paas;
    }

    //批量导入
    public static void addJob(String tag, String service, String name, String cron7x, String description, Date nTime) throws SQLException {

        String path = String.format("/%s/_%s/_%s", tag, service, name).replace("-", "_");
        String code = String.format("return water.job('%s','%s');", service, name);

        //只支持新增导入
        DbTableQuery qr = db().table("luffy_file")
                .set("file_type", 1)
                .set("tag", tag)
                .set("label", "")
                .set("note", description)
                .set("path", path)
                .set("edit_mode", "javascript")
                .set("content", code)
                .set("is_disabled", 0)
                .set("is_staticize", 0)
                .set("plan_max", 0)
                .set("create_fulltime", new Date())
                .set("update_fulltime", new Date())
                .set("use_whitelist", "");

        if (TextUtils.isNotEmpty(cron7x)) {
            qr.set("plan_interval", cron7x);
            qr.set("plan_begin_time", nTime.getTime());
        } else {
            qr.set("plan_interval", "");
        }

        qr.insertBy("path");
    }
}
