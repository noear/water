package watersev.dso.db;

import lombok.extern.slf4j.Slf4j;
import org.noear.weed.DbContext;
import watersev.Config;
import watersev.models.water.DetectionModel;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by noear on 2017/7/18.
 */
@Slf4j
public final class DbWaterDetApi {
    public static DbContext db() {
        return Config.water;
    }

    public static List<DetectionModel> getServiceList() throws SQLException {
        //不能缓存（以便随时获取状态）
        return db().table("water_tool_detection")
                .where("is_enabled=1")
                .selectList("*", DetectionModel.class);
    }

    /**
     * 更新服务，同时修改检测时间和备注（服务是被动检测的）
     */
    public static void udpService0(long detection_id, int check_state, String check_note) {
        try {
            db().table("water_tool_detection").usingExpr(true)
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
                    .whereEq("detection_id", detection_id)
                    .usingExpr(true)
                    .update();
        } catch (Exception ex) {
            log.error("{}", ex);
        }
    }
}
