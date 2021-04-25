package waterapi.dso;

import org.noear.solon.core.event.EventBus;
import org.noear.water.utils.EventPipeline;
import waterapi.dso.db.DbWaterLogApi;
import waterapi.models.TrackEvent;

import java.util.List;

/**
 * @author noear 2021/4/25 created
 */
public class TrackBcfPipelineLocal extends EventPipeline<TrackEvent> {
    private static TrackBcfPipelineLocal singleton = new TrackBcfPipelineLocal();

    public static TrackBcfPipelineLocal singleton() {
        return singleton;
    }

    private TrackBcfPipelineLocal() {
        super();
    }

    @Override
    protected void handler(List<TrackEvent> logEvents) {
        try {
            DbWaterLogApi.addTrackAll("water_exam_log_bcf", logEvents);
        } catch (Throwable ex) {
            EventBus.pushAsyn(ex);
        }
    }
}
