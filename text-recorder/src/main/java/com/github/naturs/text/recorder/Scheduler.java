package com.github.naturs.text.recorder;

/**
 * 处理日志的调度器，可用来设置线程
 *
 * Created by naturs on 2017/6/26.
 */
public interface Scheduler {

    /**
     * 处理调度任务
     */
    void schedule(Runnable r);

}
