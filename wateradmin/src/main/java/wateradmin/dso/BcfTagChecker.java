package wateradmin.dso;

import org.noear.bcf.BcfClient;
import org.noear.bcf.models.BcfResourceModel;
import org.noear.weed.ext.Fun1;
import org.noear.water.utils.TextUtils;
import wateradmin.Config;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//不能用静态函数
public class BcfTagChecker {
    private Map<String,String> tmpCache = null;

    private void tryLoadTagByUser() throws SQLException {
        if(tmpCache == null){
            tmpCache = new HashMap<>();

            List<BcfResourceModel> list = BcfClient.getUserResourcesByPack(Session.current().getPUID(), "tag");

            list.forEach((r) -> {
                tmpCache.put(r.en_name, r.en_name);
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

        return tmpCache.containsKey(tag);
    }

    public static <T> void filter(List<T> list, Fun1<String,T> getter) throws SQLException{
        if(Session.current().getIsAdmin()==1){
            return;
        }

        if (Config.is_use_tag_checker() == false) {
            return;
        }

        BcfTagChecker checker = new BcfTagChecker();

        for(int i=0,len=list.size(); i<len; i++){
            String tag = getter.run(list.get(i));

            if(TextUtils.isEmpty(tag)){
                list.remove(i);
                i--;
                len--;
            }else {
                tag = tag.split("\\.|_")[0];

                if (checker.find(tag) == false) {
                    list.remove(i);
                    i--;
                    len--;
                }
            }
        }
    }

    public static <T> void filterWaterTag(List<T> list, Fun1<String,T> getter) throws SQLException{
        for(int i=0,len=list.size(); i<len; i++){
            String tag = getter.run(list.get(i));

            if(TextUtils.isEmpty(tag)){
                list.remove(i);
                i--;
                len--;
            }else {
                tag = tag.split("\\.|_")[0];

                if (tag.startsWith("water") == false) {
                    list.remove(i);
                    i--;
                    len--;
                }
            }
        }
    }

}
