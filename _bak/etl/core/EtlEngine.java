package watersev.dso.etl;


import org.noear.water.WaterClient;
import org.noear.water.utils.Timecount;


//ETL引擎（运行器）
public class EtlEngine {
    private EtlEngine.Handler _handler; //代理可以控制是否停掉
    private EtlEngine.Runnable _runnable;
    private long _interval = 1000 * 1;

    public EtlEngine(Runnable runnable) {
        _runnable = runnable;
    }

    public EtlEngine handler(EtlEngine.Handler handler) {
        _handler = handler;
        return this;
    }

    public void start() {
        EtlLog.write(_runnable.context, _runnable.getClass(), "已启动");

        while (true) {
            try {
                //提供临时停目的机制
                if (_handler != null) {
                    _handler.run(_runnable.context);
                }

                if (_runnable.context.isStop) {
                    EtlLog.write(_runnable.context, _runnable.getClass(), "已停止");
                    return; //停目后退出线程; 等下次启用时被调度任务发现
                }

                do_exec();
            } catch (Exception ex) {

                EtlLog.write(_runnable.context, _runnable.getClass(), "已出错...具体看错误日志");

                EtlLog.error(_runnable.context, _runnable.getClass(), "已出错", ex);
                //如果出错了间隔久点，给人工处理预留时间
                _interval = 1000 * 5;
            }

            try {
                Thread.sleep(_interval);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private int pause_state = 0;

    private void do_exec() throws Exception {
        //真正的执行入口
        if (_runnable.context.isPause) {
            _interval = 1000 * 30;
            if (pause_state == 0) {
                EtlLog.write(_runnable.context, _runnable.getClass(), "暂停运行");
            }
            pause_state = 1;

            EtlLog.write(_runnable.context, _runnable.getClass(), "暂停执行30s******");
        } else {
            if (pause_state == 1) {
                EtlLog.write(_runnable.context, _runnable.getClass(), "恢复运行");
            }
            pause_state = 0;

            _interval = _runnable.context.interval; //按配置来停时间（主要为定时统计并同步做支持）

            Timecount timecount = new Timecount().start();
            //EtlLog.write(_runnable.context, _runnable.getClass(), "开始处理周期");

            _runnable.run();

            long timespan = timecount.stop().milliseconds();
            WaterClient.Registry.track("water-etl", _runnable.getClass().getSimpleName(), _runnable.context.name, timespan);

            //EtlLog.write(_runnable.context, _runnable.getClass(), "完成处理周期：  (" + timeCount.stop(1) + "s)");
        }

        //如果有报警，拉长间隔时间
        if (_runnable.context.alarmCount > 10) {
            _interval = 1000 * 30;
            _runnable.context.alarmCount = 0;
        }
    }

    public static abstract class Runnable {
        protected EtlContext context;

        public abstract void run() throws Exception;
    }

    public interface Handler {
        void run(EtlContext context) throws Exception;
    }
}
