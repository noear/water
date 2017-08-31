package watersev.controller;

import noear.snacks.ONode;
import org.apache.http.util.TextUtils;
import watersev.dao.AlarmUtil;
import watersev.dao.LogUtil;
import watersev.dao.db.DbSevApi;
import watersev.models.water.ServiceModel;
import watersev.utils.HttpUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by yuety on 2017/7/28.
 */
public class SevController {
    public static void main() throws SQLException {
        //取出待处理的服务
        List<ServiceModel> list = DbSevApi.getServiceList();

        for (ServiceModel sev : list) {
            check(sev);
        }
    }

    private static void check(ServiceModel sev) {
        if (sev.check_type > 0) {
            return;
        }

        if (TextUtils.isEmpty(sev.check_url)) {
            return;
        }

        String url = sev.check_url;
        if (url.startsWith("http://") == false) {
            if (sev.address.indexOf("://") > 0) {
                url = sev.address + sev.check_url;
            } else {
                url = "http://" + sev.address + sev.check_url;
            }
        }

        try {
            DbSevApi.setServiceState(sev.service_id, 1);//设为;正在处理中

            HttpUtil.getStatusByAsync(url, 0, (isOk, code) -> {
                if (code >= 200 && code < 400) {
                    DbSevApi.udpService(sev.service_id, 0, code + "");

                    if (sev.check_last_state != 0) { //之前坏的，现在好了提示一下
                        AlarmUtil.tryAlarm(sev, true, code);
                    }
                } else {
                    DbSevApi.udpService(sev.service_id, 1, code + "");

                    if (sev.check_last_state == 0) {//之前好的，现在坏了提示一下
                        AlarmUtil.tryAlarm(sev, false, code);
                    }
                }
            });
        } catch (Exception ex) {
            DbSevApi.udpService(sev.service_id, 1, ex.getMessage());
            LogUtil.error("SevController", ex);
        }
    }
}
