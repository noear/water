package watersev.utils;

import java.net.InetAddress;

/**
 * Created by yuety on 2017/7/28.
 */
public class ServerUtil {
    public static String getHost(){
        String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return host;
    }

    public static String getFullAddress() {
        return getHost();
    }
}
