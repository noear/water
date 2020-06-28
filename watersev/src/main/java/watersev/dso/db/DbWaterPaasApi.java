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
    //计划任务
    //
    public static List<PaasFileModel> getPlanList() throws SQLException {
        return db().table("paas_file")
                        .whereEq("file_type", 1).and("is_disabled=0")
                        .select("*")
                        .getList(new PaasFileModel());
    }

    public static void setPlanState(PaasFileModel plan, int state, String note) throws SQLException {

        DbTableQuery qr = db().table("paas_file");

        if (state == 9) {
            if (plan.plan_last_time != null) {
                plan.plan_last_timespan = System.currentTimeMillis() - plan.plan_last_time.getTime();

                qr.set("plan_last_timespan", plan.plan_last_timespan);
            }

            //对以天为进阶的任务，做同时间处理 //已在前面进行处理,不再需要后续处理
//            if (plan._is_day_task) {
//                String s_d = new Datetime(plan.plan_last_time).toString("yyyy-MM-dd");
//                String s_t = new Datetime(plan.plan_begin_time).toString("HH:mm:ss");
//
//                try {
//                    Datetime temp = Datetime.parse(s_d + " " + s_t, "yyyy-MM-dd HH:mm:ss");
//                    plan.plan_last_time = temp.getFulltime();
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
        }


        qr.set("plan_state", state)
                .set("plan_count", plan.plan_count)
                .set("plan_last_time", plan.plan_last_time)
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
