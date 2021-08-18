package wateradmin.dso.db;

import org.noear.water.utils.Datetime;
import org.noear.water.utils.IDUtils;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import wateradmin.dso.NoticeUtils;
import wateradmin.models.water.ServiceRuntimeModel;
import wateradmin.models.water_reg.ServiceConsumerModel;
import wateradmin.models.water_reg.ServiceModel;
import wateradmin.models.water_reg.ServiceSpeedDateModel;
import wateradmin.models.water_reg.ServiceSpeedHourModel;
import wateradmin.setup.Setup;

import java.sql.SQLException;
import java.util.*;

public class DbWaterRegApi {
    private static DbContext db() {
        return Setup.water;
    }

    //删除服务。
    public static boolean deleteServiceById(int service_id) throws SQLException {

        ServiceModel m = getServiceById(service_id);

        boolean isOk = db().table("water_reg_service")
                .where("service_id=?", service_id)
                .delete() > 0;

        //通知负载更新
        upstreamNotice(m.name);

        return isOk;
    }

    //修改服务启用禁用状态
    public static boolean disableService(Integer service_id, Integer is_enabled) throws SQLException {
        ServiceModel m = getServiceById(service_id);

        boolean isOk = db().table("water_reg_service")
                .where("service_id = ?", service_id)
                .set("is_enabled", is_enabled)
                .update() > 0;

        //通知负载更新
        upstreamNotice(m.name);

        return isOk;
    }

    //通知负载更新
    private static void upstreamNotice(String sev){
        if(sev.contains(":")){
            return;
        }

        if (DbWaterCfgApi.hasGateway(sev) == false) {
            return;
        }

        NoticeUtils.updateCache("upstream:"+sev);
    }

    //重置服务
    public static int resetSev() throws SQLException {
        return db().table("water_reg_service")
                .where("state = ?", 1)
                .set("state", 0)
                .update();
    }

    //获取service表中的数据。
    public static List<ServiceModel> getServices(String name, boolean is_web, int is_enabled) throws SQLException {
        return db()
                .table("water_reg_service")
                .where("is_enabled = ?", is_enabled)
                .build(tb -> {
                    if (is_web) {
                        tb.and("name LIKE ?", "web:%");
                    } else {
                        tb.and("name NOT LIKE ?", "web:%");
                    }

                    if (TextUtils.isEmpty(name) == false) {
                        if (name.startsWith("ip:")) {
                            tb.and("address LIKE ?", name.substring(3) + "%");
                        } else {
                            tb.and("name like ?", name + "%");
                        }
                    }
                })
                .orderBy("name asc")
                .selectList("*", ServiceModel.class);
    }

    public static ServiceModel getServiceById(int service_id) throws SQLException {
        return db()
                .table("water_reg_service")
                .where("service_id = ?", service_id)
                .selectItem("*", ServiceModel.class);
    }

    public static ServiceModel getServiceByKey(String key) throws SQLException {
        return db()
                .table("water_reg_service")
                .whereEq("key", key)
                .selectItem("*", ServiceModel.class);
    }

    public static List<ServiceModel> getServicesByName(String name) throws SQLException {

        return db().table("water_reg_service")
                .where("name = ?", name)
                .selectList("*", ServiceModel.class);
    }

    public static boolean udpService(Integer service_id,String name,String address,String note,Integer check_type,String check_url) throws SQLException {
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(address)){
            return false;
        }

        if(service_id == null){
            service_id = 0;
        }

        DbTableQuery query = db()
                .table("water_reg_service")
                .set("name", name)
                .set("address", address)
                .set("note",note)
                .set("check_type", check_type)
                .set("check_url", check_url);


        if (service_id == 0) {
            String key = IDUtils.guid();
            query.set("check_last_time",new Date())
                    .set("key", key).insert();
        } else {
            query.where("service_id = ?", service_id).update();
        }

        return true;
    }


    public static List<ServiceConsumerModel> getServiceConsumers(String service) throws SQLException {

        return db().table("water_reg_consumer")
                .where("service = ?", service)
                .orderBy("consumer asc")
                .selectList("*", ServiceConsumerModel.class);

    }


    //接口的三天的请求频率
    public static Map<String,List> getChartsForDate(String key, String field) throws SQLException {
        Datetime now = Datetime.Now();
        int date0 = now.getDate();
        int date1 = now.addDay(-1).getDate();
        int date2 = now.addDay(-1).getDate();


        Map<String, List> resp = new LinkedHashMap<>();
        List<ServiceRuntimeModel> threeDays = db().table("water_reg_service_runtime")
                .whereEq("key",key)
                .and("log_date>=?", date2)
                .orderBy("log_date DESC")
                .selectList(field + " val,log_date,log_hour", ServiceRuntimeModel.class); //把字段as为val

        Map<Integer, ServiceRuntimeModel> list0 = new HashMap<>();
        Map<Integer, ServiceRuntimeModel> list1 = new HashMap<>();
        Map<Integer, ServiceRuntimeModel> list2 = new HashMap<>();
        for (ServiceRuntimeModel m : threeDays) {
            if (m.log_date == date0) {
                list0.put(m.log_hour, m);
            }
            if (m.log_date == date1) {
                list1.put(m.log_hour, m);
            }
            if (m.log_date == date2) {
                list2.put(m.log_hour, m);
            }
        }

        Map<String, Map<Integer, ServiceRuntimeModel>> data = new LinkedHashMap<>();
        data.put("today", list0);
        data.put("yesterday", list1);
        data.put("beforeday", list2);

        data.forEach((k, list) -> {
            List<Object> array = new ArrayList<>();
            for (int j = 0; j < 24; j++) {
                if (list.containsKey(j)) {
                    array.add(list.get(j).val);
                } else {
                    array.add(0);
                }
            }
            resp.put(k, array);
        });

        return resp;
    }


    //获取接口三十天响应速度情况
    public static Map<String,List> getChartsForMonth(String key) throws SQLException {
        Map<String,List> resp = new LinkedHashMap<>();

        List<ServiceRuntimeModel> list = db().table("water_reg_service_runtime")
                .whereEq("key",key)
                .orderBy("log_date DESC")
                .limit(30)
                .selectList("*", ServiceRuntimeModel.class);

        Collections.sort(list, (o1, o2) -> (o1.log_date - o2.log_date));

        List<Object> memory_max = new ArrayList<>();
        List<Object> memory_total = new ArrayList<>();
        List<Object> memory_used = new ArrayList<>();
        List<Object> thread_peak_count = new ArrayList<>();
        List<Object> thread_count = new ArrayList<>();
        List<Object> thread_daemon_count = new ArrayList<>();
        List<Object> dates = new ArrayList<>();

        for (ServiceRuntimeModel m : list) {
            memory_max.add(m.memory_max);
            memory_total.add(m.memory_total);
            memory_used.add(m.memory_used);
            thread_peak_count.add(m.thread_peak_count);
            thread_count.add(m.thread_count);
            thread_daemon_count.add(m.thread_daemon_count);
            dates.add(m.log_date);
        }
        resp.put("memory_max", memory_max);
        resp.put("memory_total", memory_total);
        resp.put("memory_used", memory_used);
        resp.put("thread_peak_count", thread_peak_count);
        resp.put("thread_count", thread_count);
        resp.put("thread_daemon_count", thread_daemon_count);
        resp.put("dates", dates);
        return resp;
    }
}
