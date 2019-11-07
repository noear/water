package webapp.dao.db;

import org.noear.water.tools.Datetime;
import org.noear.water.tools.DisttimeUtil;
import org.noear.water.tools.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import webapp.Config;
import webapp.dao.IDUtil;
import webapp.models.water_msg.DistributionModel;
import webapp.models.water_msg.MessageModel;
import webapp.models.water_msg.SubscriberModel;
import webapp.models.water_msg.TopicModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DbWaterMsgApi {
    private static DbContext db() {
        return Config.water_msg;
    }

    //将状态为1的消息重置为0 //只处理已超时的状态1消息
    public static int resetMsg() throws SQLException {
        int ntime = DisttimeUtil.currTime(-10); //已过期10秒

        return db().table("message")
                .set("state", 0)
                .where("state=1 AND dist_ntime<?",ntime) //只处理已超时的状态1消息
                .update();
    }

    //获取消息列表
    public static List<MessageModel> getMessageList(int dist_count, int topic_id) throws SQLException{
        List<MessageModel> list = new ArrayList<>();
        if(dist_count == 0 && topic_id ==0) {
            return list;
        }
        else {
            return db().table("message").expre((tb) -> {
                tb.where("state=0");
                if (dist_count > 0) {
                    tb.and("dist_count>=?", dist_count);
                } else {
                    tb.and("topic_id=?", topic_id);
                }
            }).orderBy("msg_id ASC").limit(50).select("*").getList(new MessageModel());
        }
    }

    public static List<MessageModel> getMessageByWaitList() throws SQLException {
        return db().table("message").expre((tb) -> {
            tb.where("state=1");
        }).orderBy("msg_id ASC").limit(50).select("*").getList(new MessageModel());
    }
    //派发功能
    public static boolean msgDistribute(Long msg_id) throws SQLException {
        return db().table("message")
                .where("msg_id = ? AND state<>2",msg_id)
                .set("state",0)
                .set("dist_ntime",1)
                .update()>0;

    }
    //获取主题ID（没有则创建一个）
    public static TopicModel getTopicID(String topic) throws SQLException{
        TopicModel m = db().table("topic")
                .where("topic_name=?",topic)
                .select("*")
                .getItem(new TopicModel());

        if(m.topic_id == 0){
            m.topic_id = (int)(db().table("topic")
                    .set("topic_name",topic)
                    .insert());
        }

        return m;
    }

    public static MessageModel getMessageByKey(String msg_key) throws SQLException {

        if(TextUtils.isEmpty(msg_key)){
            return new MessageModel();
        }


        return db().table("message")
                .expre((tb) -> {
                    if (IDUtil.isNumeric(msg_key)) {
                        tb.where("msg_id = ?", Long.parseLong(msg_key));
                    } else {
                        tb.where("msg_key = ?", msg_key);
                    }
                })
                .select("*")
                .getItem(new MessageModel());
    }

    public static SubscriberModel getSubscriber(int topic_id) throws SQLException{
        return db().table("subscriber")
                .where("topic_id = ?",topic_id)
                .select("*")
                .getItem(new SubscriberModel());
    }

    public static MessageModel getMessageById(long msg_id) throws SQLException{
        return db().table("message")
                .where("msg_id = ?",msg_id)
                .limit(1)
                .select("*")
                .getItem(new MessageModel());
    }

    //肖除一个状态的消息 //不包括0,2
    public static int deleteMsg(int state) throws SQLException {
        if(state == 0 || state==1){
            return -1;
        }

        int date = Datetime.Now().addDay(-3).getDate();

        db().table("#d")
                .from("distribution d,message m")
                .where(" d.msg_id = m.msg_id AND m.log_date<=? and m.state=?",date,state)
                .delete();


        return db().table("$.message")
                   .where("log_date<=? AND state=?",date,state)
                   .delete();
    }

    //查询订阅列表(全部列表以及查询功能)
    public static List<SubscriberModel> getSubscriberList(String topic_name,int is_enabled) throws SQLException {
        return db().table("subscriber")
                .where("is_enabled = ?",is_enabled)
                .expre(tb->{
                if (TextUtils.isEmpty(topic_name) == false) {
                    tb.and("(topic_name like ? OR subscriber_note like ? )", "%"+topic_name + "%","%"+topic_name + "%");
                }
                })
                .orderBy("topic_name asc")
                .select("*")
                .getList(new SubscriberModel());
    }

    public static List<TopicModel> getTopicList(String topic_name) throws SQLException{
        return db().table("topic")
                .where("1 = 1")
                .expre(tb->{
                    if (TextUtils.isEmpty(topic_name) == false) {
                        tb.and("topic_name like ?", "%"+topic_name + "%");
                    }
                }).orderBy("topic_name ASC")
                .select("*")
                .getList(new TopicModel());
    }



    //添加主题
    public static boolean addTopic(String topic_name,int max_msg_num,int max_distribution_num,int max_concurrency_num,int alarm_model) throws SQLException{
        return db().table("topic")
                .set("topic_name",topic_name)
                .set("max_msg_num",max_msg_num)
                .set("max_distribution_num",max_distribution_num)
                .set("max_concurrency_num",max_concurrency_num)
                .set("alarm_model",alarm_model)
                .insert() > 0;
    }

    public static TopicModel getTopicByID(int topic_id) throws SQLException{
        return db().table("topic")
                .where("topic_id = ?",topic_id)
                .select("*")
                .getItem(new TopicModel());
    }

    public static boolean updateTopic(String topic_name,int topic_id,int max_msg_num,int max_distribution_num,int max_concurrency_num,int alarm_model) throws SQLException {
        DbTableQuery tb = db().table("topic")
                .set("max_msg_num", max_msg_num)
                .set("max_distribution_num", max_distribution_num)
                .set("max_concurrency_num", max_concurrency_num)
                .set("alarm_model", alarm_model);
        if (topic_id > 0) {
            return tb.where("topic_id = ?", topic_id).update() > 0;
        } else {
            tb.set("topic_name",topic_name);
            return tb.insert() > 0;
        }
    }

    //订阅列表删除功能。
    public static boolean deleteSubs(Integer subscriber_id) throws SQLException {
        return db().table("subscriber")
                .where("subscriber_id = ?",subscriber_id)
                .delete()>0;
    }

    //订阅列表 启用/禁用
    public static boolean enabledSubs(int subscriber_id,int is_enabled) throws SQLException{
        return db().table("subscriber")
                .where("subscriber_id = ?",subscriber_id)
                .set("is_enabled",is_enabled)
                .update() > 0;
    }

    //获得异常消息的dist_id和subscriber_id。
    public static List<DistributionModel> repairSubs1(Integer msg_id) throws SQLException {
        return db().table("distribution")
                .where("msg_id = ?",msg_id)
                .select("dist_id,subscriber_id")
                .getList(new DistributionModel());
    }

    //根据查询到的subscriber_id查询订阅者url。
    public static SubscriberModel repairSubs2(int subscriber_id) throws SQLException {
        return db().table("subscriber")
                    .where("subscriber_id = ?",subscriber_id)
                    .select("receive_url")
                    .getItem(new SubscriberModel());
    }
    //更新distribution中url
    public static boolean repairSubs3(long dist_id, String receive_url) throws SQLException {
        return db().table("distribution")
                    .where("dist_id = ?",dist_id)
                    .set("receive_url",receive_url)
                    .update()>0;
    }

    //取消派发
    public static boolean cancelSend(long msg_id) throws SQLException{
        return db().table("message")
                .where("msg_id = ?", msg_id)
                .set("state", -1)
                .update() > 0;
    }


}
