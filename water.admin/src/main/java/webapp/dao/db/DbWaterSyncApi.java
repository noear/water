package webapp.dao.db;

import org.noear.water.tools.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import webapp.Config;
import webapp.models.water.SynchronousModel;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class DbWaterSyncApi {
    private static DbContext db() {
        return Config.water;
    }

    //根据名称和状态列出同步列表。
    public static List<SynchronousModel> getSyncList(String tag, String name, int _state) throws SQLException {
        return db()
                .table("water_base_synchronous")
                .where("is_enabled = ?", _state)
                .and("tag = ?", tag)
                .expre(tb -> {
                    if (!TextUtils.isEmpty(name))
                        tb.and("name like ?", name + "%");
                })
                .select("*")
                .getList(new SynchronousModel());
    }

    public static boolean addSync(Integer type, String name, Integer interval, String target, String target_pk, String source,
                                  String source_model, String alarm_mobile, Integer is_enabled) throws SQLException {
        return db().table("water_base_synchronous")
                .set("key", UUID.randomUUID().toString().replaceAll("-", ""))
                .set("name", name)
                .set("type", type)
                .set("interval", interval)
                .set("target", target)
                .set("target_pk", target_pk)
                .set("source", source)
                .set("source_model", source_model)
                .set("alarm_mobile", alarm_mobile)
                .set("is_enabled", is_enabled)
                .insert() > 0;
    }

    //数据同步  根据id获得对象
    public static SynchronousModel getSyncById(int sync_id) throws SQLException {
        return db().table("water_base_synchronous")
                .where("id = ?", sync_id)
                .limit(1)
                .select("*")
                .getItem(new SynchronousModel());
    }

    public static boolean updSync(Integer sync_id, Integer type, String name, String tag, Integer interval, String target, String target_pk, String source,
                                  String source_model, String alarm_mobile, Integer is_enabled) throws SQLException {
        DbTableQuery db = db().table("water_base_synchronous")
                .set("name", name)
                .set("tag", tag)
                .set("type", type)
                .set("interval", interval)
                .set("target", target)
                .set("target_pk", target_pk)
                .set("source", source)
                .set("source_model", source_model)
                .set("alarm_mobile", alarm_mobile)
                .set("is_enabled", is_enabled);
        if (sync_id > 0) {
            return db.where("id = ?", sync_id).update() > 0;
        } else {
            return db.insert() > 0;
        }
    }

    //获取synchronous表的tag分组信息。
    public static List<SynchronousModel> getSyncTags() throws SQLException {
        return db().table("water_base_synchronous")
                .groupBy("tag")
                .select("tag,count(*) counts")
                .getList(new SynchronousModel());
    }
}
