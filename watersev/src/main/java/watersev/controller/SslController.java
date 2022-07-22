package watersev.controller;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.WW;
import org.noear.water.WaterClient;
import org.noear.water.utils.*;
import watersev.dso.AlarmUtil;
import watersev.dso.LogUtil;
import watersev.dso.db.DbWaterToolApi;
import watersev.models.water_tool.CertificationModel;
import watersev.utils.HttpUtilEx;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.List;

/**
 * 应用监视（可集群，可多实例运行。同时间，只会有一个节点有效）
 *
 * @author noear
 * */
@Component
public final class SslController implements IJob {
    @Override
    public String getName() {
        return "ssl";
    }

    @Override
    public int getInterval() {
        return 1000 * 60 * 30; //实际是：30m 跑一次
    }


    @Override
    public void exec() throws Throwable {
        RegController.addService("watersev-" + getName());

        if (LockUtils.tryLock(WW.watersev_det, WW.watersev_det, 10)) {
            exec0();
        }
    }

    private void exec0() throws SQLException {
        //取出待处理的服务（已启用的服务）
        List<CertificationModel> list = DbWaterToolApi.certificationGetList();

        for (CertificationModel task : list) {
            check(task);
        }
    }

    //检测服务，并尝试报警
    private void check(CertificationModel task) {
        String threadName = "ssl-" + task.certification_id;
        Thread.currentThread().setName(threadName);

        //检测开始
        String url = task.url;

        if (url.startsWith("https://")) {
            check_type(task, url);
            return;
        }
    }

    private void check_type(CertificationModel sev, String url) {
        //String trackName = sev.name + "@" + sev.protocol + "://" + sev.address;

        try {
            X509Certificate certificate = getCa(url);
            DbWaterToolApi.certificationSetState(sev.certification_id, 0, "", certificate.getNotAfter());

        } catch (Throwable ex) {
            DbWaterToolApi.certificationSetState(sev.certification_id, 1, "0", null);
            LogUtil.sevWarn(getName(), sev.certification_id + "", url + "::\n" + Utils.throwableToString(ex));
        }
    }


    private X509Certificate getCa(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        connection.connect();
        Certificate certificate = null;
        if(connection.getServerCertificates().length > 0) {
            certificate = connection.getServerCertificates()[0];
        }
        connection.disconnect();

        return (X509Certificate)certificate;

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
