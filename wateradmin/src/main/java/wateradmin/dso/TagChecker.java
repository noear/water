package wateradmin.dso;

import org.noear.grit.client.GritClient;
import org.noear.grit.model.domain.ResourceEntity;
import org.noear.weed.ext.Fun1;
import org.noear.water.utils.TextUtils;
import wateradmin.Config;

import java.sql.SQLException;
import java.util.*;

//不能用静态函数
public class TagChecker {
    private Set<String> tagCached = null;

    private void tryLoadTagByUser() throws SQLException {
        if (tagCached == null) {
            tagCached = new HashSet<>();

            List<ResourceEntity> list = GritClient.global().auth()
                    .getResListByGroup(Session.current().getSubjectId(), "tag");

            list.forEach((r) -> {
                tagCached.add(r.display_name);
            });
        }
    }

    public boolean find(String tag) throws SQLException {
        if (tag == null) {
            return false;
        }

        if (Config.is_use_tag_checker() == false) {
            return true;
        }

        tryLoadTagByUser();

        for (String tag0 : tagCached) {
            if (tag.startsWith(tag0)) {
                return true;
            }
        }

        return false;
    }

    public static <T> void filter(List<T> list, Fun1<String, T> getter) throws SQLException {
        if (Session.current().getIsAdmin() == 1) {
            return;
        }

        if (Config.is_use_tag_checker() == false) {
            return;
        }

        TagChecker checker = new TagChecker();

        for (int i = 0, len = list.size(); i < len; i++) {
            String tag = getter.run(list.get(i));

            if (TextUtils.isEmpty(tag)) {
                list.remove(i);
                i--;
                len--;
            } else {
                tag = tag.split("\\.|_")[0];

                if (checker.find(tag) == false) {
                    list.remove(i);
                    i--;
                    len--;
                }
            }
        }
    }
}
