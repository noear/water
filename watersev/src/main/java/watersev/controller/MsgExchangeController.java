package watersev.controller;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.WaterClient;
import org.noear.water.protocol.MsgBroker;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.model.message.MessageModel;
import org.noear.water.protocol.model.message.MessageState;
import org.noear.water.utils.*;
import watersev.dso.CheckinUtil;
import watersev.dso.LogUtil;
import watersev.dso.db.DbWaterRegApi;
import watersev.models.water_cfg.BrokerHolder;
import watersev.models.water_reg.ServiceSmpModel;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 消息交换器（从持久层转入队列）（可集群，建议只运行1个实例）
 *
 * @author noear
 * */
@Slf4j
@Component
public class MsgExchangeController implements IJob {
    static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);


    final int _interval_def = 1000;
    int _interval = 1000;

    Map<String, BrokerHolder> brokerHolderMap = new HashMap<>();

    @Inject("${water.job.msg.broker}")
    String jobMsgBroker;

    @Override
    public String getName() {
        return "msgexg";
    }

    @Override
    public int getInterval() {
        return _interval;
    }

    @Override
    public void exec() throws Exception {
        CheckinUtil.checkin("watersev-" + getName());

        //控制集群内只有一个节点在跑
        if (getLock() == false) {
            return;
        }

        List<MsgBroker> list = null;

        if (Utils.isEmpty(jobMsgBroker)) {
            list = ProtocolHub.getMsgBrokerList();
        } else {
            list = Arrays.asList(ProtocolHub.getMsgBroker(jobMsgBroker));
        }

        for (MsgBroker msgBroker : list) {
            BrokerHolder brokerHolder = brokerHolderMap.get(msgBroker.getName());

            if (brokerHolder == null) {
                //如果是第一次
                brokerHolder = new BrokerHolder(msgBroker);
                brokerHolderMap.put(msgBroker.getName(), brokerHolder);
            }

            //如果之前有，检检是否已停?
            if (brokerHolder.started) {
                break;
            }

            exec0(brokerHolder);
        }
    }

    private void exec0(BrokerHolder brokerHolder) {
        brokerHolder.started = true;

        new Thread(() -> {
            try {
                while (true) {
                    if (execDo(brokerHolder) == false) {
                        break;
                    }
                }
            } catch (Throwable e) {
                log.error("{}", e);
            } finally {
                brokerHolder.started = false;
            }
        }).start();
    }

    private boolean execDo(MsgBroker msgBroker) throws Exception {
        if (msgBroker.getQueue().count() > 20000) {
            //
            //防止派发机出问题时，队列不会暴掉
            //
            return false;
        }

        long dist_nexttime = System.currentTimeMillis();
        List<MessageModel> msgList = msgBroker.getSource()
                .getMessageListOfPending(5000, dist_nexttime);

        CountDownLatch countDownLatch = new CountDownLatch(msgList.size());

        for (MessageModel msg : msgList) {
            executor.execute(() -> {
                exchange(msgBroker, msg);
                countDownLatch.countDown();
            });
        }

        //等待执行完成，再到下一轮
        countDownLatch.await();

        if (msgList.size() > 0) {
            _interval = _interval_def;
            return true;
        } else {
            _interval = 500;
            return false;
        }
    }

    private void exchange(MsgBroker msgBroker, MessageModel msg) {
        Thread.currentThread().setName("water-msg-e-" + msg.msg_id);

        try {
            long ntime = DisttimeUtils.currTime();
            if (msg.dist_nexttime > ntime) { //如果时间还没到
                return;
            }

            exchangeDo(msgBroker, msg);
        } catch (Throwable ex) {
            LogUtil.writeForMsgByError(msg, ex);
        }
    }

    private void exchangeDo(MsgBroker msgBroker, MessageModel msg) {
        try {
            //1.推送到队列（未来可以转发到不同的队列）
            String msg_id_str = String.valueOf(msg.msg_id);

            msgBroker.getQueue().push(msg_id_str);

            //2.状态改为处理中
            msgBroker.getSource().setMessageState(msg, MessageState.processed);

        } catch (Throwable ex) {
            msgBroker.getSource()
                    .setMessageState(msg, MessageState.undefined);//0); //如果失败，重新设为0 //重新操作一次

            LogUtil.writeForMsgByError(msg, ex);
        }
    }


    //
    // 进行锁控制
    //
    private final String lock_group = "watermsg";
    private final String lock_key = "watermsg_exchange_lock";
    private final String lock_master_key = "watermsg_exchange_lock_master";

    private boolean getLock() throws SQLException {
        //
        //尝试获取锁（1秒内只能调度一次），避免集群切换时，多次运行
        //
        if (LockUtils.tryLock(lock_group, lock_key, 1)) {
            //获取当前集群节点id
            String nodeId = WaterClient.localHost();
            //获取主节点id
            String masterNodeId = LockUtils.getClient().openAndGet(us -> us.key(lock_master_key).get());

            if (TextUtils.isNotEmpty(masterNodeId)) {
                if (nodeId.equals(masterNodeId)) {
                    return true;
                }

                //如果有主节点，遍历集群是否在其中？
                List<ServiceSmpModel> list = DbWaterRegApi.getWatermsgServiceList();
                for (ServiceSmpModel sm : list) {
                    if (sm.meta.contains("sss")) {
                        if (sm.meta.contains("msg")) {
                            //如果通过sss=msg，说明msg服务在跑
                            if (sm.address.equals(masterNodeId)) {
                                //如果找到了，看看是不是当前节点
                                return nodeId.equals(masterNodeId);
                            }
                        }
                    } else {
                        //没有sss指定，说明所有服务在跑（包括：msg）
                        if (sm.address.equals(masterNodeId)) {
                            //如果找到了，看看是不是当前节点
                            return nodeId.equals(masterNodeId);
                        }
                    }
                }
            }

            LockUtils.getClient().open(us -> us.key(lock_master_key).persist().set(nodeId));
            return true;
        } else {
            //获取锁失败，不用管了
            return false;
        }
    }
}
