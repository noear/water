package watersev.dso.service;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.water.WW;
import org.noear.water.utils.CaUtils;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.LockUtils;
import org.noear.water.utils.Timespan;
import watersev.dso.AlarmUtil;
import watersev.dso.LogUtil;
import watersev.dso.db.DbWaterToolApi;
import watersev.models.water_tool.CertificationModel;

import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @author noear 2022/7/25 created
 */
@Component
public class DetCaService {
    public String getName() {
        return "detca";
    }

    public void execDo() throws SQLException {
        //5分钟一次
        if (LockUtils.tryLock(WW.watersev_det, getName(), 60 * 5) == false) {
            return;
        }

        //取出待处理的服务（已启用的服务）
        List<CertificationModel> list = DbWaterToolApi.certificationGetList();

        for (CertificationModel task : list) {
            try {
                check(task);
            } catch (Throwable e) {
                LogUtil.error(getName(), task.certification_id + "", task.url + "::\n" + Utils.throwableToString(e));
            }
        }
    }

    //检测服务，并尝试报警
    private void check(CertificationModel task) {
        String threadName = getName() + "-" + task.certification_id;
        Thread.currentThread().setName(threadName);

        //检测开始
        String url = task.url;

        if (url.startsWith("https://")) {
            Date time_of_end = check_type(task, url);

            if (time_of_end != null) {
                long days = new Timespan(time_of_end, new Date()).days();
                if (days <= 100) {
                    AlarmUtil.tryAlarm(task, time_of_end, days);
                    LogUtil.warn(getName(), task.certification_id + "", url + "::" + days + "d::" + new Datetime(time_of_end).toString("yyyy-MM-dd"));
                }else{
                    LogUtil.info(getName(), task.certification_id + "", url + "::" + days + "d::" + new Datetime(time_of_end).toString("yyyy-MM-dd"));
                }
            }
            return;
        }
    }

    private Date check_type(CertificationModel task, String url) {
        try {
            X509Certificate certificate = CaUtils.getCa(url);
            Date time_of_end = certificate.getNotAfter();
            DbWaterToolApi.certificationSetState(task.certification_id, 0, "", time_of_end);

            return time_of_end;
        } catch (Throwable ex) {
            DbWaterToolApi.certificationSetState(task.certification_id, 1, "0", null);
            LogUtil.warn(getName(), task.certification_id + "", url + "::\n" + Utils.throwableToString(ex));

            return null;
        }
    }
}