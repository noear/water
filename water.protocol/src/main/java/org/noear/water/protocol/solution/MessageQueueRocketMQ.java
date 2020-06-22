package org.noear.water.protocol.solution;

import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.noear.water.protocol.IMessageQueue;

import java.util.List;


/**
 *
 * https://www.cnblogs.com/enenen/p/12773099.html
 *
 * https://www.cnblogs.com/markLogZhu/p/12545597.html
 *
 * https://segmentfault.com/a/1190000021240352?utm_source=tag-newest
 *
 * */
public class MessageQueueRocketMQ implements IMessageQueue {
    String _queue_name;
    final String group_name = "water.message";
    final String server = "127.0.0.1:9876";

    @Override
    public void push(String msg) {
        DefaultMQProducer producer = new DefaultMQProducer(group_name);
        producer.setNamesrvAddr(server);
        //发送超时时间，默认3000 单位ms
        producer.setSendMsgTimeout(5000);

        try {

            producer.start();

            Message msgX = new Message(_queue_name, msg.getBytes());
            SendResult send = producer.send(msgX);

            if (send.getSendStatus().equals(SendStatus.SEND_OK)) {
                //发送成功处理
            }else {
                //发送失败处理
            }
        } catch (Exception e) {
            //发送失败处理
            e.printStackTrace();
        } finally {
            //正式环境不要发完就就shutdown，要在应用退出时再shutdown。
            producer.shutdown();
        }

    }

    @Override
    public String poll() {
        DefaultLitePullConsumer consumer = new DefaultLitePullConsumer(group_name);

        consumer.setNamesrvAddr(server);
        //要消费的topic，可使用tag进行简单过滤
        consumer.subscribe(_queue_name, "*");
        //一次最大消费的条数
        consumer.setPullBatchSize(1);
        //无消息时，最大阻塞时间。默认5000 单位ms
        consumer.setPollTimeoutMillis(5000);

        try {
            consumer.start();

            //拉取消息，无消息时会阻塞
            List<MessageExt> msgs = consumer.poll();
            //同步消费位置。不执行该方法，应用重启会存在重复消费。
            consumer.commitSync();

            if (msgs.size() > 0) {
                return new String(msgs.get(0).getBody());
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            consumer.shutdown();
        }
    }


    @Override
    public long count() {
        return 0;
    }

}
