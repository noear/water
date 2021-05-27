package waterapi.controller.job;

import org.noear.snack.ONode;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Result;
import org.noear.solon.extend.validation.annotation.NotEmpty;
import org.noear.solon.extend.validation.annotation.Whitelist;
import waterapi.controller.UapiBase;
import waterapi.dso.LockUtils;
import waterapi.dso.db.DbPassApi;
import waterapi.dso.interceptor.Logging;

import java.util.Map;

/**
 * @author noear 2021/5/26 created
 */
@Logging
@Whitelist
@Controller
public class CMD_job_register extends UapiBase {
    /**
     * @param tag     分类标签
     * @param service 服务名
     * @param jobs    任务-json
     */
    @NotEmpty({"tag", "service", "jobs"})
    @Mapping("/job/register/")
    public Result cmd_exec(String tag, String service, String jobs) throws Exception {
        if (jobs.startsWith("{") == false) {
            return Result.failure();
        }

        if (LockUtils.tryLock(Solon.cfg().appName(), ("job_register_" + tag + "_" + service), 30)) {
            Map<String, String> jobMap = ONode.deserialize(jobs, Map.class);
            StringBuilder buf = new StringBuilder(200);

            for (Map.Entry<String, String> kv : jobMap.entrySet()) {
                buf.setLength(0);

                DbPassApi.addJob(tag, service, kv.getKey(), kv.getValue());
            }
        }

        return Result.succeed();
    }
}
