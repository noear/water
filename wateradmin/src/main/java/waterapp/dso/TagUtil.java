package waterapp.dso;

import org.noear.solon.core.XContext;
import org.noear.water.utils.TextUtils;
import waterapp.models.TagCountsModel;

import java.util.List;

public class TagUtil {
    public static String build(String tag_name, List<TagCountsModel> tags){
        if(TextUtils.isEmpty(tag_name)){
            tag_name = cookieGet();
        }

        if(TextUtils.isEmpty(tag_name) && tags.isEmpty() == false){
            tag_name = tags.get(0).tag;
        }

        if(tag_name == null){
            return "";
        }else{
            return tag_name;
        }
    }

    public static String cookieGet(){
        return XContext.current().cookie("wateradmin_log__tag");
    }

    public static void cookieSet(String tag){
        if(TextUtils.isEmpty(tag)){
            return;
        }

        XContext.current().cookieSet("wateradmin_log__tag", tag);
    }
}
