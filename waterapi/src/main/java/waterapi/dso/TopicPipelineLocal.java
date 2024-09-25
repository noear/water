package waterapi.dso;

import org.noear.solon.core.event.EventBus;
import org.noear.water.utils.EventPipeline;
import waterapi.dso.db.DbWaterMsgApi;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 写入时，先写到队列
 * 提交时，每次提交100条；消费完后暂停1秒
 *
 * */
public class TopicPipelineLocal extends EventPipeline<String> {
    private static TopicPipelineLocal singleton = new TopicPipelineLocal();

    public static TopicPipelineLocal singleton() {
        return singleton;
    }

    private TopicPipelineLocal() {
        super(200, 200);
    }

    Set<String> topicSet = new HashSet<>();

    @Override
    protected void handle(List<String> topicEvents) {
        try {
            for (String topicName : topicEvents) {
                if (topicSet.contains(topicName) == false) {
                    DbWaterMsgApi.tryAddTopic(topicName);
                    topicSet.add(topicName);
                }
            }
        } catch (Throwable ex) {
            EventBus.publish(ex); //todo: EventBus.pushAsyn(ex);
        }
    }
}