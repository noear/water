package waterapi.dso;

import org.noear.solon.core.XContext;
import org.noear.water.utils.TextUtils;

public class IPUtils {
    public static String getIP(XContext request){
        String ip =  request.header("RemoteIp");

        if (TextUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.header("X-Forwarded-For");
        }

        if (TextUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.header("Proxy-Client-IP");
        }

        if (TextUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.header("WL-Proxy-Client-IP");
        }

        if (TextUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.ip();
        }

        return ip;
    }
}
