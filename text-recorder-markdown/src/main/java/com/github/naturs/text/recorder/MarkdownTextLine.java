package com.github.naturs.text.recorder;

/**
 *
 * Created by naturs on 2017/7/8.
 */
public class MarkdownTextLine extends GenericTextLine {

    public static MarkdownTextLine with() {
        return new MarkdownTextLine(null);
    }

    public static MarkdownTextLine with(Object tag) {
        return new MarkdownTextLine(null);
    }

    private MarkdownTextLine(Object tag) {
        super(tag, MarkdownTextLine.class);
    }

    /**
     * 设置普通文本
     * @param message 文本内容
     */
    public MarkdownTextLine text(String message) {
        return text(false, message);
    }

    /**
     * 设置普通文本
     * @param rawMessage 是否只显示文本内容，即不显示时间、方法等信息
     * @param message 文本内容
     */
    public MarkdownTextLine text(boolean rawMessage, String message) {
        setMessageType(GenericTextLine.TYPE_MESSAGE_NORMAL);
        setRawMessage(rawMessage);
        setMessage(message);
        return this;
    }

    /**
     * 设置异常信息
     * @param message 异常信息描述
     * @param t 异常
     */
    public MarkdownTextLine throwable(String message, Throwable t) {
        setMessageType(GenericTextLine.TYPE_MESSAGE_NORMAL);
        setRawMessage(false);
        setMessage(message);
        setThrowable(t);
        return this;
    }

    /**
     * 设置json文本
     * @param message 描述信息
     * @param json json内容
     */
    public MarkdownTextLine json(String message, String json) {
        setMessageType(GenericTextLine.TYPE_MESSAGE_JSON);
        setRawMessage(false);
        setMessage(message);
        setExtraMessage(json);
        return this;
    }

    /**
     * 设置xml文本
     * @param message 描述信息
     * @param xml xml内容
     */
    public MarkdownTextLine xml(String message, String xml) {
        setMessageType(GenericTextLine.TYPE_MESSAGE_XML);
        setRawMessage(false);
        setMessage(message);
        setExtraMessage(xml);
        return this;
    }

    /**
     * 设置空行
     */
    public MarkdownTextLine blankLine() {
        setMessageType(GenericTextLine.TYPE_BLANK_LINE);
        setRawMessage(true);
        return this;
    }

    /**
     * 设置分隔线
     */
    public MarkdownTextLine divider() {
        setMessageType(GenericTextLine.TYPE_DIVIDER);
        setRawMessage(true);
        return this;
    }
}
