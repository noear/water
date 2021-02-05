package waterapi.dso;

import org.noear.water.WW;
import org.noear.water.protocol.ProtocolHub;
import waterapi.dso.db.DbWaterMsgApi;
import waterapi.models.TopicModel;

public class MsgUtils {
    public static void updateCache(String tags) {
        try {
            TopicModel topicModel = DbWaterMsgApi.getTopicById(WW.msg_ucache_topic);

            ProtocolHub.messageSource().addMessage(topicModel.topic_id, WW.msg_ucache_topic, tags);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
