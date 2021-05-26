package waterapi.dso.db;

import org.noear.weed.DbContext;
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
    public static void addJob(String tag, String service, String name, String description) throws SQLException {

        String path = String.format("/%s/_%s/_%s", tag, service, name);
        String code = String.format("return water.job('%s','%s');", service, name);

        //只支持新增导入
        db().table("paas_file")
                .set("file_type", 1)
                .set("tag", tag)
                .set("label", "")
                .set("note", description)
                .set("path", path)
                .set("edit_mode", "javascript")
                .set("content", code)
                .set("is_disabled", 0)
                .set("is_staticize", 0)
                .set("plan_interval", "1h")
                .set("plan_max", 0)
                .set("create_fulltime", new Date())
                .set("update_fulltime", new Date())
                .set("alarm_sign", "")
                .set("alarm_mobile", "")
                .set("use_whitelist", "")
                .insertBy("path");
    }
}
