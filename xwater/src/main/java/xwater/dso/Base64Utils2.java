package xwater.dso;

import org.noear.water.utils.Base64Utils;

/**
 * @author noear 2021/11/5 created
 */
public class Base64Utils2 {
    public static String decode(String code) {
        try {
            return Base64Utils.decode(code);
        } catch (Exception e) {
            return null;
        }
    }

    public static String encode(String code) {
        try {
            return Base64Utils.encode(code);
        } catch (Exception e) {
            return null;
        }
    }
}
