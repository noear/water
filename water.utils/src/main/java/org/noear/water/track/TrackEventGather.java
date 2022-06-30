package org.noear.water.track;

import java.io.Serializable;
import java.util.Map;

/**
 * @author noear 2022/6/30 created
 */
public class TrackEventGather implements Serializable {
    public Map<String, TrackEvent> mainSet;
    public Map<String, TrackEvent> serviceSet;
    public Map<String, TrackEvent> fromSet;
}
