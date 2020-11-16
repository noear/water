package watersev.controller;

import org.noear.solon.annotation.Component;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.WaterClient;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.LockUtils;
import org.noear.water.utils.TextUtils;
import org.noear.water.utils.Timespan;
import watersev.Config;
import watersev.dso.AlarmUtil;
import watersev.dso.LogUtil;
import watersev.dso.db.DbWaterCfgApi;
import watersev.dso.db.DbWaterRegApi;
import watersev.models.water_reg.ServiceModel;
import watersev.utils.HttpUtilEx;

import java.util.List;

/**
 * 服务检测（已支持 is_unstable）
 * */
@Component
public final class SevController implements IJob {

    public SevController() {
        DbWaterRegApi.initServiceState();
    }

    @Override
    public String getName() {
        return "sev";
    }

    @Override
    public int getInterval() {
        return 1000 * 4;
    }

    private boolean _init = false;

    @Override
    public void exec() throws Exception {
        //半夜不做事
        Datetime time = Datetime.Now();
        int hours = time.getHours();
        if (hours > 1 && hours < 6) {
            return;
        }

        if (_init == false) {
            _init = true;

            //第一次启动时重置所有服务状态
            DbWaterRegApi.resetServiceState();
        }


        //取出待处理的服务（已启用的服务）
        List<ServiceModel> list = DbWaterRegApi.getServiceList();

        for (ServiceModel sev : list) {
            check(sev);
        }
    }

    //检测服务，并尝试报警
    private void check(ServiceModel sev) {
        if (sev.check_type > 0) {
            //主到签到模式
            check_type1(sev);
        } else {
            //被动检测模式
            check_type0(sev);
        }
    }

    //主到签到模式
    private void check_type1(ServiceModel sev) {
        long times = new Timespan(sev.check_last_time).seconds();

        if (times < 10) {
            //对签到型的服务进行检查 (10s内，是否有签到过)
            //
            DbWaterRegApi.udpService1(sev.service_id, sev, 0);

            if (sev.check_error_num >= 2) { //之前2次坏的，现在好了提示一下
                AlarmUtil.tryAlarm(sev, true, 0);
            }
        } else {
            if (sev.is_unstable && sev.check_error_num >= 2) {
                //
                // 如果为非稳定服务，且出错2次以上；删掉
                //
                DbWaterRegApi.delService(sev.service_id);
            } else {
                //
                // 如果稳定服务，则提示出错
                //
                DbWaterRegApi.udpService1(sev.service_id, sev, 1);

                if (sev.check_error_num >= 2) { //之前2次坏的，现在好了提示一下
                    //报警，30秒一次
                    //
                    if (LockUtils.tryLock(Config.water_service_name, "sev_check_" + sev.service_id, 30)) {
                        AlarmUtil.tryAlarm(sev, false, 0);
                    }
                }
            }
        }
    }

    //被动检测模式
    private void check_type0(ServiceModel sev) {
        if (TextUtils.isEmpty(sev.check_url)) {
            return;
        }

        if (sev.state == 1 && new Timespan(sev.check_last_time).seconds() < 30) {
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

        if (url.startsWith("http") == false) {
            return;
        }

        try {
            DbWaterRegApi.setServiceState(sev.service_id, 1);//设为;正在处理中

            /**
             * callback:
             * isOk:请求是否成功
             * code:如果成功，状态码为何?
             * hint:如果出错，提示信息?
             */

            String url2 = url;
            HttpUtilEx.getStatusByAsync(url, (isOk, code, hint) -> {
                if (code >= 200 && code < 400) { //正常
                    DbWaterRegApi.udpService0(sev.service_id, 0, code + "");

                    if (sev.check_error_num >= 2) { //之前2次坏的，现在好了提示一下
                        AlarmUtil.tryAlarm(sev, true, code);
                        //通知给网关
                        gatewayNotice(sev);
                    }
                } else {
                    //出错
                    if (sev.is_unstable && sev.check_error_num >= 2 && !isOk) {
                        //
                        // 如果为非稳定服务，且出错2次以上，且是网络错误；删掉
                        //
                        DbWaterRegApi.delService(sev.service_id);
                    } else {
                        DbWaterRegApi.udpService0(sev.service_id, 1, code + "");
                        LogUtil.error(getName(), sev.service_id + "", sev.name + "@" + sev.address, url2 + "，" + hint);

                        if (sev.check_error_num >= 2) {//之前好的，现在坏了提示一下
                            AlarmUtil.tryAlarm(sev, false, code);
                            if (sev.check_error_num == 2) {
                                gatewayNotice(sev);
                            }
                        }
                    }
                }
            });
        } catch (Throwable ex) { //出错
            DbWaterRegApi.udpService0(sev.service_id, 1, ex.getMessage());
            LogUtil.error(this, sev.service_id + "", sev.name + "@" + sev.address, ex);
        }
    }

    //通知负载更新
    private void gatewayNotice(ServiceModel sev) {
        if (sev.name.contains(":")) {
            return;
        }

        if (DbWaterCfgApi.hasGateway(sev.name) == false) {
            return;
        }

        //通知网关，更新负载
        WaterClient.Notice.updateCache("upstream:" + sev.name);
    }
}
