package org.noear.water.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class LocalUtils {
    public static String getLocalIp(){

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
}
