package watersev.dso.db;

import lombok.extern.slf4j.Slf4j;
import org.noear.weed.DbContext;
import watersev.Config;
import watersev.models.water_reg.ServiceModel;
import watersev.models.water_reg.ServiceSmpModel;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by noear on 2017/7/18.
 */
@Slf4j
public final class DbWaterRegApi {
    public static DbContext db() {
        return Config.water;
    }

    public static List<ServiceModel> getServiceList() throws SQLException {
        //不能缓存（以便随时获取状态）
        return db().table("water_reg_service")
                .where("is_enabled=1")
                .selectList("*", ServiceModel.class);
    }

    public static List<ServiceSmpModel> getWaterServiceList(String subService) throws SQLException {
        //不能缓存（以便随时获取状态）
        return db().table("water_reg_service")
                .whereEq("name", subService)
                .andEq("is_enabled", 1)
                .andEq("check_last_state", 0)
                .caching(Config.cache_data)
                .usingCache(1)
                .selectList("name,address,meta,check_last_state", ServiceSmpModel.class);
    }

    public static void setServiceState(long service_id, int state) {
        try {
            db().table("water_reg_service")
                    .set("state", state)
                    .where("service_id=?", service_id)
                    .update();
        } catch (Exception ex) {
            log.error("{}", ex);
        }
    }

    /**
     * 更新服务，同时修改检测时间和备注（服务是被动检测的）
     * */
    public static void udpService0(long service_id, int check_state, String check_note) {
        try {
            db().table("water_reg_service").usingExpr(true)
                    .set("state", 0)
                    .set("check_last_state", check_state)
                    .set("check_last_time", System.currentTimeMillis())
                    .set("check_last_note", check_note)
                    .build((tb) -> {
                        if (check_state == 0)
                            tb.set("check_error_num", 0);
                        else
                            tb.set("check_error_num", "$check_error_num+1");
                    })
                    .where("service_id=?", service_id)
                    .update();
        } catch (Exception ex) {
            log.error("{}", ex);
        }
    }


    /**
     * 更新服务，但不改检测时间和备注（服务是主要签到的）
     * */
    public static void udpService1(long service_id, int check_state) {
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
            log.error("{}", ex);
        }
    }

    public static void delService(long service_id) {
        try {
            db().table("water_reg_service")
                    .where("service_id=? AND is_unstable=1", service_id)
                    .delete();
        } catch (Exception ex) {
            log.error("{}", ex);
        }
    }

    public static void delConsumer(String consumer_address) {
        try {
            db().table("water_reg_consumer")
                    .whereEq("consumer_address", consumer_address)
                    .delete();
        } catch (Exception ex) {
            log.error("{}", ex);
        }
    }

}
