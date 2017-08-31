package watersev.dao.db;

import noear.weed.DbContext;
import watersev.Config;
import watersev.dao.DisttimeUtil;
import watersev.dao.LogUtil;
import watersev.models.water_msg.DistributionModel;
import watersev.models.water_msg.MessageModel;
import watersev.models.water_msg.SubscriberModel;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by yuety on 2017/7/18.
 */
public class DbMsgApi {
    public static List<Long> getMessageList(int rows, int ntime) throws SQLException{
        return
        Config.water_msg_r.table("message")
                          .where("state=0 AND dist_ntime<?",ntime)
                          .orderBy("msg_id ASC")
                          .limit(rows)
                          .select("msg_id")
                          .getArray("msg_id");
    }

    public static MessageModel getMessage(long msgID) throws SQLException {
        MessageModel m = Config.water_msg_r.table("message")
                .where("msg_id=?", msgID)
                .select("*")
                .getItem(new MessageModel());

        if (m.state != 0) {
            return null;
        } else {
            setMessageState(msgID, 1);

            return m;
        }
    }

    public static boolean setMessageState(long msgID, int state)  {
        try {
            Config.water_msg.table("message")
                    .set("state", state)
                    .where("msg_id=?", msgID)
                    .update();

            return true;
        }catch (Exception ex){
            ex.printStackTrace();

            LogUtil.error("setMessageState",ex);

            return false;
        }
    }

    public static boolean setMessageRepet(MessageModel msg, int state)  {
        try {
            msg.dist_count += 1;

            int ntime = DisttimeUtil.nextTime(msg);

            Config.water_msg.table("message")
                    .set("state", state)
                    .set("dist_ntime", ntime)
                    .set("dist_count", "$dist_count+1")
                    .where("msg_id=?", msg.msg_id)
                    .update();

            return true;
        }catch (SQLException ex){
            ex.printStackTrace();

            LogUtil.error("setMessageRepet",ex);

            return false;
        }
    }

    public static List<SubscriberModel> getSubscriberListByTopic(int topicID) throws SQLException{
        return
        Config.water_msg_r.table("subscriber")
                .where("topic_id=?",topicID)
                .select("*")
                .getList(new SubscriberModel());
    }

    public static void addDistribution(long msgID, SubscriberModel subs) throws SQLException {
        DbContext dbr = Config.water_msg_r;
        DbContext db = Config.water_msg;

        boolean isExists = dbr.table("distribution").where("msg_id=?", msgID).and("subscriber_id=?", subs.subscriber_id).exists();

        if (isExists == false) {
            db.table("distribution")
                    .set("msg_id", msgID)
                    .set("subscriber_id", subs.subscriber_id)
                    .set("alarm_mobile", subs.alarm_mobile)
                    .set("receive_url", subs.receive_url)
                    .set("access_key", subs.access_key)
                    .set("is_sync", subs.is_sync)
                    .set("log_date", "$DATE(NOW())")
                    .set("log_fulltime", "$NOW()")
                    .insert();
        }
    }

    public static List<DistributionModel> getDistributionListByMsg(long msgID) throws SQLException {
        return Config.water_msg_r.table("distribution")
                        .where("msg_id=? AND (state=0 OR state=1)", msgID)
                        .hint("/*TDDL:MASTER*/")
                        .select("*")
                        .getList(new DistributionModel());
    }

    public static boolean setDistributionState(long msg_id,long dist_id, int state) {
        try {
            Config.water_msg.table("distribution")
                    .set("state", state)
                    .where("msg_id=? and dist_id=?",msg_id, dist_id)
                    .update();

            return true;
        }catch (Exception ex){
            ex.printStackTrace();

            LogUtil.error("setDistributionState",ex);

            return false;
        }
    }
}
