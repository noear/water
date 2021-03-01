package org.noear.water.utils;

import org.noear.water.utils.ext.Act1;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 写入时，先写到队列
 * 提交时，每次提交100条；消费完后暂停0.1秒
 *
 * @author noear
 * @since 2.0
 * */
public abstract class EventPipeline<Event> implements TaskUtils.ITask {
    private long interval = 500; //必须大于等于100
    private long interval_min = 100;

    private int packetSize = 100; //必须大于等于100
    private int packetSize_min = 100;

    public EventPipeline() {
        TaskUtils.run(this);
    }

    public EventPipeline(long interval,int packetSize) {
        setInterval(interval);
        setPacketSize(packetSize);
        TaskUtils.run(this);
    }

    protected abstract void handler(List<Event> events);

    //
    //
    //

    Queue<Event> queueLocal = new LinkedBlockingQueue<>();

    public void add(Event event) {
        try {
            queueLocal.add(event);
        } catch (Exception ex) {
            //不打印
        }
    }




    /**
     * 获取任务间隔时间
     * */
    @Override
    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        if (interval >= interval_min) {
            this.interval = interval;
        }
    }

    /**
     * 设置管道包大小
     * */
    public void setPacketSize(int packetSize) {
        if (packetSize >= packetSize_min) {
            this.packetSize = packetSize;
        }
    }

    @Override
    public void exec() throws Exception {

        while (true) {
            List<Event> list = new ArrayList<>(packetSize);

            collectDo(list);

            if (list.size() > 0) {
                handler(list);
            } else {
                break;
            }
        }
    }

    private void collectDo(List<Event> list) {
        //收集最多100条日志
        //
        int count = 0;
        while (true) {
            Event event = queueLocal.poll();

            if (event == null) {
                break;
            } else {
                list.add(event);
                count++;

                if (count == packetSize) {
                    break;
                }
            }
        }
    }
}
