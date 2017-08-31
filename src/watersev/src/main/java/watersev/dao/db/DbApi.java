package watersev.dao.db;

import watersev.Config;
import watersev.dao.CacheUtil;
import watersev.models.water.MonitorModel;
import watersev.models.water.ConfigModel;
import watersev.models.water.SynchronousModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuety on 2017/7/27.
 */
public class DbApi {
    public static List<String> getAlarmMobileList() throws SQLException{
        return
        Config.water.table("alarm")
                    .select("mobile")
                    .caching(CacheUtil.data)
                    .getArray("mobile");
    }


    public static List<SynchronousModel> getSyncList(boolean isFast) {
        try {
            return Config.water_r.table("synchronous").where("is_enabled = 1").expre((tb)->{
                if(isFast){
                    tb.and("`interval` <= 10");
                }else{
                    tb.and("`interval` > 10");
                }
            }).select("*").getList(new SynchronousModel());
        }catch (Exception ex){
            ex.printStackTrace();

            return new ArrayList<>();
        }
    }

    public static void setSyncTaskTag(int sync_id, long task_tag) throws SQLException{
        Config.water.table("synchronous")
                .set("task_tag",task_tag)
                .where("sync_id=?",sync_id).update();
    }

    public static ConfigModel getConfig(String tag, String key) throws SQLException{

        return Config.water_r.table("config")
                .where("tag=? AND `key`=?", tag, key)
                .select("*")
                .getItem(new ConfigModel());
    }


    public static List<MonitorModel> getMonitorList(){
        try {
            return Config.water_r.table("monitor").where("is_enabled=1").select("*").getList(new MonitorModel());
        }catch (Exception ex){
            ex.printStackTrace();

            return new ArrayList<>();
        }
    }

    public static void setMonitorState(int monitor_id, int alarm_count,String task_tag) throws SQLException{
        Config.water.table("monitor")
                .set("alarm_count",alarm_count)
                .set("task_tag",task_tag)
                .where("monitor_id=?",monitor_id).update();
    }
}
