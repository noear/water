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





    public void addCount(String group, String category, String item, long count) {
        TrackPipeline.singleton().appendCount(group, category, item, count);
    }

    public void addCount(String group, String category, String item, long count, long count5) {
        TrackPipeline.singleton().appendCount(group, category, item, count, count5);
    }

    public void addCount(String group, String category, String item, long count, long count1, long count2, long count5) {
        TrackPipeline.singleton().appendCount(group, category, item, count, count1, count2, count5);
    }


    public void addGauge(String group, String category, String item, long val) {
        if (group.endsWith("_")) {
            TrackPipeline.singleton().append(group, category, item, val);
        } else {
            TrackPipeline.singleton().append(group + "_", category, item, val);
        }
    }

    public void addMeter(String group, String category, String item, long val) {
        TrackPipeline.singleton().append(group, category, item, val);
    }

    public void addMeterAndMd5(String group, String tag, String name, long val) {
        String nameMd5 = getNameMd5(name);
        TrackPipeline.singleton().append(group, tag, nameMd5, val);
    }

    public void addMeterByNode(String group, String _node, long val) {
        if (TextUtils.isNotEmpty(_node)) {
            TrackPipeline.singleton().appendNode(group, _node, val);
        }
    }

    public void addMeterByFrom(String group, String _from, long val) {
        if (TextUtils.isNotEmpty(_from)) {
            TrackPipeline.singleton().appendFrom(group, _from, val);
        }
    }


    @Deprecated
    public void trackCount(String service, String tag, String name, long count) {
        addCount(service, tag, name, count);
    }

    @Deprecated
    public void trackCount(String service, String tag, String name, long count, long count5) {
        addCount(service, tag, name, count, count5);
    }

    @Deprecated
    public void trackCount(String service, String tag, String name, long count, long count1, long count2, long count5) {
        addCount(service, tag, name, count, count1, count2, count5);
    }


    /**
     * 跟踪请求性能
     */
    @Deprecated
    public void track(String service, String tag, String name, long timespan) {
        addMeter(service, tag, name, timespan);
    }

    @Deprecated
    public void trackAndMd5(String service, String tag, String name, long timespan) {
        addMeterAndMd5(service, tag, name, timespan);
    }

    @Deprecated
    public void trackNode(String service, String _node, long timespan) {
        addMeterByNode(service, _node, timespan);
    }

    @Deprecated
    public void trackFrom(String service, String _from, long timespan) {
        addMeterByFrom(service, _from, timespan);
    }

    /////////////////////////////////////////

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
