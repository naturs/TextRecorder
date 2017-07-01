package com.github.naturs.text.recorder;

/**
 * 用来打印日志的工具类
 * Created by naturs on 2017/7/1.
 */
public class TextLineLogPrinter {

    private static LogPrinter sLogPrinter;

    static void init(LogPrinter printer) {
        sLogPrinter = printer;
    }

    /**
     * 是否可以打印日志
     * @param tag 日志tag
     */
    public static boolean isLoggable(String tag) {
        return sLogPrinter != null && sLogPrinter.isLoggable(tag);
    }

    /**
     * 打印普通log日志
     */
    public static void print(String tag, String message) {
        if (sLogPrinter != null) {
            sLogPrinter.print(tag, message);
        }
    }

    /**
     * 打印异常信息
     */
    public static void print(String tag, Throwable t) {
        if (sLogPrinter != null) {
            sLogPrinter.print(tag, t);
        }
    }

}
