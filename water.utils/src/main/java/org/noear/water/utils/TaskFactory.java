package org.noear.water.utils;


import org.noear.water.utils.ext.Act0Ex;

public class TaskFactory {
    public static void run(ITask task) {
        new Thread(() -> {
            doRun(task);
        }).start();
    }

    public static void run(long interval, Act0Ex runnable) {
        run(new TaskImp(interval, runnable));
    }

    private static void doRun(ITask task) {
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
    }

    protected static class TaskImp implements ITask {
        long interval;
        Act0Ex runnable;

        TaskImp(long interval, Act0Ex runnable) {
            this.interval = interval;
            this.runnable = runnable;
        }

        @Override
        public long getInterval() {
            return this.interval;
        }

        @Override
        public void exec() throws Throwable {
            runnable.run();
        }
    }
}
