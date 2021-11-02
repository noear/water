package waterapi.dso;

import org.noear.water.WW;
import org.noear.water.protocol.ProtocolHub;

public class MsgUtils {
    public static void updateCache(String tags) {
        //
        //初始化时，注册自己会造成缓存更新；此时 messageSource 还未初始化
        //
        if (ProtocolHub.msgBrokerFactory == null) {
            return;
        }

        try {

            ProtocolHub.getMsgSource(null).addMessage(WW.msg_ucache_topic, tags);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
