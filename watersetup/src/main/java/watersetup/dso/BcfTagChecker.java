package watersetup.dso;

import org.noear.water.utils.TextUtils;
import org.noear.weed.ext.Fun1;

import java.sql.SQLException;
import java.util.List;

//不能用静态函数
public class BcfTagChecker {

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
