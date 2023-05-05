package watersev.controller;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.WW;
import org.noear.water.utils.LockUtils;
import watersev.dso.LogUtil;
import watersev.dso.service.DetAppService;
import watersev.dso.service.DetCaService;

/**
 * 应用监视（可集群，可多实例运行。同时间，只会有一个节点有效）
 *
 * @author noear
 * */
@Component
public final class DetController implements IJob {
    @Inject
    DetAppService detAppService;

    @Inject
    DetCaService detCaService;

    @Override
    public String getName() {
        return "det";
    }

    @Override
    public int getInterval() {
        return 1000 * 60; //实际是：60s 跑一次
    }


    @Override
    public void exec() throws Throwable {
        RegController.addService("watersev-" + getName());

        if (LockUtils.tryLock(WW.watersev_det, getName(), 60)) {
            try {
                detAppService.execDo();
            } catch (Throwable e) {
                LogUtil.error(getName(), "", Utils.throwableToString(e));
            }
        }

        if (LockUtils.tryLock(WW.watersev_det, getName(), 60 * 60) == false) {
            try {
                detCaService.execDo();
            } catch (Throwable e) {
                LogUtil.error(getName(), "", Utils.throwableToString(e));
            }
        }
    }
}
