package com.github.naturs.text.recorder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 * Created by naturs on 2017/6/29.
 */
public class Schedulers {

    private static final Scheduler defaultScheduler = new Scheduler() {

        final Executor executor = Executors.newCachedThreadPool();

        @Override
        public void schedule(Runnable r) {
            executor.execute(r);
        }
    };

    /**
     * 默认的任务调度器
     */
    public static Scheduler io() {
        return defaultScheduler;
    }

}
