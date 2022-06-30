package org.noear.water.dso;

import org.noear.water.track.TrackEvent;
import org.noear.water.track.TrackEventBuffer;

import java.util.Map;

/**
 * @author noear 2022/6/30 created
 */
public class TrackPipeline extends TrackEventBuffer {

    @Override
    protected void flush(Map<String, TrackEvent> mainSet, Map<String, TrackEvent> serviceSet, Map<String, TrackEvent> fromSet) throws Throwable {

    }
}
