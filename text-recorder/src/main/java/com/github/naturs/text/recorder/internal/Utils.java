package com.github.naturs.text.recorder.internal;

import java.io.Closeable;
import java.io.Flushable;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 自带的工具类
 * Created by naturs on 2017/6/27.
 */
public class Utils {

    /**
     * 将异常堆栈转换成字符串
     */
    public static String getStackTrace(Throwable t) {
        if (t == null) {
            return null;
        }
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString().trim();
    }

    public static StackTraceElement getStackTraceElement(Class<?> invokeClass) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        StackTraceElement targetElement = null;

        boolean shouldTrace = false;
        for (StackTraceElement element : stackTrace) {
            boolean isInvokeMethod = element.getClassName().equals(invokeClass.getName());
            if (shouldTrace && !isInvokeMethod) {
                targetElement = element;
                break;
            }
            shouldTrace = isInvokeMethod;
        }
        return targetElement;
    }

    public static void flushQuietly(Flushable flushable) {
        if (flushable != null) {
            try {
                flushable.flush();
            } catch (Exception ignore) {
            }
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignore) {
            }
        }
    }
}
