package waterapi.dao.db;

import noear.weed.DataList;
import noear.weed.DbContext;
import waterapi.Config;
import waterapi.utils.EncryptUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by yuety on 2017/7/18.
 */
public final class DbSevApi {
    public static DbContext db() {
        return Config.water;
    }

    public static DbContext dbr() {
        return Config.water_r;
    }

    public static DataList getServiceList() throws SQLException {
        return dbr().table("service").orderBy("name").select("*").getDataList();
    }

    public static void setState1As0() throws SQLException {
        db().table("service")
                .set("state", 0)
                .where("state=?",1)
                .update();
    }

    public static void addService(String service, String address, String note, String alarm_mobile ,String check_url, int check_type) throws SQLException {
        if (address.startsWith("192.168.")) {
            return;
        }

        String key = EncryptUtil.md5(service + "#" + address);

        boolean isOk = db().table("service")
                .set("note", note)
                .set("alarm_mobile",alarm_mobile)
                .set("check_url", check_url)
                .set("check_type", check_type)
                .set("check_last_state", 0)
                .set("check_last_time", "$NOW()")
                .set("check_last_note", "")
                .where("`key`=?", key)
                .update() > 0;


        if (isOk == false) {
            db().table("service")
                    .set("`key`", key)
                    .set("name", service)
                    .set("address", address)
                    .set("note", note)
                    .set("alarm_mobile",alarm_mobile)
                    .set("check_url", check_url)
                    .set("check_type", check_type)
                    .set("check_last_state", 0)
                    .set("check_last_time", "$NOW()")
                    .set("check_last_note", "")
                    .insert();
        }
    }
}
