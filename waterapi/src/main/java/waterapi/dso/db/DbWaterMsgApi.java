package waterapi.dso.db;

import org.noear.weed.*;
import waterapi.Config;
import waterapi.dso.CacheUtils;
import waterapi.dso.TopicPipelineLocal;

import java.sql.SQLException;

/**
 * 消息服务接口
 * */
public final class DbWaterMsgApi {

    private static DbContext db() {
        return Config.water;
    }

    //获取主题ID（没有则创建一个）
    public static void tryAddTopic(String topic_name) throws SQLException {
        boolean ieExists = db().table("water_msg_topic")
                .whereEq("topic_name", topic_name)
                .caching(CacheUtils.data)
                .selectExists();

        if (ieExists == false) {
            db().table("water_msg_topic")
                    .set("topic_name", topic_name)
                    .set("gmt_create", System.currentTimeMillis())
                    .insert();
        }
    }


    //删除订阅者
    public static boolean removeSubscriber(String key, String topic_name) throws SQLException {
        return db().table("water_msg_subscriber")
                .whereEq("subscriber_key", key).andEq("topic_name", topic_name)
                .delete() > 0;
    }

    //添加订阅者
    public static long addSubscriber(String key, String name, String tag, String alarm_mobile, String topic_name, String receive_url, String receive_key, int receive_way, boolean is_unstable) throws SQLException {
        topic_name = topic_name.trim();

        //注册主题
        TopicPipelineLocal.singleton().add(topic_name);

        DbTableQuery tq = db().table("water_msg_subscriber").usingExpr(true)
                .set("alarm_mobile", alarm_mobile)
                .set("is_unstable", (is_unstable ? 1 : 0))
                .set("subscriber_key", key)
                .set("name", name)
                .set("tag", tag)
                .set("subscriber_note", name)
                .set("receive_url", receive_url)
                .set("receive_key", receive_key)//后面要改掉
                .set("receive_way", receive_way)
                .set("check_error_num", 0)
                .set("gmt_modified", System.currentTimeMillis());

        if (db().table("water_msg_subscriber")
                .where("subscriber_key=?", key).and("topic_name=?", topic_name)
                .selectExists()) {
            return tq.whereEq("subscriber_key", key)
                    .andEq("topic_name", topic_name)
                    .update();
        } else {
            return tq.set("topic_name", topic_name)
                    .set("gmt_create", System.currentTimeMillis())
                    .insert();
        }
    }
}
