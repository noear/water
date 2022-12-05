package features;

import org.junit.Test;
import org.noear.water.utils.CaUtils;
import org.noear.water.utils.Datetime;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

/**
 * @author noear 2022/7/22 created
 */
public class CaTest {
    @Test
    public void test() throws Exception {
        URL url = new URL("https://www.oschina.net");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setUseCaches(false);
        connection.connect();
        for (Certificate certificate : connection.getServerCertificates()) {
            //第一个就是服务器本身证书，后续的是证书链上的其他证书
            X509Certificate x509Certificate = (X509Certificate) certificate;
            System.out.println(x509Certificate.getSubjectDN().getName());
            System.out.println(new Datetime(x509Certificate.getNotBefore()).toString("yyyy-MM-dd HH:mm:ss"));//有效期开始时间
            System.out.println(new Datetime(x509Certificate.getNotAfter()).toString("yyyy-MM-dd HH:mm:ss"));//有效期结束时间
            break;
        }
        connection.disconnect();
    }

    @Test
    public void test2() throws Exception {
        Date time_of_end = CaUtils.getCaEndTime("https://www.oschina.net");
        System.out.println(new Datetime(time_of_end).toString("yyyy-MM-dd HH:mm:ss"));//有效期结束时间
    }
}
