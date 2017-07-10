package com.github.naturs.text.recorder;

/**
 * 日志打印接口
 *
 * <p>
 *
 * Created by naturs on 2017/7/1.
 */
public interface LogPrinter {

    /**
     * 是否可以打印日志
     * @param tag 日志tag
     */
    boolean isLoggable(String tag);

    /**
     * 打印普通log日志
     */
    void print(String tag, String message);

    /**
     * 打印异常信息
     */
    void print(String tag, Throwable t);
}
