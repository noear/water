package org.noear.water.protocol.solution;

import org.noear.water.protocol.MsgQueue;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

/**
 * @author noear 2021/11/1 created
 */
public class MsgQueueLocal implements MsgQueue {
    Queue<String> queueLocal = new LinkedBlockingQueue<>();
    @Override
    public boolean push(String msg_id) {
        queueLocal.add(msg_id);
        return true;
    }

    @Override
    public String poll() {
        return queueLocal.poll();
    }

    @Override
    public void pollGet(Consumer<String> callback) {
        while (true) {
            String msg = queueLocal.poll();

            if (msg == null) {
                break;
            }

            callback.accept(msg);
        }
    }

    @Override
    public long count() {
        return queueLocal.size();
    }

    @Override
    public void close() throws Exception {

    }
}
