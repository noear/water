package webapp.dso.db;

import org.noear.solon.core.XContext;
import org.noear.weed.DbContext;
import waterapi.Config;
import waterapi.dao.LogUtil;
import waterapi.dao.MsgUtil;
import waterapi.models.ServiceModel;
import waterapi.utils.EncryptUtil;
import waterapi.utils.IPUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by yuety on 2017/7/18.
 */
public final class DbSevApi {
    public static DbContext db() {
        return Config.water;
    }

    public static List<ServiceModel> getServiceList(String name) throws SQLException {
        return db().table("$.service")
                .where("`name`=? AND is_enabled=1 AND check_last_state = 0", name)
                .select("*")
                .getList(new ServiceModel());
    }

    //添加服务（key）
    public static void addService(String service, String address, String note, String alarm_mobile ,String check_url, int check_type) throws SQLException {
        if(note == null) {
            note = "";
        }

        String key = EncryptUtil.md5(service + "#" + address + "#" + note);

        boolean isOk = db().table("$.service").usingExpr(true)
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
            db().table("$.service").usingExpr(true)
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
                MsgUtil.updateCache("upstream:"+service);
            }
        }
    }

    public static boolean disableService(String service, String address,String note, boolean is_enabled) throws SQLException {
        String key = EncryptUtil.md5(service + "#" + address + "#" + note);

        boolean isOk = db().table("service")
                .where("`key` = ?", key)
                .set("is_enabled", (is_enabled?1:0))
                .update() > 0;

        //通知负载更新
        if(service.contains(":")==false){
            MsgUtil.updateCache("upstream:"+service);
        }

        return isOk;
    }


    public static void logConsume(String service,String consumer,String consumer_address) {
        try {
            db().table("service_consumer")
                    .usingExpr(true)
                    .set("service", service)
                    .set("consumer", consumer)
                    .set("consumer_address", consumer_address)
                    .set("consumer_ip", IPUtil.getIP(XContext.current()))
                    .set("chk_fulltime", "$NOW()")
                    .updateExt("service,consumer_address");
        }catch (Exception ex){
            ex.printStackTrace();
            LogUtil.error(XContext.current(),ex);
        }
    }
}
