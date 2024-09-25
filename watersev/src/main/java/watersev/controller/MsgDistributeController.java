package watersev.controller;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.solon.cloud.model.Instance;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.handle.ContextEmpty;
import org.noear.solon.core.handle.ContextUtil;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.WW;
import org.noear.water.protocol.MsgBroker;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.model.message.*;
import org.noear.water.utils.*;
import org.noear.water.utils.ext.Act4;
import watersev.dso.AlarmUtil;
import watersev.dso.LogUtil;
import watersev.dso.MsgUtils;
import watersev.dso.db.DbWaterMsgApi;
import watersev.dso.db.DbWaterRegApi;
import watersev.models.StateTag;
import watersev.models.water_cfg.BrokerHolder;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 消息派发器（分发给订阅者，并派发）（可集群，可多实例运行。会分散broker）
 *
 * 订阅类型（0,1异步等待 ; 2异步不等待并设为成功 ; 3异步不待等并设为处理中 ;）
 * 消息状态（-2无派发对象 ; -1:忽略；0:未处理；1处理中；2已完成；3派发超次数）
 *
 * @author noear
 * */
@Slf4j
@Component
public final class MsgDistributeController implements IJob {
    static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    Map<String, BrokerHolder> brokerHolderMap = new HashMap<>();

    @Override
    public String getName() {
        return "msgdis";
    }

    @Override
    public int getInterval() {
        return 100; //让CPU稍微休息下
    }

    @Override
    public void exec() throws Exception {
        RegController.addService("watersev-" + getName());

        //获取集群节点列表（内部缓存1秒）
        List<String> sevList = DbWaterRegApi.getWaterServiceList(WW.watersev_msgdis)
                .stream()
                .filter(s -> new Timespan(s.check_last_time).seconds() < 10)
                .map(s -> s.address)//提取地址
                .sorted() //排序，用于定位自己的索引位
                .collect(Collectors.toList()); //转为List

        int sevSize = sevList.size();
        int sevIndex = sevList.indexOf(Instance.local().address());

        //如果没有节点数量，退出本次处理
        if (sevIndex < 0 || sevSize == 0) {
            LogUtil.warn(this.getName(), "", "sevIndex=" + sevIndex + ",sevSize=" + sevSize);
            return;
        }

        /**
         * 如果 sevSize(3) > broSize(2) || sevSize(3) > broSize(1)。则：1(1),2(2),3(1) || 1(1),2(1),3(1)
         * 如果 sevSize(1) < broSize(3) || sevSize(2) < broSize(3)。则：1(1),1(2),1(3) || 1(1),2(2),3(1)
         * */

        List<MsgBroker> broList = ProtocolHub.getMsgBrokerList();

        for (int broIndex = 0; broIndex < broList.size(); broIndex++) {
            if (sevSize > broList.size()) {
                if (broIndex % broList.size() != sevIndex % broList.size()) {//todo:超过服务顺位的，可让一个bro跑多个sev上
                    //如果不是集群索引位，跳过（节点不会干相同的事!）
                    LogUtil.warn(this.getName(), "", "broIndex % broList.size() != sevIndex % broList.size()");
                    continue;
                }
            } else {
                if (broIndex % sevSize != sevIndex) {
                    //如果不是集群索引位，跳过（节点不会干相同的事!）
                    LogUtil.warn(this.getName(), "", "broIndex % sevSize != sevIndex");
                    continue;
                }
            }


            MsgBroker msgBroker = broList.get(broIndex);
            BrokerHolder brokerHolder = brokerHolderMap.get(msgBroker.getName());

            if (brokerHolder == null) {
                //如果是第一次
                brokerHolder = new BrokerHolder(msgBroker);
                brokerHolderMap.put(msgBroker.getName(), brokerHolder);
            }

            //如果之前有，检检是否已停?
            if (brokerHolder.started) {
                continue;
            }

            exec0(brokerHolder);
        }
    }

    private void exec0(BrokerHolder brokerHolder) {
        //
        //引处，不需要再做索
        //
        RunUtils.runAsyn(() -> {
            exec1(brokerHolder);
        });
    }

    private void exec1(BrokerHolder brokerHolder) {
        try {
            brokerHolder.started = true;

            //引处，多节点同时也不会出问题
            brokerHolder.getQueue().pollGet(msg_id_str -> {
                if (TextUtils.isEmpty(msg_id_str)) {
                    return;
                }

                //改用线程池处理
                executor.execute(() -> distribute(brokerHolder, msg_id_str));
            });

        } catch (Throwable e) {
            log.error("{}", e);
        } finally {
            brokerHolder.started = false;
        }
    }

