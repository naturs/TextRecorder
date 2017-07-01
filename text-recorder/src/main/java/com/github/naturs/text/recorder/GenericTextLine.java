package com.github.naturs.text.recorder;

/**
 * 通用的记录，包含消息、额外消息、异常、分割线等信息，一般来说，该类已包含大部分的记录信息。
 *
 * Created by naturs on 2017/6/26.
 */
public final class GenericTextLine extends TextLine {

    /**
     * 消息类型：普通消息
     */
    public static final int TYPE_MESSAGE_NORMAL = 0;
    /**
     * 消息类型：带json字符串的消息
     */
    public static final int TYPE_MESSAGE_JSON = 1;
    /**
     * 消息类型：带xml字符串的消息
     */
    public static final int TYPE_MESSAGE_XML = 2;
    /**
     * 消息类型：分割线
     */
    public static final int TYPE_DIVIDER = 3;
    /**
     * 消息类型：空行
     */
    public static final int TYPE_BLANK_LINE = 4;

    /**
     * 日志类型
     */
    private int messageType;
    /**
     * 日志内容
     */
    private String message;
    /**
     * 额外的日志内容
     */
    private String extraMessage;
    /**
     * 异常
     */
    private Throwable throwable;

    public GenericTextLine(int messageType,
                           boolean rawMessage,
                           String message,
                           String extraMessage,
                           Throwable throwable,
                           Class<?> recordClass) {
        super(recordClass);
        this.messageType = messageType;
        setRawMessage(rawMessage);
        this.message = message;
        this.extraMessage = extraMessage;
        this.throwable = throwable;
    }

    public int getMessageType() {
        return messageType;
    }

    public String getMessage() {
        return message;
    }

    public String getExtraMessage() {
        return extraMessage;
    }

    public Throwable getThrowable() {
        return throwable;
    }

}
