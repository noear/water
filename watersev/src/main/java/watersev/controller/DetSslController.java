package watersev.controller;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.WW;
import org.noear.water.utils.*;
import watersev.dso.LogUtil;
import watersev.dso.db.DbWaterToolApi;
import watersev.models.water_tool.CertificationModel;

import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.List;

/**
 * 应用监视（可集群，可多实例运行。同时间，只会有一个节点有效）
 *
 * @author noear
 * */
@Component
public final class DetSslController implements IJob {
    @Override
    public String getName() {
        return "detssl";
    }

    @Override
    public int getInterval() {
        return 1000 * 60 * 5; //实际是：5m 跑一次
    }


    @Override
    public void exec() throws Throwable {
        RegController.addService("watersev-" + getName());

        if (LockUtils.tryLock(WW.watersev_detssl, WW.watersev_detssl, 10)) {
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
            X509Certificate certificate = CaUtils.getCa(url);
            DbWaterToolApi.certificationSetState(sev.certification_id, 0, "", certificate.getNotAfter());

        } catch (Throwable ex) {
            DbWaterToolApi.certificationSetState(sev.certification_id, 1, "0", null);
            LogUtil.sevWarn(getName(), sev.certification_id + "", url + "::\n" + Utils.throwableToString(ex));
        }
    }
}
