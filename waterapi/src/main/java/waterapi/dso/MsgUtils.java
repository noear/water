package waterapi.dso;

import org.noear.water.WW;
import org.noear.water.protocol.ProtocolHub;
import waterapi.dso.db.DbWaterMsgApi;
import waterapi.models.TopicModel;

public class MsgUtils {
    public static void updateCache(String tags) {
        //
        //初始化时，注册自己会造成缓存更新；此时 messageSource 还未初始化
        //
        if(ProtocolHub.msgSource() == null){
            return;
        }

        try {
            TopicModel topicModel = DbWaterMsgApi.getTopicById(WW.msg_ucache_topic);

            ProtocolHub.msgSource().addMessage(topicModel.topic_id, WW.msg_ucache_topic, tags);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
