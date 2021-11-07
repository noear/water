package waterapi.dso.db;

import org.noear.water.utils.EncryptUtils;
import org.noear.weed.DataItem;
import org.noear.weed.DbContext;
import org.noear.solon.core.handle.Context;
import waterapi.dso.IPUtils;
import waterapi.dso.MsgUtils;
import waterapi.Config;
import waterapi.dso.LogUtils;
import waterapi.models.ServiceModel;

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
    public static void addService(String tag, String service, String address, String check_url, int check_type, String code_location, boolean is_unstable) throws SQLException {
        addService(tag, service, address, "", "", check_url, check_type, code_location, is_unstable);
    }

    public static void addService(String tag, String service, String address, String meta, String alarm_mobile, String check_url, int check_type, String code_location,boolean is_unstable) throws SQLException {
        if (meta == null) {
            meta = "";
        }

        if (tag == null) {
            tag = "";
        }

        String key = serviceMd5(service, address, meta);

        DataItem dataItem = new DataItem();
        dataItem.set("note", meta)
                .set("meta", meta)
                .set("tag", tag)
                .set("alarm_mobile", alarm_mobile)
                .set("is_unstable", (is_unstable ? 1 : 0))
                .set("check_url", check_url)
                .set("check_type", check_type)
                .set("code_location", code_location)
                .set("check_last_state", 0) //最后检查状态（0：OK；1：error）
                .set("check_last_time", "$NOW()")
                .set("check_last_note", "");


        boolean isOk = db().table("water_reg_service").usingExpr(true)
                .whereEq("key", key)
                .update(dataItem) > 0;


        if (isOk == false) {
            dataItem.set("key", key)
                    .set("name", service)
                    .set("address", address);

            db().table("water_reg_service").usingExpr(true)
                    .insert(dataItem);

            //新增节点时，添加负载通知
            if (service.contains(":") == false && check_type == 0) {
                MsgUtils.updateCache("upstream:" + service);
            }
        }
    }

    public static boolean delService(String service, String address, String meta) throws SQLException {
        String key = serviceMd5(service, address, meta);

        boolean isOk = db().table("water_reg_service")
                .whereEq("key", key)
                .delete() > 0;

        //通知负载更新
        if (service.contains(":") == false) {
            MsgUtils.updateCache("upstream:" + service);
        }

        return isOk;
    }

    public static ServiceModel getService(String service, String address, String meta) throws SQLException {
        String key = serviceMd5(service, address, meta);

        return db().table("water_reg_service")
                .whereEq("key", key)
                .select("*")
                .getItem(new ServiceModel());
    }

    public static boolean disableService(String service, String address, String meta, boolean is_enabled) throws SQLException {
        String key = serviceMd5(service, address, meta);

        boolean isOk = db().table("water_reg_service")
                .where("`key` = ?", key)
                .set("is_enabled", (is_enabled ? 1 : 0))
                .update() > 0;

        //通知负载更新
        if (service.contains(":") == false) {
            MsgUtils.updateCache("upstream:" + service);
        }

        return isOk;
    }


    public static void logConsume(String service, String consumer, String consumer_address) {
        try {
            db().table("water_reg_consumer")
                    .usingExpr(true)
                    .set("service", service)
                    .set("consumer", consumer)
                    .set("consumer_address", consumer_address)
                    .set("consumer_ip", IPUtils.getIP(Context.current()))
                    .set("chk_fulltime", "$NOW()")
                    .upsertBy("service,consumer_address");
        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtils.error(Context.current(), ex);
        }
    }

    public static String serviceMd5(String service, String address, String meta) {
        if (address.length() < 100) {
            return EncryptUtils.md5(service + "#" + address + "#" + meta);
        } else {
            return EncryptUtils.md5(service + "#" + address.substring(0, 100) + "#" + meta);
        }
    }
}
