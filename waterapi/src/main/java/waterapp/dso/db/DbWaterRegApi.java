package waterapp.dso.db;

import org.noear.water.utils.EncryptUtils;
import org.noear.weed.DbContext;
import org.noear.solon.core.XContext;
import waterapp.Config;
import waterapp.dso.LogUtils;
import waterapp.dso.MsgUtils;
import waterapp.models.ServiceModel;
import waterapp.dso.IPUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * 注册服务接口
 * */
public final class DbWaterRegApi {
    public static DbContext db() {
        return Config.water;
    }

    public static List<ServiceModel> getServiceList(String name) throws SQLException {
        return db().table("water_reg_service")
                .where("`name`=? AND is_enabled=1 AND check_last_state = 0", name)
                .select("*")
                .getList(new ServiceModel());
    }

    //添加服务（key）
    public static void addService(String service, String address, String check_url, int check_type) throws SQLException{
        addService(service,address,"","",check_url,check_type);
    }
    public static void addService(String service, String address, String note, String alarm_mobile ,String check_url, int check_type) throws SQLException {
        if(note == null) {
            note = "";
        }

        String key = EncryptUtils.md5(service + "#" + address + "#" + note);

        boolean isOk = db().table("water_reg_service").usingExpr(true)
                .set("note", note)
                .set("alarm_mobile", alarm_mobile)
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
                    .set("note", note)
                    .set("alarm_mobile", alarm_mobile)
                    .set("check_url", check_url)
                    .set("check_type", check_type)
                    .set("check_last_state", 0)
                    .set("check_last_time", "$NOW()")
                    .set("check_last_note", "")
                    .insert();

            //新增节点时，添加负载通知
            if(service.contains(":")==false && check_type==0){
                MsgUtils.updateCache("upstream:"+service);
            }
        }
    }

    public static boolean disableService(String service, String address,String note, boolean is_enabled) throws SQLException {
        String key = EncryptUtils.md5(service + "#" + address + "#" + note);

        boolean isOk = db().table("water_reg_service")
                .where("`key` = ?", key)
                .set("is_enabled", (is_enabled?1:0))
                .update() > 0;

        //通知负载更新
        if(service.contains(":")==false){
            MsgUtils.updateCache("upstream:"+service);
        }

        return isOk;
    }


    public static void logConsume(String service,String consumer,String consumer_address) {
        try {
            db().table("water_reg_consumer")
                    .usingExpr(true)
                    .set("service", service)
                    .set("consumer", consumer)
                    .set("consumer_address", consumer_address)
                    .set("consumer_ip", IPUtils.getIP(XContext.current()))
                    .set("chk_fulltime", "$NOW()")
                    .upsertBy("service,consumer_address");
        }catch (Exception ex){
            ex.printStackTrace();
            LogUtils.error(XContext.current(),ex);
        }
    }
}
