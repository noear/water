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
    }
}
