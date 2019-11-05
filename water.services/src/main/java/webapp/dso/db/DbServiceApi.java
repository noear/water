package webapp.dso.db;

import org.noear.solon.core.XContext;
import org.noear.water.tools.EncryptUtils;
import org.noear.weed.DbContext;
import webapp.Config;
import webapp.dso.LogUtil;
import webapp.dso.MessageUtil;
import webapp.model.ServiceModel;
import webapp.utils.IPUtil;

import java.sql.SQLException;
import java.util.List;

public final class DbServiceApi {
    public static DbContext db() {
        return Config.water;
    }

    /**
     * 获取服务列表
     * */
    public static List<ServiceModel> getServiceList(String name) throws SQLException {
        return db().table("water_base_service")
                .whereEq("name", name)
                .and("is_enabled=1 AND check_last_state = 0")
                .select("*")
                .getList(ServiceModel.class);
    }

    /**
     * 添加服务（key）
     * */
    public static void addService(String service, String address, String note, String alarm_mobile, String check_url, int check_type) throws SQLException {
        if (note == null) {
            note = "";
        }

        String key = EncryptUtils.md5(service + "#" + address + "#" + note);

        boolean isOk = db().table("water_base_service").usingExpr(true)
                .set("note", note)
                .set("alarm_mobile", alarm_mobile)
                .set("check_url", check_url)
                .set("check_type", check_type)
                .set("check_last_state", 0)
                .set("check_last_time", "$NOW()")
                .set("check_last_note", "")
                .whereEq("key", key)
                .update() > 0;


        if (isOk == false) {
            db().table("water_base_service").usingExpr(true)
                    .set("key", key)
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
            if (service.contains(":") == false && check_type == 0) {
                MessageUtil.updateCache("upstream:" + service);
            }
        }
    }

    /**
     * 禁用服务
     * */
    public static boolean disableService(String service, String address, String note, boolean is_enabled) throws SQLException {
        String key = EncryptUtils.md5(service + "#" + address + "#" + note);

        boolean isOk = db().table("water_base_service")
                .whereEq("key", key)
                .set("is_enabled", (is_enabled ? 1 : 0))
                .update() > 0;

        //通知负载更新
        if (service.contains(":") == false) {
            MessageUtil.updateCache("upstream:" + service);
        }

        return isOk;
    }


    /**
     * 记录消息者
     * */
    public static void logConsume(String service, String consumer, String consumer_address) {
        try {
            db().table("water_base_service_consumer")
                    .usingExpr(true)
                    .set("service",   service)
                    .set("consumer", consumer)
                    .set("consumer_address", consumer_address)
                    .set("consumer_ip", IPUtil.getIP(XContext.current()))
                    .set("chk_fulltime", "$NOW()")
                    .updateExt("service,consumer_address");
        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.error(XContext.current(), ex);
        }
    }
}