    private void distribute(MsgBroker msgBroker, String msg_id_str) {
        Thread.currentThread().setName("msg-d-" + msg_id_str);

        try {
            //转为ID
            long msgID = Long.parseLong(msg_id_str);

            //可能会出异常
            MessageModel msg = msgBroker.getSource().getMessageById(msgID);

            //如果找不到消息，则退出
            if (msg == null) {
                return;
            }

            //已成功或已取消费
            if (msg.state > MessageState.processed.code ||
                    msg.state < MessageState.undefined.code) {
                return;
            }

            //设置处理状态
            if (msg.state != MessageState.processed.code) {
                msgBroker.getSource().setMessageState(msg, MessageState.processed);
            }

            //路由
            if (routingDo(msgBroker, msg) == false) {
                return;
            }

            //派发
            distributeDo(msgBroker, msg);
        } catch (Throwable ex) {
            EventBus.publish(ex);
        }
    }

    /**
     * 路由
     */
    private boolean routingDo(MsgBroker msgBroker, MessageModel msg) throws Exception {
        //1.取出订阅者
        Map<Long, SubscriberModel> subsList = DbWaterMsgApi.getSubscriberListByTopic(msg.topic_name);

        //2.如果没有订阅者，就收工
        if (subsList.size() == 0) {
            msgBroker.getSource().setMessageState(msg, MessageState.notarget);//-2);
            return false;
        }

        //3.尝试建立路由关系和派发任务
        if (msg.dist_routed == false) {
            for (SubscriberModel m : subsList.values()) {
                msgBroker.getSource().addDistributionNoLock(msg, m);
            }

            msgBroker.getSource().setMessageRouteState(msg, true);
        }

        return true;
    }

    /**
     * 派发
     */
    private void distributeDo(MsgBroker msgBroker, MessageModel msg) throws Exception {
        try {
            distributeDo0(msgBroker, msg);
        } catch (Throwable ex) {
            msgBroker.getSource().setMessageRepet(msg, MessageState.undefined);//0); //如果失败，重新设为0 //重新操作一次

            LogUtil.writeForMsgByError(msg, msgBroker.getName(), ex);
        }
    }


    private void distributeDo0(MsgBroker msgBroker, MessageModel msg) throws Exception {
        //1.取出订阅者
        Map<Long, SubscriberModel> subsList = DbWaterMsgApi.getSubscriberListByTopic(msg.topic_name);

        //3.获出待分发任务
        List<DistributionModel> distList = new ArrayList<>();
        List<DistributionModel> distList_tmp = msgBroker.getSource().getDistributionListByMsg(msg.msg_id);

        //3.1.过滤可能已不存在的订阅者
        for (DistributionModel d : distList_tmp) { //可能会有什么意外，会产生重复数据
            SubscriberModel s1 = subsList.get(d.subscriber_id);
            if (s1 != null) {
                d._is_unstable = s1.is_unstable;
                distList.add(d);
            }
        }

        //3.2.如果没有可派发对象，就收工
        if (distList.size() == 0) {
            msgBroker.getSource().setMessageState(msg, MessageState.completed);//2);
            return;
        }

        //4.开始派发
        //
        StateTag state = new StateTag(msg, distList.size());

        try {
            ContextUtil.currentSet(new ContextEmpty());
            ContextUtil.current().headerSet(WW.http_header_trace, msg.trace_id);

            for (DistributionModel m : distList) {
                m._start_time = new Date();

                distributeMessage(msgBroker, state, msg, m, distributeMessage_callback);
            }
        } finally {
            ContextUtil.currentRemove();
        }
    }


    private Act4<MsgBroker, StateTag, DistributionModel, Boolean> distributeMessage_callback = (msgBroker, tag, dist, isOk) -> {
        //synchronized (tag.msg.msg_id) {
        //
        //锁一下，确保计数的线程安全
        //
        Thread.currentThread().setName("msg-d-" + dist.msg_id);

        if (isOk) {
            if (msgBroker.getSource().setDistributionState(tag.msg, dist, MessageState.completed)) {//2
                tag.value.incrementAndGet();
            }
        } else {
            msgBroker.getSource().setDistributionState(tag.msg, dist, MessageState.processed);//1);
        }

        //4.返回派发结果
        if (tag.count.incrementAndGet() == tag.total) {
            //处理完了后，解锁
            if (tag.value.get() == tag.total) {
                msgBroker.getSource().setMessageState(tag.msg, MessageState.completed);//2);

                if (tag.msg.dist_count >= 3) {
//                    System.out.print("发送短信报警---\r\n");
                    AlarmUtil.tryAlarm(tag, true, dist);
                }

            } else {
                if (tag.isDistributionEnd()) { //是否已派发结束（超出超大派发次数）
                    msgBroker.getSource().setMessageState(tag.msg, MessageState.excessive);//3);

//                    System.out.print("发送短信报警---\r\n");
                    AlarmUtil.tryAlarm(tag, false, dist);
                } else {
                    msgBroker.getSource().setMessageRepet(tag.msg, MessageState.undefined);//0);

                    if (tag.msg.dist_count >= 3) {
//                        System.out.print("发送短信报警---\r\n");
                        AlarmUtil.tryAlarm(tag, false, dist);
                    }
                }
            }
            //}
        }
    };

