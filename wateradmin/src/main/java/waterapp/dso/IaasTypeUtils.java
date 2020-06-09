package waterapp.dso;

import org.noear.water.utils.TextUtils;
import waterapp.dso.db.DbWaterCfgApi;

import java.util.Properties;

public class IaasTypeUtils {
    public static String iaas_type_str(int iaas_type){
        try {
            //
            // 先从配置里获取
            //
            Properties prop = DbWaterCfgApi.getConfigByTagName("_system", "iaas_type", true).getProp();

            String val = prop.getProperty(iaas_type+"");

            if(TextUtils.isEmpty(val) == false){
                return val;
            }

        }catch (Exception ex){

        }

        //如果没有，用静态的配置
        return iaas_type_str0(iaas_type);
    }

    private static String iaas_type_str0(int iaas_type){
        switch (iaas_type){
            case 1:return "LBS";
            case 2:return "RDS";
            case 3:return "Redis";
            case 4:return "Memcached";
            case 5:return "DRDS";
            case 6:return "ECI";
            case 7:return "NAS";
            case 8:return "PolarDB";
            default:return "ECS";
        }
    }
}
