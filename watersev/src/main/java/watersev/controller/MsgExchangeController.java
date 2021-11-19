package watersev.controller;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.cloud.model.Instance;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.WW;
import org.noear.water.protocol.MsgBroker;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.model.message.MessageModel;
import org.noear.water.protocol.model.message.MessageState;
import org.noear.water.utils.*;
import watersev.dso.LogUtil;
import watersev.dso.db.DbWaterRegApi;
import watersev.models.water_cfg.BrokerHolder;
import watersev.utils.CallUtil;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 消息交换器（从持久层转入队列）（可集群，可多实例运行。会分散broker，便可能会空sev）
 *
 * @author noear
 * */
@Slf4j
@Component
public class MsgExchangeController implements IJob {
    static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    final int _interval_def = 1000;
    int _interval = 500;

    Map<String, BrokerHolder> brokerHolderMap = new HashMap<>();

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
        RegController.addService("watersev-" + getName());

        //获取集群节点列表（内部缓存1秒）
        List<String> sevList = DbWaterRegApi.getWaterServiceList(WW.watersev_msgexg)
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
         * 如果 sevSize(3) > broSize(2) || sevSize(3) > broSize(1)。则：1(1),2(2),3( ) || 1(1),2( ),3( )
         * 如果 sevSize(1) < broSize(3) || sevSize(2) < broSize(3)。则：1(1),1(2),1(3) || 1(1),2(2),3(3)
         * */

        List<MsgBroker> broList = ProtocolHub.getMsgBrokerList();

        for (int broIndex = 0; broIndex < broList.size(); broIndex++) {
            if (sevSize > broList.size()) {
                if (broIndex % broList.size() != sevIndex) { //todo:超过服务顺位的，空一个sev（确保一个bro，不在多个sev上跑）
                    //如果不是集群索引位，跳过（节点不会干相同的事!）
                    LogUtil.warn(this.getName(), "", "broIndex % broList.size() != sevIndex");
                    continue;
                }
            } else {
                if (broIndex % sevSize != sevIndex) {
                    //如果不是集群索引位，跳过（节点不会干相同的事!）//todo:最坏的可能，有一个节点会少处理1秒
                    LogUtil.warn(this.getName(), "", "broIndex % sevSize != sevIndex");
                    continue;
                }
            }

            MsgBroker msgBroker = broList.get(broIndex);
            BrokerHolder brokerHolder = brokerHolderMap.get(msgBroker.getName());

            if (brokerHolder == null) {
                brokerHolder = new BrokerHolder(msgBroker);
                brokerHolderMap.put(msgBroker.getName(), brokerHolder);
            }

            //如果已开始，跳过
            if (brokerHolder.started) {
                continue;
            }

            exec0(brokerHolder);
        }
    }

    private void exec0(BrokerHolder brokerHolder) {
        String lockName = "broker-" + brokerHolder.getName();

        //todo:比任务轮询时间还长，可能不合适？
        if (LockUtils.tryLock(WW.watersev_msgexg, lockName, 1)) {
            CallUtil.asynCall(() -> {
                exec1(brokerHolder);
            });
        }
    }

    private void exec1(BrokerHolder brokerHolder) {
        try {
            brokerHolder.started = true;

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
        Thread.currentThread().setName("msg-e-" + msg.msg_id);

        try {
            long ntime = DisttimeUtils.currTime();
            if (msg.dist_nexttime > ntime) { //如果时间还没到
                return;
            }

            exchangeDo(msgBroker, msg);
        } catch (Throwable ex) {
            LogUtil.writeForMsgByError(msg, msgBroker.getName(), ex);
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

            LogUtil.writeForMsgByError(msg, msgBroker.getName(), ex);
        }
    }
}
