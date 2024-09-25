package wateradmin.dso;

import org.noear.water.utils.TextUtils;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.models.water_cfg.EnumModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class EnumUtil {
    public static List<EnumModel> get(String group) throws SQLException {
        List<EnumModel> enumList = new ArrayList<>();
        AtomicBoolean isNumeric = new AtomicBoolean(true);

        Properties prop = DbWaterCfgApi.getConfigByTagName("_system", group).getProp();
        prop.forEach((k, v) -> {
            EnumModel m = new EnumModel();
            m.value = k.toString();
            m.title = v.toString();
            if (TextUtils.isNumeric(m.value)) {
                m.enum_id = Integer.parseInt(m.value);
            } else {
                isNumeric.set(false);
                m.enum_id = 0;
            }
            enumList.add(m);
        });

        if (isNumeric.get()) {
            enumList.sort(Comparator.comparing(m -> m.enum_id));
        } else {
            enumList.sort(Comparator.comparing(m -> m.value));
        }

        return enumList;
    }
}
