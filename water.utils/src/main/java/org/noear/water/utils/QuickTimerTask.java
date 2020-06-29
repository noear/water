package org.noear.water.utils;


import java.util.concurrent.*;

public abstract class QuickTimerTask implements Runnable {
    public static final ScheduledExecutorService EXECUTOR_SERVICE = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "Quick Timer");
            thread.setDaemon(true);
            return thread;
        }
    });

    public QuickTimerTask() {
        EXECUTOR_SERVICE.scheduleAtFixedRate(this, getDelay(), getPeriod(), TimeUnit.MILLISECONDS);
    }

    public static void cancelQuickTask() {
        EXECUTOR_SERVICE.shutdown();
    }

    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period) {
        return EXECUTOR_SERVICE.scheduleAtFixedRate(command, initialDelay, period, TimeUnit.MILLISECONDS);
    }


    /**
     * 获取定时任务的延迟启动时间
     */
    protected long getDelay() {
        return 0;
    }

    /**
     * 获取定时任务的执行频率
     *
     * @return
     */
    protected abstract long getPeriod();
}