    private void distributeMessage(MsgBroker msgBroker, StateTag tag, MessageModel msg, DistributionModel dist, Act4<MsgBroker, StateTag, DistributionModel, Boolean> callback) {

        //1.生成签名
        StringBuilder sb = new StringBuilder(200);
        sb.append(msg.msg_key).append("#");
        sb.append(msg.topic_name).append("#");
        sb.append(msg.content).append("#");
        sb.append(dist.receive_key);

        String sgin = EncryptUtils.md5(sb.toString());

        //2.组装分源的数据
        Map<String, String> params = new HashMap<>();
        params.put("key", msg.msg_key);
        params.put("topic", msg.topic_name);
        params.put("times", msg.dist_count + "");
        params.put("trace_id", msg.trace_id);
        params.put("message", Base64Utils.encode(msg.content));
        params.put("tags", msg.tags);
        params.put("sgin", sgin);

        try {
            //两种模式：http://xxx/path 和 @service/path
            String receive_url2 = MsgUtils.getReceiveUrl2(dist.receive_url);

            if (dist.receive_way == 2 || dist.receive_way == 3) {
                HttpUtils.longHttp(receive_url2)
                        .header(WW.http_header_trace, msg.trace_id)
                        .data(params).postAsync((isOk, resp, ex) -> {
                            distributeResultLog(msgBroker, msg, dist, isOk, resp, ex);
                        });

                //::2:: 进行异步http分发 //不等待 //状态设为已完成
                if (dist.receive_way == 2) {
                    callback.run(msgBroker, tag, dist, true);
                }

                //::3:: 进行异步http分发 //不等待 //状态设为处理中（等消费者主动设为成功）
                if (dist.receive_way == 3) {
                    //推后一小时，可手工再恢复
                    long ntime = DisttimeUtils.distTime(Datetime.Now().addHour(1).getFulltime());
                    msgBroker.getSource().setMessageState(msg, MessageState.processed, ntime);//1
                }
            } else {
                //::0,1
                //
                //3.2.0.进行异步http分发
                HttpUtils.longHttp(receive_url2)
                        .header(WW.http_header_trace, msg.trace_id)
                        .data(params).postAsync((isOk, resp, ex) -> {
                            boolean isOk2 = distributeResultLog(msgBroker, msg, dist, isOk, resp, ex);
                            callback.run(msgBroker, tag, dist, isOk2);
                        });
            }

        } catch (Exception ex) {
            LogUtil.writeForMsgByError(msg, msgBroker.getName(), dist, ex.getLocalizedMessage());

            callback.run(msgBroker, tag, dist, false);
        }
    }

    private boolean distributeResultLog(MsgBroker msgBroker, MessageModel msg, DistributionModel dist, boolean isOk, Response resp, Exception ex) throws IOException {
        Thread.currentThread().setName("msg-d-" + msg.msg_id);

        dist._duration = new Timespan(dist._start_time).milliseconds();

        String text = null;
        int code = 0;

        if (resp != null) {
            code = resp.code();
            text = resp.body().string();
        }


        try {
            ContextUtil.currentSet(new ContextEmpty());
            ContextUtil.current().headerSet(WW.http_header_trace, msg.trace_id);

            if (isOk) {
                boolean isOk2 = "OK".equals(text);

                if (isOk2) {
                    LogUtil.writeForMsg(msg, msgBroker.getName(), dist, text);
                } else {
                    LogUtil.writeForMsgByError(msg, msgBroker.getName(), dist, text);
                }

                return isOk2;
            } else {
                if (ex == null) {
                    if (text == null) {
                        text = "http error";
                    } else {
                        text = code + " - " + text;
                    }

                    LogUtil.writeForMsgByError(msg, msgBroker.getName(), dist, text);
                } else {
                    LogUtil.writeForMsgByError(msg, msgBroker.getName(), dist, Utils.getFullStackTrace(ex));
                }

                return false;
            }
        } finally {
            ContextUtil.currentRemove();
        }
    }
}
