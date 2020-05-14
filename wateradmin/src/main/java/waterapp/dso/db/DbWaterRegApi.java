package waterapp.dso.db;

import org.noear.water.WaterClient;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import waterapp.Config;
import waterapp.dso.IDUtil;
import waterapp.models.water_reg.ServiceConsumerModel;
import waterapp.models.water_reg.ServiceModel;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class DbWaterRegApi {
    private static DbContext db() {
        return Config.water;
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

        WaterClient.Notice.updateCache("upstream:"+sev);
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
                    if(is_web){
                        tb.and("name LIKE ?","web:%");
                    }else{
                        tb.and("name NOT LIKE ?","web:%");
                    }

                    if (TextUtils.isEmpty(name) == false) {
                        if(name.startsWith("ip:")){
                            tb.and("address LIKE ?",name.substring(3)+"%");
                        }else{
                            tb.and("name like ?", name + "%");
                        }
                    }
                })
                .orderBy("name asc")
                .select("*")
                .getList(new ServiceModel());
    }

    public static ServiceModel getServiceById(int service_id) throws SQLException{
        return db()
                .table("water_reg_service")
                .where("service_id = ?", service_id)
                .select("*")
                .getItem(new ServiceModel());
    }

    public static List<ServiceModel> getServicesByName(String name) throws SQLException {

        return db().table("water_reg_service")
                .where("name = ?", name)
                .select("*")
                .getList(new ServiceModel());

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
            String key = IDUtil.buildGuid();
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
                .select("*")
                .getList(new ServiceConsumerModel());

    }
}
