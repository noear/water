package wateradmin.dso.db;

import org.noear.solon.Utils;
import org.noear.water.protocol.model.message.SubscriberModel;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import org.noear.water.utils.TextUtils;
import wateradmin.Config;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_msg.TopicModel;

import java.sql.SQLException;
import java.util.List;

public class DbWaterMsgApi {
    private static DbContext db() {
        return Config.water;
    }


    //获取主题ID（没有则创建一个）
    public static TopicModel getTopicID(String topic) throws SQLException {
        TopicModel m = db().table("water_msg_topic")
                .where("topic_name=?", topic)
                .selectItem("*", TopicModel.class);

        if (m.topic_id == 0) {
            m.topic_id = (int) (db().table("water_msg_topic")
                    .set("topic_name", topic)
                    .set("gmt_create", System.currentTimeMillis())
                    .insert());
        }

        return m;
    }


    public static SubscriberModel getSubscriber(String topic) throws SQLException {
        return db().table("water_msg_subscriber")
                .whereEq("topic_name", topic)
                .andEq("is_enabled", 1)
                .limit(1)
                .selectItem("*", SubscriberModel.class);
    }

    public static List<TagCountsModel> getSubscriberTagList() throws SQLException {
        return db().table("water_msg_subscriber")
                .groupBy("tag")
                .orderBy("tag asc")
                .selectList("tag, count(*) counts", TagCountsModel.class);
    }

    public static List<TagCountsModel> getSubscriberNameList(String tag_name) throws SQLException {
        return db().table("water_msg_subscriber")
                .whereEq("tag", tag_name)
                .groupBy("name")
                .orderBy("name asc")
                .selectList("name tag, count(*) counts", TagCountsModel.class);
    }

    //查询订阅列表(全部列表以及查询功能)
    public static List<SubscriberModel> getSubscriberList(String tag_name, String name,String topic_name, int is_enabled) throws SQLException {
        DbTableQuery qr = db().table("water_msg_subscriber")
                .where("is_enabled = ?", is_enabled);

        if ("_".equals(tag_name)) {
            qr.andEq("tag", "");
        } else {
            if (Utils.isNotEmpty(tag_name)) {
                qr.andEq("tag", tag_name);
            }
        }

        return qr.andIf(Utils.isNotEmpty(name), "name=?", name)
                .build(tb -> {
                    if (TextUtils.isEmpty(topic_name) == false) {
                        String key = "%" + topic_name + "%";
                        tb.and().begin("topic_name LIKE ? ", key)
                                .or("name LIKE ?", key)
                                .or("receive_url LIKE ?", key)
                                .end();
                    }
                })
                .orderBy("topic_name asc")
                .limit(500) //在分页之前，挡一下（不分页，按规模调配后，基本也是够了）
                .selectList("*", SubscriberModel.class);
    }

    public static List<TagCountsModel> getTopicTagList() throws SQLException {
        return db().table("water_msg_topic")
                .groupBy("tag")
                .orderBy("tag")
                .selectList("tag, count(*) counts", TagCountsModel.class);
    }

    public static List<TopicModel> getTopicList(String tag_name,String topic_name, String sort) throws SQLException {
        DbTableQuery qr = db().table("water_msg_topic")
                .where("1 = 1");

        if ("_".equals(tag_name)) {
            qr.andEq("tag", "");
        } else {
            if (Utils.isNotEmpty(tag_name)) {
                qr.andEq("tag", tag_name);
            }
        }

        return qr.build(tb -> {
            if (TextUtils.isEmpty(topic_name) == false) {
                if (TextUtils.isNumeric(topic_name)) {
                    tb.andEq("topic_id", Integer.parseInt(topic_name));
                } else {
                    tb.andLk("topic_name", "%" + topic_name + "%");
                }
            }


            if (TextUtils.isEmpty(sort)) {
                tb.orderBy("topic_name ASC");
            } else {
                tb.orderBy("stat_msg_day_num DESC,topic_name ASC");
            }

        }).selectList("*", TopicModel.class);
    }


    //添加主题
    public static boolean addTopic(String topic_name, int max_msg_num, int max_distribution_num, int max_concurrency_num, int alarm_model) throws SQLException {
        return db().table("water_msg_topic")
                .set("topic_name", topic_name)
                .set("max_msg_num", max_msg_num)
                .set("max_distribution_num", max_distribution_num)
                .set("max_concurrency_num", max_concurrency_num)
                .set("alarm_model", alarm_model)
                .insert() > 0;
    }

    public static TopicModel getTopicByID(int topic_id) throws SQLException {
        return db().table("water_msg_topic")
                .where("topic_id = ?", topic_id)
                .selectItem("*", TopicModel.class);
    }

    public static boolean updateTopic(String tag,String topic_name, int topic_id, int max_msg_num, int max_distribution_num, int max_concurrency_num, int alarm_model) throws SQLException {
        DbTableQuery tb = db().table("water_msg_topic")
                .set("tag", tag)
                .set("max_msg_num", max_msg_num)
                .set("max_distribution_num", max_distribution_num)
                .set("max_concurrency_num", max_concurrency_num)
                .set("alarm_model", alarm_model)
                .set("gmt_modified", System.currentTimeMillis());

        if (topic_id > 0) {
            return tb.where("topic_id = ?", topic_id).update() > 0;
        } else {
            tb.set("topic_name", topic_name);
            return tb.insert() > 0;
        }
    }

    public static boolean deleteTopic(int topic_id) throws SQLException {
        db().table("water_msg_topic")
                .whereEq("topic_id", topic_id)
                .delete();

        return true;
    }

    //订阅列表删除功能。
    public static boolean deleteSubs(List<Object> ids) throws SQLException {
        return db().table("water_msg_subscriber")
                .whereIn("subscriber_id", ids)
                .delete() > 0;
    }

    //订阅列表 启用/禁用
    public static boolean enabledSubs(List<Object> ids, int is_enabled) throws SQLException {
        return db().table("water_msg_subscriber")
                .whereIn("subscriber_id", ids)
                .set("is_enabled", is_enabled)
                .set("gmt_modified", System.currentTimeMillis())
                .update() > 0;
    }

    //根据查询到的subscriber_id查询订阅者url。
    public static SubscriberModel repairSubs2(long subscriber_id) throws SQLException {
        return db().table("water_msg_subscriber")
                .where("subscriber_id = ?", subscriber_id)
                .selectItem("receive_url", SubscriberModel.class);
    }
}
