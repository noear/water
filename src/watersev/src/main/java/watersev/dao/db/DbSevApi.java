package watersev.dao.db;

import noear.weed.DbContext;
import watersev.Config;
import watersev.models.water.ServiceModel;
import watersev.utils.EncryptUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by yuety on 2017/7/18.
 */
public final class DbSevApi {
    public static DbContext db() {
        return Config.water;
    }

    public static DbContext dbr() {
        return Config.water_r;
    }

    public static void initServiceState(){
        try {
            db().table("service").set("state", 0).update();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static List<ServiceModel> getServiceList() throws SQLException {
        //不能缓存（以便随时获取状态）
        return dbr().table("service").where("state=0").select("*").getList(new ServiceModel());
    }

    public static void setServiceState(int service_id, int state){
        try {
            db().table("service")
                    .set("state", state)
                    .where("service_id=?", service_id)
                    .update();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void udpService(int service_id, int check_state, String note)  {
        try {
            db().table("service")
                    .set("state", 0)
                    .set("check_last_state", check_state)
                    .set("check_last_time", "$NOW()")
                    .set("check_last_note", note)
                    .where("service_id=?", service_id)
                    .update();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void addService(String service, String address, String check_url, int check_type){
        try{
            doAddService(service,address,check_url,check_type);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private static void doAddService(String service, String address, String check_url, int check_type) throws SQLException {


        String key = EncryptUtil.md5(service + "#" + address);

        boolean isOk = db().table("service")
                .set("check_url", check_url)
                .set("check_type", check_type)
                .set("check_last_state", 0)
                .set("check_last_time", "$NOW()")
                .set("check_last_note", "")
                .where("`key`=?", key)
                .update() > 0;


        if (isOk == false) {
            db().table("service")
                    .set("`key`", key)
                    .set("name", service)
                    .set("address", address)
                    .set("check_url", check_url)
                    .set("check_type", check_type)
                    .set("check_last_state", 0)
                    .set("check_last_time", "$NOW()")
                    .set("check_last_note", "")
                    .insert();
        }
    }
}
