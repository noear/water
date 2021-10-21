package waterapi.dso;

import org.noear.solon.core.event.EventBus;
import org.noear.water.utils.EventPipeline;
import waterapi.dso.db.DbWaterLogApi;
import waterapi.models.TrackEvent;

import java.util.List;

/**
 * @author noear 2021/4/25 created
 */
public class TrackSqlPipelineLocal extends EventPipeline<TrackEvent> {
    private static TrackSqlPipelineLocal singleton = new TrackSqlPipelineLocal();

    public static TrackSqlPipelineLocal singleton() {
        return singleton;
    }

    private TrackSqlPipelineLocal() {
        super();
    }

    @Override
    protected void handler(List<TrackEvent> logEvents) {
        try {
            DbWaterLogApi.addTrackAll("water_exam_log_sql", logEvents);
        } catch (Throwable ex) {
            EventBus.push(ex); //todo: EventBus.pushAsyn(ex);
        }
    }
}
