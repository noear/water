package org.noear.water.dso;

import org.noear.snack.ONode;
import org.noear.water.WaterAddress;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 任务服务接口
 */
public class JobApi {
    protected final ApiCaller apiCaller;

    public JobApi() {
        apiCaller = new ApiCaller(WaterAddress.getJobApiUrl());
    }


    /**
     * 注册任务
     *
     * @param jobs [name,]
     */
    public boolean register(String tag, String service, Map<String, String> jobs) throws IOException {
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
