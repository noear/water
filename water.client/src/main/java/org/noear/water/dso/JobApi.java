package org.noear.water.dso;

import org.noear.snack.ONode;
import org.noear.water.WaterAddress;
import org.noear.water.model.JobM;
import org.noear.water.utils.TextUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分布式任务服务接口
 *
 * @author noear
 * @since 2.0
 */
public class JobApi {
    protected final ApiCaller apiCaller;

    public JobApi() {
        apiCaller = new ApiCaller(WaterAddress.getDefApiUrl());
    }


    /**
     * 注册任务
     *
     * @param jobs [name,]
     */
    public boolean register(String tag, String service, List<JobM> jobs) throws IOException {
        if(TextUtils.isEmpty(tag) || TextUtils.isEmpty(service)){
            return false;
        }

        if(jobs == null || jobs.size() == 0){
            return false;
        }

        String jobs_str = ONode.stringify(jobs);

        Map<String, String> params = new HashMap<>();
        params.put("tag", tag);
        params.put("service", service);
        params.put("jobs", jobs_str);

        String txt = apiCaller.post("/job/register/", params);

        int code = ONode.loadStr(txt).get("code").getInt();
        return code == 1 || code == 200;
    }
}
