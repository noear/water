package waterapi.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;

/**
 * Created by yuety on 2017/7/28.
 */

public final class ServerUtil {

    public static String getHost(){
        String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return host;
    }

    public static String getFullAddress(HttpServletRequest request) {
        return getHost() + ":" + request.getLocalPort();
    }

}
