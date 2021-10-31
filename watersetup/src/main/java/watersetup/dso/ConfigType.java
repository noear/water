package watersetup.dso;


import watersetup.models.water_cfg.EnumModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigType {
    public static final int water_gateway = 9;
    public static final int db = 10;
    public static final int redis = 11;
    public static final int mangodb = 12;
    public static final int elasticsearch = 13;
    public static final int hbase = 14;
    public static final int memcached = 20;

    public static final int iaas_ram = 1003;

    public static final int water_logger = 1101;
    public static final int water_queue = 1102;

    private static Map<Integer,String> _types = new HashMap<>();
    public static void loadTypes(boolean reset){
        synchronized (_types){
            if (_types.size() == 0 || reset) {
                try {
                    _types.clear();

                    List<EnumModel> enumList = EnumUtil.get("config_type");
                    enumList.forEach((di) -> {
                        _types.put(Integer.parseInt(di.value), di.title);
                    });

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    public static final String getTitle(Integer value) {
        try {
            loadTypes(false);

            String tmp = _types.get(value);


            return tmp == null ? "" : tmp;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
