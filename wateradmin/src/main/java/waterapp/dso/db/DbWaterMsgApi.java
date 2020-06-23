package waterapp.dso.db;

import org.noear.water.utils.Datetime;
import org.noear.water.utils.StringUtils;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import org.noear.water.utils.TextUtils;
import waterapp.Config;
import waterapp.dso.IDUtil;
import waterapp.models.water_msg.DistributionModel;
import waterapp.models.water_msg.MessageModel;
import waterapp.models.water_msg.SubscriberModel;
import waterapp.models.water_msg.TopicModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbWaterMsgApi {
    private static DbContext db() {
        return Config.water_msg;
    }

    //获取消息列表
    public static List<MessageModel> getMessageList(int dist_count, int topic_id) throws SQLException {
        List<MessageModel> list = new ArrayList<>();
        if (dist_count == 0 && topic_id == 0) {
            return list;
        } else {
            return db().table("water_msg_message").build((tb) -> {
                tb.where("state=0");
                if (dist_count > 0) {
                    tb.and("dist_count>=?", dist_count);
                } else {
                    tb.and("topic_id=?", topic_id);
                }
            }).orderBy("msg_id ASC").limit(50).select("*").getList(new MessageModel());
        }
    }

    public static List<MessageModel> getMessageList(int _m, String key) throws SQLException {
        DbTableQuery qr = db().table("water_msg_message");

        if (_m == 0) {
            qr.whereEq("state", 0).and("dist_count>3");
        } else if (_m == 1) {
            qr.where("state=0");
        } else if (_m == 2) {
            qr.where("state=1");
        } else if (_m == 3) {
            qr.where("state>1");
        } else {
            qr.where("state<0");
        }

        if (key != null) {
            key = key.trim();

            if (key.startsWith("@")) {
                qr.andEq("tags", key.substring(1));
            } else {
                if (StringUtils.isNumeric(key)) {
                    qr.andEq("msg_id", Integer.parseInt(key));
                } else {
                    qr.andEq("topic_name", key);
                }
            }
        }

        return qr.orderBy("msg_id DESC").limit(50).select("*").getList(new MessageModel());
    }

    public static List<MessageModel> getMessageByWaitList() throws SQLException {
        return db().table("water_msg_message").build((tb) -> {
            tb.where("state=1");
        }).orderBy("msg_id ASC").limit(50).select("*").getList(new MessageModel());
    }

    //派发功能
    public static boolean msgDistribute(List<Object> ids) throws SQLException {
        return db().table("water_msg_message")
                .whereIn("msg_id", ids).andNeq("state", 2)
                .set("state", 0)
                .set("dist_nexttime", 0)
                .update() > 0;

    }

    //获取主题ID（没有则创建一个）
    public static TopicModel getTopicID(String topic) throws SQLException {
        TopicModel m = db().table("water_msg_topic")
                .where("topic_name=?", topic)
                .select("*")
                .getItem(new TopicModel());

        if (m.topic_id == 0) {
            m.topic_id = (int) (db().table("water_msg_topic")
                    .set("topic_name", topic)
                    .insert());
        }

        return m;
    }

    public static MessageModel getMessageByKey(String msg_key) throws SQLException {

        if (TextUtils.isEmpty(msg_key)) {
            return new MessageModel();
        }


        return db().table("water_msg_message")
                .build((tb) -> {
                    if (IDUtil.isNumeric(msg_key)) {
                        tb.where("msg_id = ?", Long.parseLong(msg_key));
                    } else {
                        tb.where("msg_key = ?", msg_key);
                    }
                })
                .select("*")
                .getItem(new MessageModel());
    }

    public static SubscriberModel getSubscriber(int topic_id) throws SQLException {
        return db().table("water_msg_subscriber")
                .where("topic_id = ?", topic_id)
                .select("*")
                .getItem(new SubscriberModel());
    }

    public static MessageModel getMessageById(long msg_id) throws SQLException {
        return db().table("water_msg_message")
                .where("msg_id = ?", msg_id)
                .limit(1)
                .select("*")
                .getItem(new MessageModel());
    }

    //肖除一个状态的消息 //不包括0,2
    public static int deleteMsg(int state) throws SQLException {
        if (state == 0 || state == 1) {
            return -1;
        }

        int date = Datetime.Now().addDay(-3).getDate();

        db().table("#d")
                .from("water_msg_distribution d,water_msg_message m")
                .where(" d.msg_id = m.msg_id AND m.log_date<=? and m.state=?", date, state)
                .delete();


        return db().table("water_msg_message")
                .where("log_date<=? AND state=?", date, state)
                .delete();
    }

    //查询订阅列表(全部列表以及查询功能)
    public static List<SubscriberModel> getSubscriberList(String topic_name, int is_enabled) throws SQLException {
        return db().table("water_msg_subscriber")
                .where("is_enabled = ?", is_enabled)
                .build(tb -> {
                    if (TextUtils.isEmpty(topic_name) == false) {
                        tb.and("(topic_name like ? OR subscriber_note like ? )", "%" + topic_name + "%", "%" + topic_name + "%");
                    }
                })
                .orderBy("topic_name asc")
                .select("*")
                .getList(new SubscriberModel());
    }

    public static List<TopicModel> getTopicList(String topic_name) throws SQLException {
        return db().table("water_msg_topic")
                .where("1 = 1")
                .build(tb -> {
                    if (TextUtils.isEmpty(topic_name) == false) {
                        if (StringUtils.isNumeric(topic_name)) {
                            tb.andEq("topic_id", Integer.parseInt(topic_name));
                        } else {
                            tb.andLk("topic_name", "%" + topic_name + "%");
                        }
                    }
                }).orderBy("topic_name ASC")
                .select("*")
                .getList(new TopicModel());
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
                .select("*")
                .getItem(new TopicModel());
    }

    public static boolean updateTopic(String topic_name, int topic_id, int max_msg_num, int max_distribution_num, int max_concurrency_num, int alarm_model) throws SQLException {
        DbTableQuery tb = db().table("water_msg_topic")
                .set("max_msg_num", max_msg_num)
                .set("max_distribution_num", max_distribution_num)
                .set("max_concurrency_num", max_concurrency_num)
                .set("alarm_model", alarm_model);
        if (topic_id > 0) {
            return tb.where("topic_id = ?", topic_id).update() > 0;
        } else {
            tb.set("topic_name", topic_name);
            return tb.insert() > 0;
        }
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
                .update() > 0;
    }

    //获得异常消息的dist_id和subscriber_id。
    public static List<DistributionModel> repairSubs1(List<Object> ids) throws SQLException {
        return db().table("water_msg_distribution")
                .whereIn("msg_id", ids)
                .select("dist_id,subscriber_id")
                .getList(new DistributionModel());
    }

    //根据查询到的subscriber_id查询订阅者url。
    public static SubscriberModel repairSubs2(int subscriber_id) throws SQLException {
        return db().table("water_msg_subscriber")
                .where("subscriber_id = ?", subscriber_id)
                .select("receive_url")
                .getItem(new SubscriberModel());
    }

    //更新distribution中url
    public static boolean repairSubs3(long dist_id, String receive_url) throws SQLException {
        return db().table("water_msg_distribution")
                .where("dist_id = ?", dist_id)
                .set("receive_url", receive_url)
                .update() > 0;
    }

    //取消派发
    public static boolean cancelSend(List<Object> ids) throws SQLException {
        return db().table("water_msg_message")
                .whereIn("msg_id", ids)
                .set("state", -1)
                .update() > 0;
    }
}
