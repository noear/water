package org.noear.water.dso;

import org.noear.water.WaterClient;
import org.noear.water.track.TrackEvent;
import org.noear.water.track.TrackEventBuffer;
import org.noear.water.track.TrackEventGather;

import java.util.Map;

/**
 * @author noear 2022/6/30 created
 */
public class TrackPipeline extends TrackEventBuffer {
    private static final TrackPipeline singleton = new TrackPipeline();

    public static TrackPipeline singleton() {
        return singleton;
    }

    @Override
    protected void flush(Map<String, TrackEvent> mainSet, Map<String, TrackEvent> serviceSet, Map<String, TrackEvent> fromSet) throws Throwable {
        synchronized (this) {
            TrackEventGather gather = new TrackEventGather();
            gather.mainSet = mainSet;
            gather.serviceSet = serviceSet;
            gather.fromSet = fromSet;

            WaterClient.Track.appendAll(gather, false);

            mainSet.clear();
            serviceSet.clear();
            fromSet.clear();
        }
    }
}
