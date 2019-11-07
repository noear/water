package webapp.dao.db;

import org.noear.water.tools.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import webapp.Config;
import webapp.models.water.AccountModel;

import java.sql.SQLException;
import java.util.List;

public class DbWaterAccountApi {
    private static DbContext db() {
        return Config.water;
    }

    //登陆
    public static AccountModel getAccount(String access_id, String access_key) throws Exception {
        return db().table("water_base_account")
                .where("id = ?", access_id)
                .and("access_key = ?", access_key)
                .select("*")
                .getItem(AccountModel.class);
    }

    public static AccountModel getAccountById(int account_id) throws Exception {
        return db().table("water_base_account")
                .where("id = ?", account_id)
                .select("*")
                .getItem(AccountModel.class);
    }


    //获取账号的手机号（用于报警）
    public static List<String> getAccountMobiles() throws SQLException {
        return Config.water.table("water_base_account").where("LENGTH(alarm_mobile) > 0")
                .select("alarm_mobile")
                .getArray("alarm_mobile");
    }


    public static List<AccountModel> getAccountListByName(String name) throws Exception {
        return db().table("water_base_account")
                .where("1 = 1")
                .expre(tb -> {
                    if (TextUtils.isEmpty(name) == false) {
                        tb.and("name like ?", "%"+name + "%");
                    }
                })
                .select("*")
                .getList(AccountModel.class);
    }

    public static boolean addAccount(String name, String alarm_mobile, String note, String access_id, String access_key) throws SQLException {
        return db().table("water_base_account")
                .set("name", name)
                .set("alarm_mobile", alarm_mobile)
                .set("access_id", access_id)
                .set("access_key", access_key)
                .set("note", note)
                .insert() > 0;
    }

    public static boolean setAccount(Integer account_id, String name, String alarm_mobile, String note, String access_id, String access_key) throws SQLException {
        DbTableQuery db = db().table("water_base_account")
                .set("name", name)
                .set("alarm_mobile", alarm_mobile)
                .set("access_id", access_id)
                .set("access_key", access_key)
                .set("note", note);
        if (account_id > 0) {
            return db.where("id = ?", account_id).update() > 0;
        } else {
            return db.insert() > 0;
        }
    }
}
