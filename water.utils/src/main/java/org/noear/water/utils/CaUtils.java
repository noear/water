package org.noear.water.utils;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

/**
 * @author noear 2022/7/25 created
 */
public class CaUtils {
    public static Date getCaEndTime(String url) throws IOException {
        X509Certificate certificate = getCa(url);
        return certificate.getNotAfter();
    }

    public static X509Certificate getCa(String url) throws IOException {
        URL caUrl = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) caUrl.openConnection();

        connection.connect();
        Certificate certificate = null;
        if (connection.getServerCertificates().length > 0) {
            certificate = connection.getServerCertificates()[0];
        }
        connection.disconnect();

        return (X509Certificate) certificate;

//        for (Certificate certificate : connection.getServerCertificates()) {
//            //第一个就是服务器本身证书，后续的是证书链上的其他证书
//            X509Certificate x509Certificate = (X509Certificate) certificate;
//            System.out.println(x509Certificate.getSubjectDN().getName());
//            System.out.println(new Datetime(x509Certificate.getNotBefore()).toString("yyyy-MM-dd HH:mm:ss"));//有效期开始时间
//            System.out.println(new Datetime(x509Certificate.getNotAfter()).toString("yyyy-MM-dd HH:mm:ss"));//有效期结束时间
//            break;
//        }
    }
}
