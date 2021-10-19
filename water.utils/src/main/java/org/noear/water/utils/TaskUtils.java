package org.noear.water.utils;


import org.noear.water.utils.ext.Act0Ex;


/**
 * <pre><code>
 *     //直接运行
 *     TaskUtils.run(1000, ()->{
 *        //当前跑完，等1000再跑；一直往复
 *     });
 *
 *     //接口实现运行
 *     TaskUtils.rne(new TaskImp());
 * </code></pre>
 * */
public class TaskUtils {
    public static void run(ITask task) {
        new Thread(() -> {
            doRun(task);
        }, "Simple task").start();
    }

    public static void run(long interval, Act0Ex runnable) {
        run(new TaskImp(interval, 0, runnable));
    }

    public static void run(long interval, long delayed, Act0Ex runnable) {
        run(new TaskImp(interval, delayed, runnable));
    }

    private static void doRun(ITask task) {
        if (task.getDelayed() > 0) {
            try {
                Thread.sleep(task.getDelayed());
            } catch (Throwable ex) {

            }
        }

        while (true) {
            try {
                long time_start = System.currentTimeMillis();
                task.exec();
                long time_end = System.currentTimeMillis();

                if (task.getInterval() == 0) {
                    return;
                }

                if ((time_end - time_start) < task.getInterval()) {
                    Thread.sleep(task.getInterval());
                }

            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }

    public interface ITask {
        long getInterval();

        void exec() throws Throwable;

        default long getDelayed() {
            return 0;
        }
    }

    protected static class TaskImp implements ITask {
        long interval;
        long delayed;
        Act0Ex runnable;

        TaskImp(long interval, long delayed, Act0Ex runnable) {
            this.interval = interval;
            this.delayed = delayed;
            this.runnable = runnable;
        }

        @Override
        public long getInterval() {
            return interval;
        }

        @Override
        public long getDelayed() {
            return delayed;
        }

        @Override
        public void exec() throws Throwable {
            runnable.run();
        }
    }
}
