package webapp.dao;

import org.noear.bcf.BcfClient;
import org.noear.bcf.models.BcfResourceModel;
import org.noear.weed.ext.Fun1;
import org.apache.http.util.TextUtils;
import webapp.Config;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//不能用静态函数
public class BcfTagChecker {
    private Map<String,String> tmpCache = null;

    private void tryLoadAgroupByUser() throws SQLException {
        if(tmpCache == null){
            tmpCache = new HashMap<>();

            List<BcfResourceModel> list = BcfClient.getUserResourcesByPack(Session.current().getPUID(), "tag");

            list.forEach((r) -> {
                tmpCache.put(r.cn_name, r.cn_name);
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

        tryLoadAgroupByUser();

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

}
