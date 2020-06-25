package waterraas.utils;


import org.noear.snack.ONode;

/**
 * Created by noear on 2017/7/31.
 */
public class ArgsUtil {
    public static ONode getONode(String[] args) {
        ONode d = new ONode();

        int len = args.length;

        for (int i = 0; i < len; i ++) {
            String arg = args[i].replaceAll("-*","");

            if(arg.indexOf("=")>0){
                String[] ss = arg.split("=");
                d.set(ss[0], ss[1]);
            }else {
                if (i + 1 < len) {
                    d.set(arg, args[i + 1]);
                }
                i++;
            }
        }

        return d;
    }
}
