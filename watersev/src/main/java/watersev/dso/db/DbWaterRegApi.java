package watersev.dso.db;

import org.noear.water.utils.EncryptUtils;
import org.noear.weed.DbContext;
import watersev.Config;
import watersev.models.water_reg.ServiceModel;
import watersev.models.water_reg.ServiceSmpModel;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by noear on 2017/7/18.
 */
public final class DbWaterRegApi {
    public static DbContext db() {
        return Config.water;
    }

    public static void initServiceState(){
        try {
            db().table("water_reg_service").set("state", 0).where("1=1").update();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static int resetServiceState() throws SQLException{
        return db().table("water_reg_service")
                .where("state = ?", 1)
                .set("state", 0)
                .update();
    }

    public static List<ServiceModel> getServiceList() throws SQLException {
        //不能缓存（以便随时获取状态）
        return db().table("water_reg_service")
                .where("is_enabled=1")
                .selectList("*", ServiceModel.class);
    }

    public static List<ServiceSmpModel> getServiceListByName(String serviceName) throws SQLException {
        //不能缓存（以便随时获取状态）
        return db().table("water_reg_service")
                .where("is_enabled=1")
                .andEq("name", serviceName)
                .selectList("name,address,meta,check_last_state", ServiceSmpModel.class);
    }


    public static void setServiceState(long service_id, int state){
        try {
            db().table("water_reg_service")
                    .set("state", state)
                    .where("service_id=?", service_id)
                    .update();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void udpService0(long service_id, int check_state, String note) {
        try {
            db().table("water_reg_service").usingExpr(true)
                    .set("state", 0)
                    .set("check_last_state", check_state)
                    .set("check_last_time", "$NOW()")
                    .set("check_last_note", note)
                    .build((tb) -> {
                        if (check_state == 0)
                            tb.set("check_error_num", 0);
                        else
                            tb.set("check_error_num", "$check_error_num+1");
                    })
                    .where("service_id=?", service_id)
                    .update();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void delService(long service_id) {
        try {
            db().table("water_reg_service")
                    .where("service_id=? AND is_unstable=1", service_id)
                    .delete();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void udpService1(long service_id, ServiceModel sev, int check_state) {
        try {
            db().table("water_reg_service").usingExpr(true)
                    .set("state", 0)
                    .set("check_last_state", check_state)
                    .build((tb) -> {
                        if (check_state == 0)
                            tb.set("check_error_num", 0);
                        else
                            tb.set("check_error_num", "$check_error_num+1");
                    })
                    .where("service_id=?", service_id)
                    .update();
        } catch (Exception ex) {
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

        String key = EncryptUtils.md5(service + "#" + address);

        boolean isOk = db().table("water_reg_service").usingExpr(true)
                .set("check_url", check_url)
                .set("check_type", check_type)
                .set("check_last_state", 0)
                .set("check_last_time", "$NOW()")
                .set("check_last_note", "")
                .where("`key`=?", key)
                .update() > 0;


        if (isOk == false) {
            db().table("water_reg_service").usingExpr(true)
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
