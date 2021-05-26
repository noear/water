package org.noear.water.dso;

import org.noear.snack.ONode;
import org.noear.water.WaterAddress;

import java.util.HashMap;
import java.util.Map;

/**
 * 任务服务接口
 */
public class JobApi {
    protected final ApiCaller apiCaller;
    public JobApi(){
        apiCaller = new ApiCaller(WaterAddress.getJobApiUrl());
    }


    /**
     * 注册任务
     */
    public boolean register(String tag, String service, String... names) throws Exception{
        String names_str = String.join(",", names);

        Map<String, String> params = new HashMap<>();
        params.put("tag", tag);
        params.put("service", service);
        params.put("names", names_str);

        String txt = apiCaller.post("/job/register/", params);

        int code = ONode.loadStr(txt).get("code").getInt();
        return code == 1 || code == 200;
    }
}
