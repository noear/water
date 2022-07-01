package org.noear.water.dso;

import org.noear.snack.ONode;
import org.noear.water.WaterAddress;
import org.noear.water.WaterSetting;
import org.noear.water.track.TrackEventGather;
import org.noear.water.utils.NamesUtils;
import org.noear.water.utils.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 跟踪服务接口
 *
 * @author noear
 * @since 2.0
 * */
public class TrackApi {




    protected final ApiCaller apiCaller;

    public TrackApi() {
        apiCaller = new ApiCaller(WaterAddress.getDefApiUrl());
    }


    public String getNameMd5(String name) {
        return NamesUtils.getNameMd5(name);
    }

    public String getName(String nameMd5) {
        return NamesUtils.getName(nameMd5);
    }


    public void trackCount(String service, String tag, String name, long count) {
        TrackPipeline.singleton().appendCount(service, tag, name, count);
    }

    public void trackCount(String service, String tag, String name, long count, long count5) {
        TrackPipeline.singleton().appendCount(service, tag, name, count, count5);
    }

    public void trackCount(String service, String tag, String name, long count, long count1, long count2, long count5) {
        TrackPipeline.singleton().appendCount(service, tag, name, count, count1, count2, count5);
    }

    /**
     * 跟踪请求性能
     */
    public void trackAndMd5(String service, String tag, String name, long timespan) {
        String nameMd5 = getNameMd5(name);
        TrackPipeline.singleton().append(service, tag, nameMd5, timespan);
    }

    public void track(String service, String tag, String name, long timespan) {
        TrackPipeline.singleton().append(service, tag, name, timespan);
    }

    public void trackNode(String service, String _node, long timespan) {
        if (TextUtils.isNotEmpty(_node)) {
            TrackPipeline.singleton().appendNode(service, _node, timespan);
        }
    }

    public void trackFrom(String service, String _from, long timespan) {
        if (TextUtils.isNotEmpty(_from)) {
            TrackPipeline.singleton().appendFrom(service, _from, timespan);
        }
    }

    public void appendAll(TrackEventGather gather, boolean async) {
        if (async) {
            WaterSetting.pools.submit(() -> {
                appendAllDo(gather);
            });
        } else {
            appendAllDo(gather);
        }
    }

    private void appendAllDo(TrackEventGather gather) {
        if (gather == null) {
            return;
        }

        String json = ONode.stringify(gather);

        try {
            Map<String, String> map = new HashMap<>();
            map.put("data", json);

            apiCaller.post("/track/add2/", map);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
