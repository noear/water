package waterapi.dso;

/**
 * @author noear 2021/11/12 created
 */
public class GatewayUtils {
    public static void notice(String tag, String name) {
        if (name.contains(":")) {
            return;
        }

        MsgUtils.updateCache("upstream:" + name);

//        if (TextUtils.isEmpty(tag)) {
//            return;
//        }

//        MsgUtils.updateCache("upstream:" + tag + "/" + name);
    }
}
