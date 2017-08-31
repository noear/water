package watersev.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by yuety on 2017/7/18.
 */
public final class EncryptUtil {
    public static String md5(String str)
    {
        String s = null;
        try {
            byte[] data = str.getBytes("UTF-8");

            s = DigestUtils.md5Hex(data);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return s;
    }
}
