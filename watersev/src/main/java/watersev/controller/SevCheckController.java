package watersev.controller;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.WW;
import org.noear.water.WaterClient;
import org.noear.water.dso.GatewayUtils;
import org.noear.water.utils.*;
import watersev.dso.AlarmUtil;
import watersev.dso.LogUtil;
import watersev.dso.db.DbWaterRegApi;
import watersev.models.water_reg.ServiceModel;
import watersev.utils.HttpUtilEx;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

/**
 * 服务检测（已支持 is_unstable）（可集群，可多实例运行。同时间，只会有一个节点有效）
 *
 * @author noear
 * */
@Component
public final class SevCheckController implements IJob {
    @Override
    public String getName() {
        return "sevchk";
    }

    @Override
    public int getInterval() {
        return 1000 * 5;
    }


    @Override
    public void exec() throws Throwable {
        RegController.addService("watersev-" + getName());

        //尝试获取锁（1秒内只能调度一次），避免集群切换时，多次运行
        //
        if (LockUtils.tryLock(WW.watersev_sevchk, WW.watersev_sevchk, 4)) {
            exec0();
        }
    }

    private void exec0() throws SQLException {
        //取出待处理的服务（已启用的服务）
        List<ServiceModel> list = DbWaterRegApi.getServiceList();

        for (ServiceModel sev : list) {
            check(sev);
        }
    }

    //检测服务，并尝试报警
    private void check(ServiceModel sev) {
        Thread.currentThread().setName("sev-c-" + sev.service_id);

        if (sev.check_type > 0) {
            //主到签到模式
            check_type1(sev);
        } else {
            //被动检测模式
            check_type0(sev);
        }
    }

    /**
     * 主到签到模式
     */
    private void check_type1(ServiceModel sev) {
        long seconds = new Timespan(sev.check_last_time).seconds();


        if (seconds < 10) {
            //对签到型的服务进行检查
            //
            DbWaterRegApi.udpService1(sev.service_id, 0);

            if (sev.check_error_num >= 2) {
                //日志
                LogUtil.info(getName(), sev.address, sev.name + "@" + sev.address + ": since the last check-in time: " + seconds + "s" );

                //之前2次坏的，现在好了提示一下
                AlarmUtil.tryAlarm(sev, true, 0);
            }
        } else {
            //日志
            LogUtil.warn(getName(), sev.address, sev.name + "@" + sev.address + ": did not checkin time: " + seconds + "s");

            //超过8秒的，说明有问题了 //默认5秒会签到
            if (sev.is_unstable && sev.check_error_num >= 2) {
                //
                // 如果为非稳定服务，且出错2次以上；删掉
                //
                DbWaterRegApi.delService(sev.service_id);
                DbWaterRegApi.delConsumer(sev.address);
                LogUtil.warn(getName(), sev.address, sev.name + "@" + sev.address + ": had delete");
            } else {
                //
                // 如果稳定服务，则提示出错
                //
                DbWaterRegApi.udpService1(sev.service_id, 1);

                if (sev.check_error_num >= 2) { //之前2次坏的，现在好了提示一下
                    //报警，30秒一次
                    //
                    if (LockUtils.tryLock(WW.watersev_sevchk, "sev-a-" + sev.service_id, 30)) {
                        AlarmUtil.tryAlarm(sev, false, 0);
                    }
                }
            }
        }
    }

    /**
     * 被动检测模式
     */
    private void check_type0(ServiceModel sev) {
        if (TextUtils.isEmpty(sev.check_url)) {
            return;
        }

        String urlTmp = sev.check_url;
        if (urlTmp.indexOf("://") < 0) {
            if (sev.address.indexOf("://") > 0) {
                urlTmp = sev.address + sev.check_url;
            } else {
                urlTmp = "http://" + sev.address + sev.check_url;
            }
        }

        String url = urlTmp;

        if (url.startsWith("http://") || url.startsWith("https://")) {
            check_type0_http(sev, url);
            return;
        }

        if (url.startsWith("://")) {
            RunUtils.runAsyn(()->{
                check_type0_tcp(sev, url);
            });
            return;
        }
    }

