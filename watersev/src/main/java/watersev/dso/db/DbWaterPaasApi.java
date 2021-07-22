package watersev.dso.db;

import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import watersev.Config;
import watersev.models.water_paas.PaasFileModel;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public final class DbWaterPaasApi {
    private static DbContext db() {
        return Config.water_paas; //此处要进行适配修改
    }

    //===============
    //定时任务
    //
    public static List<PaasFileModel> getPlanList() throws SQLException {
        return db().table("paas_file")
                .whereEq("file_type", 1).and("is_disabled=0")
                .and().begin("plan_last_timespan = 0").or("plan_last_timespan <= ", System.currentTimeMillis()).end()
                .selectList("*", PaasFileModel.class);
    }

    public static void setPlanState(PaasFileModel plan, int state, String note) throws SQLException {
        DbTableQuery qr = db().table("paas_file");

        qr.set("plan_state", state)
                .set("plan_count", plan.plan_count)
                .set("plan_last_time", plan.plan_last_time)
                .build((tb) -> {
                    if (plan.plan_last_timespan > 0) {
                        tb.set("plan_last_timespan", plan.plan_last_timespan);
                    }

                    if(plan.plan_next_timestamp > 0){
                        tb.set("plan_next_timestamp", plan.plan_next_timestamp);
                    }
                })
                .where("file_id=?", plan.file_id)
                .update();
    }

    public static void resetPlanState() throws SQLException {
        db().table("paas_file")
                .set("plan_state", 9)
                .where("plan_state=?", 2)
                .update();
    }
}
