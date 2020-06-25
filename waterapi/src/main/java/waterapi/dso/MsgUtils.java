package waterapi.dso;

import waterapi.dso.db.DbWaterMsgApi;

public class MsgUtils {
    public static void updateCache(String tags) {
        try {
            DbWaterMsgApi.addMessage("water.cache.update", tags);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
