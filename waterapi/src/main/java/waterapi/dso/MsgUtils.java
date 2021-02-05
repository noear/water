package waterapi.dso;

import org.noear.water.protocol.ProtocolHub;
import waterapi.dso.db.DbWaterMsgApi;

public class MsgUtils {
    public static void updateCache(String tags) {
        try {
            ProtocolHub.messageSource().addMessage("water.cache.update", tags);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