    private void check_type0_tcp(ServiceModel sev, String url) {
        String nameAndIp = sev.name + "@" + sev.address;

        try {
            URI uri = URI.create(url);

            //ping 检测
            PingUtils.ping(uri.getAuthority(), 3000);

            DbWaterRegApi.udpService0(sev.service_id, 0, "");
            WaterClient.Track.trackCount("_waterchk", "service", nameAndIp, 1, 0);


            if (sev.check_error_num >= 2) { //之前2次坏的，现在好了提示一下
                AlarmUtil.tryAlarm(sev, true, 200);
                //通知给网关
                GatewayUtils.notice(sev.tag, sev.name);
            }
        } catch (Throwable ex) {
            WaterClient.Track.trackCount("_waterchk", "service", nameAndIp, 1, 1);

            if (sev.is_unstable && sev.check_error_num >= 2) {
                //
                // 如果为非稳定服务，且出错2次以上，且是网络错误；删掉
                //
                DbWaterRegApi.delService(sev.service_id);
                DbWaterRegApi.delConsumer(sev.address);
            } else {
                DbWaterRegApi.udpService0(sev.service_id, 1, "0");
                LogUtil.warn(getName(), sev.address, sev.name + "@" + sev.address + "::\n" + Utils.throwableToString(ex));

                //开始告警
                if (sev.check_error_num >= 2) {//之前好的，现在坏了提示一下
                    //报警，30秒一次
                    //
                    if (LockUtils.tryLock(WW.watersev_sevchk, "sev-a-" + sev.service_id, 30)) {
                        AlarmUtil.tryAlarm(sev, false, 0);
                    }

                    if (sev.check_error_num == 2) {
                        GatewayUtils.notice(sev.tag, sev.name);
                    }
                }
            }
        }
    }

    private void check_type0_http(ServiceModel sev, String url) {
        String nameAndIp = sev.name + "@" + sev.address;

        try {
            /**
             * callback:
             * isOk:请求是否成功
             * code:如果成功，状态码为何?
             * hint:如果出错，提示信息?
             */

            String url2 = url;

            //最长5秒会回调
            HttpUtilEx.getStatusByAsync(url, (isOk, code, hint) -> {
                Thread.currentThread().setName("sev-c-" + sev.service_id);

                if (code >= 200 && code < 400) { //正常
                    DbWaterRegApi.udpService0(sev.service_id, 0, code + "");


                    WaterClient.Track.trackCount("_waterchk", "service", nameAndIp, 1, 0);

                    if (sev.check_error_num >= 2) { //之前2次坏的，现在好了提示一下
                        AlarmUtil.tryAlarm(sev, true, code);
                        //通知给网关
                        GatewayUtils.notice(sev.tag, sev.name);
                    }
                } else {
                    WaterClient.Track.trackCount("_waterchk", "service", nameAndIp, 1, 1);

                    //出错
                    if (sev.is_unstable && sev.check_error_num >= 2 && !isOk) {
                        //
                        // 如果为非稳定服务，且出错2次以上，且是网络错误；删掉
                        //
                        DbWaterRegApi.delService(sev.service_id);
                        DbWaterRegApi.delConsumer(sev.address);
                    } else {
                        DbWaterRegApi.udpService0(sev.service_id, 1, code + "");
                        LogUtil.warn(getName(), sev.address, sev.name + "@" + sev.address + "\n" + url2 + ", " + hint);

                        if (sev.check_error_num >= 2) {//之前好的，现在坏了提示一下
                            //报警，30秒一次
                            //
                            if (LockUtils.tryLock(WW.watersev_sevchk, "sev-a-" + sev.service_id, 30)) {
                                AlarmUtil.tryAlarm(sev, false, code);
                            }

                            if (sev.check_error_num == 2) {
                                GatewayUtils.notice(sev.tag, sev.name);
                            }
                        }
                    }
                }
            });
        } catch (Throwable ex) { //出错
            WaterClient.Track.trackCount("_waterchk", "service", nameAndIp, 1, 1);

            DbWaterRegApi.udpService0(sev.service_id, 1, ex.getMessage());
            LogUtil.warn(getName(), sev.address, sev.name + "@" + sev.address + "\n" + Utils.throwableToString(ex));
        }
    }
}
