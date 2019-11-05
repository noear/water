package webapp.dso;


import webapp.dso.db.DbMsgApi;

public class MsgUtil {
    public static void updateCache(String tags) {
        try {
            DbMsgApi.addMessage(IDUtil.buildGuid(), "water.cache.update", tags, null);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
