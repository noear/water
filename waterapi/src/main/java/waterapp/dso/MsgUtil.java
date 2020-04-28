package waterapp.dso;

import waterapp.dso.db.DbWaterMsgApi;

public class MsgUtil {
    public static void updateCache(String tags) {
        try {
            DbWaterMsgApi.addMessage(IDUtil.buildGuid(), "water.cache.update", tags, null);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
