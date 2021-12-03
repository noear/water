package xwater.dso;

import org.noear.water.utils.TextUtils;
import org.noear.weed.ext.Fun1;

import java.sql.SQLException;
import java.util.List;

//不能用静态函数
public class TagChecker {

    public static <T> void filterWaterTag(List<T> list, Fun1<String, T> getter) throws SQLException {
        for (int i = 0, len = list.size(); i < len; i++) {
            String tag = getter.run(list.get(i));

            if (TextUtils.isNotEmpty(tag)) {
                if (tag.startsWith("water") || tag.startsWith("_")) {
                    continue;
                }
            }

            list.remove(i);
            i--;
            len--;
        }
    }
}
