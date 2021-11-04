package waterapi.dso.db;

import org.noear.weed.*;
import waterapi.Config;
import waterapi.dso.CacheUtils;
import waterapi.models.TopicModel;

import java.sql.SQLException;

/**
 * 消息服务接口
 * */
public final class DbWaterMsgApi {

    private static DbContext db() {
        return Config.water;
    }



    //获取主题ID（没有则创建一个）
    public static TopicModel getTopicById(String topic_name) throws SQLException {
        TopicModel m = db().table("water_msg_topic")
                .whereEq("topic_name", topic_name)
                .caching(CacheUtils.data)
                .selectItem("*", TopicModel.class);

        if (m.topic_id == 0) {
            m.topic_id = (int) (db().table("water_msg_topic")
                    .set("topic_name", topic_name)
                    .insert());
        }

        return m;
    }


    //删除订阅者
    public static boolean removeSubscriber(String key, String topic_name) throws SQLException {
        return db().table("water_msg_subscriber")
                .whereEq("subscriber_key", key).andEq("topic_name", topic_name)
                .delete() > 0;
    }

    //添加订阅者
    public static long addSubscriber(String key, String note, String alarm_mobile, String topic_name, String receive_url, String receive_key, int receive_way, boolean is_unstable) throws SQLException {
        topic_name = topic_name.trim();
        //用于生成主题
        getTopicById(topic_name);


        DbTableQuery tq = db().table("water_msg_subscriber").usingExpr(true)
                .set("alarm_mobile", alarm_mobile)
                .set("is_unstable", (is_unstable ? 1 : 0))
                .set("subscriber_key", key)
                .set("subscriber_note", note)
                .set("receive_url", receive_url)
                .set("receive_key", receive_key)//后面要改掉
                .set("receive_way", receive_way)
                .set("check_error_num", 0)
                .set("log_fulltime", "$NOW()");

        if (db().table("water_msg_subscriber")
                .where("subscriber_key=?", key).and("topic_name=?", topic_name)
                .selectExists()) {
            return tq.whereEq("subscriber_key", key)
                    .andEq("topic_name", topic_name)
                    .update();
        } else {
            return tq.set("topic_name", topic_name)
                    .set("topic_name", topic_name).insert();
        }
    }
}
