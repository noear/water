package watersetup.dso;


import watersetup.dso.db.DbWaterCfgApi;
import watersetup.models.water_cfg.EnumModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

public class EnumUtil {
    public static List<EnumModel> get(String group) throws SQLException {
        List<EnumModel> enumList = new ArrayList<>();
        Properties prop = DbWaterCfgApi.getConfigByTagName("_system", group).getProp();
        prop.forEach((k, v) -> {
            EnumModel m = new EnumModel();
            m.value = k.toString();
            m.title = v.toString();
            m.enum_id = Integer.parseInt(m.value);
            enumList.add(m);
        });
        enumList.sort(Comparator.comparing(m -> m.enum_id));

        return enumList;
    }
}
