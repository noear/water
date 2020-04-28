package org.noear.water.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by noear on 2017/7/28.
 */

public final class ServerUtils {

    public static String getHost(){

        String host = null;

        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            Enumeration<InetAddress>      ee = null;

            while (en.hasMoreElements()) {
                ee = en.nextElement().getInetAddresses();
                while (ee.hasMoreElements()) {
                    host = ee.nextElement().getHostAddress();

                    if(TextUtils.isEmpty(host) == false) {
                        if (host.startsWith("192.") || host.startsWith("172.") || host.startsWith("10.")) {
                            return host;
                        }
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return host;
    }

    public static String getFullAddress(int port) {
        return getHost() + ":" + port;
    }

}
