package webapp.dso;


import webapp.dso.db.DbMessageApi;

public class MessageUtil {
    public static void updateCache(String tags) {
        try {
            DbMessageApi.addMessage(IDUtil.buildGuid(), "water.cache.update", tags, null);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
