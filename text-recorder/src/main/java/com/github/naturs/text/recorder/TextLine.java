package com.github.naturs.text.recorder;

import com.github.naturs.text.recorder.internal.Utils;

/**
 * 每个{@link TextLine}代表一条记录，但一条记录不一定只有一条信息，可以有多条信息
 *
 * Created by naturs on 2017/6/29.
 */
public abstract class TextLine<T extends TextLine> {

    /**
     * 日志标签，含义不定，可由用户自己确定
     */
    private Object tag;

    /**
     * 是否只显示日志内容，不显示时间、调用方法等信息
     */
    private boolean rawMessage;

    /**
     * 执行日志保存的时间
     */
    private long timestamp;

    /**
     * 执行日志保存的调用栈
     */
    private StackTraceElement stackTrace;

    /**
     * 使用{@link TextLineConverter}转换后的message，如果没有找到合适的Converter，该值为null。
     */
    private String convertedMessage;

    protected TextLine(Class<?> recordClass) {
        this.timestamp = System.currentTimeMillis();
        this.stackTrace = Utils.getStackTraceElement(recordClass);
    }

    public boolean isRawMessage() {
        return rawMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public StackTraceElement getStackTrace() {
        return stackTrace;
    }

    public String getConvertedMessage() {
        return convertedMessage;
    }

    public Object getTag() {
        return tag;
    }

    protected void setRawMessage(boolean rawMessage) {
        this.rawMessage = rawMessage;
    }

    void setTag(Object tag) {
        this.tag = tag;
    }

    void setConvertedMessage(String convertedMessage) {
        this.convertedMessage = convertedMessage;
    }
}
