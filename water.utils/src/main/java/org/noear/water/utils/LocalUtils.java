package org.noear.water.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class LocalUtils {
    public static String getLocalAddr(int port){
        return getLocalIp() + ":" + port;
    }

    public static String getLocalIp(){

        NetworkInterface neti = null;
        String host = null;


        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            Enumeration<InetAddress>      ee = null;

            while (en.hasMoreElements()) {
                neti = en.nextElement();//网卡

                if(neti.getName() == null){
                    continue;
                }

                //检查网卡名
                if(neti.getName().startsWith("en") ||  neti.getName().startsWith("eth")) {
                    //网卡绑定的地址
                    ee = neti.getInetAddresses();

                    while (ee.hasMoreElements()) {
                        //地址
                        host = ee.nextElement().getHostAddress();

                        if (TextUtils.isEmpty(host) == false) {
                            if (host.startsWith("192.") || host.startsWith("172.") || host.startsWith("10.")) {
                                return host;
                            }
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
