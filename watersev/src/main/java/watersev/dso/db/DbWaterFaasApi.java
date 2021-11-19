package watersev.dso.db;

import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import watersev.Config;
import watersev.models.water_paas.LuffyFileModel;

import java.sql.SQLException;
import java.util.List;

public final class DbWaterFaasApi {
    private static DbContext db() {
        return Config.water_paas; //此处要进行适配修改
    }

    //===============
    //定时任务
    //
    public static List<LuffyFileModel> getPlanList() throws SQLException {
        return db().table("luffy_file")
                .whereEq("file_type", 1)
                .andEq("is_disabled", 0)
                .andLte("plan_last_timespan", System.currentTimeMillis())
                .selectList("*", LuffyFileModel.class);
    }

    public static void setPlanState(LuffyFileModel plan, int state, String note) throws SQLException {
        DbTableQuery qr = db().table("luffy_file");

        qr.set("plan_state", state)
                .set("plan_count", plan.plan_count)
                .set("plan_last_time", plan.plan_last_time.getTime())
                .build((tb) -> {
                    if (plan.plan_last_timespan > 0) {
                        tb.set("plan_last_timespan", plan.plan_last_timespan);
                    }

                    if (plan.plan_next_time != null) {
                        tb.set("plan_next_time", plan.plan_next_time.getTime());
                    }
                })
                .where("file_id=?", plan.file_id)
                .update();
    }

    public static void resetPlanState() throws SQLException {
        db().table("luffy_file")
                .set("plan_state", 9)
                .where("plan_state=?", 2)
                .update();
    }
}
