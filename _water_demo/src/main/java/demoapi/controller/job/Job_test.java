package demoapi.controller.job;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.cloud.CloudJobHandler;
import org.noear.solon.cloud.annotation.CloudJob;
import org.noear.solon.core.handle.Context;
import org.slf4j.MDC;

/**
 * @author noear 2021/11/7 created
 */
//分布式任务
@Slf4j
@CloudJob(name = "demo_test", cron7x = "0 1 * * * ?")
public class Job_test implements CloudJobHandler {

    @Override
    public void handle(Context ctx) throws Throwable {
        //处理任务...
//        TagsMDC
        MDC.put("tag0", "job");
        log.info("我被调度了");
    }
}