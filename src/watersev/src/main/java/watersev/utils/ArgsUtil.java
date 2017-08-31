package watersev.utils;

import noear.snacks.ONode;

/**
 * Created by yuety on 2017/7/31.
 */
public class ArgsUtil {
    public static ONode getONode(String[] args) {
        ONode d = new ONode();

        int len = args.length;

        for (int i = 0; i < len; i += 2) {
            if (i + 1 < len) {
                d.set(args[i], args[i + 1]);
            }
        }

        return d;
    }
}
