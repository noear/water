package waterapp.dso;

public class IaasTypeUtils {
    public static String iaas_type_str(int iaas_type){
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
