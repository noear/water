package watersev.dso.db;

import org.noear.water.protocol.model.message.SubscriberModel;
import org.noear.water.utils.TextUtils;
import org.noear.wood.DbContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import watersev.Config;
import watersev.models.water_msg.TopicModel;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by noear on 2017/7/18.
 */
public class DbWaterMsgApi {
    private static Logger log_msg = LoggerFactory.getLogger("water_log_msg");

    public static DbContext db() {
        return Config.water;
    }

    //获取主题
    public static TopicModel getTopic(String topic) throws SQLException {
        TopicModel m = db().table("water_msg_topic")
                .where("topic_name=?", topic)
                .caching(Config.cache_data)
                .selectItem("*", TopicModel.class);

        return m;
    }

    //根据主题获取订阅者
    public static Map<Long, SubscriberModel> getSubscriberListByTopic(String topic_name) throws SQLException {
        Map<Long, SubscriberModel> map = new HashMap<>();

        List<SubscriberModel> list = db().table("water_msg_subscriber")
                .where("topic_name=? AND is_enabled=1", topic_name)
                .caching(Config.cache_data).usingCache(10)
                .selectList("*", SubscriberModel.class);

        list.forEach(m -> {
            map.put(m.subscriber_id, m);
        });

        return map;
    }

    public static List<SubscriberModel> getSubscriberListNoCache() throws SQLException {
        List<SubscriberModel> list = db().table("water_msg_subscriber")
                .where("is_enabled=1")
                .caching(Config.cache_data)
                .selectList("*", SubscriberModel.class);

        return list;
    }

    public static void delSubscriberByError(String receive_url) {
        if (TextUtils.isNotEmpty(receive_url)) {
            try {
                db().table("water_msg_subscriber")
                        .whereEq("receive_url", receive_url)
                        .andEq("is_unstable", 1)
                        .andGte("check_error_num", 2)
                        .delete();
            } catch (Throwable ex) {
                //EventBus.pushAsyn(ex);
                MDC.put("tag0", "delSubscriber");

                log_msg.error("{}", ex);
            }
        }
    }

    public static void setSubscriberState(String receive_url, int check_state) {
        if (TextUtils.isNotEmpty(receive_url)) {
            try {
                db().table("water_msg_subscriber").usingExpr(true)
                        .set("check_last_state", check_state)
                        .set("gmt_modified", System.currentTimeMillis())
                        .build((tb) -> {
                            if (check_state == 200)
                                tb.set("check_error_num", 0);
                            else
                                tb.set("check_error_num", "$check_error_num+1");
                        })
                        .whereEq("receive_url", receive_url)
                        .update();
            } catch (Throwable ex) {
                MDC.put("tag0", "setSubscriberState");

                log_msg.error("{}\r\n{}", receive_url + "", ex);
            }
        }
    }
}
