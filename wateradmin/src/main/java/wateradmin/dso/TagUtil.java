package wateradmin.dso;

import org.noear.solon.core.handle.Context;
import org.noear.water.utils.TextUtils;
import wateradmin.models.TagCountsModel;

import java.util.List;

public class TagUtil {
    public static String build(String tag_name, List<TagCountsModel> tags) {
        if (TextUtils.isEmpty(tag_name)) {
            tag_name = cookieGet();
        }

        if (TextUtils.isNotEmpty(tag_name)) {
            String tag  = tag_name;
            if (tags.stream().anyMatch(m -> tag.equals(m.tag)) == false) {
                tag_name = "";
            }
        }

        if (TextUtils.isEmpty(tag_name) && tags.isEmpty() == false) {
            tag_name = tags.get(0).tag;
        }

        if (tag_name == null) {
            return "";
        } else {
            return tag_name;
        }
    }

    public static String cookieGet(){
        return Context.current().cookie("wateradmin_log__tag");
    }

    public static void cookieSet(String tag){
        if(TextUtils.isEmpty(tag)){
            return;
        }

        Context.current().cookieSet("wateradmin_log__tag", tag);
    }
}
