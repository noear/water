package org.noear.water.dso;


/**
 * @author noear 2021/11/12 created
 */
public class GatewayUtils {
    public static void notice(String tag, String name) {
        if (name.contains(":")) {
            return;
        }

        NoticeUtils.updateCache("upstream:" + name);

//        if (TextUtils.isEmpty(tag)) {
//            return;
//        }

//        NoticeUtils.updateCache("upstream:" + tag + "/" + name);
    }
}
