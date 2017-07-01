package com.github.naturs.text.recorder;

import java.util.ArrayList;

/**
 * 文本记录
 * <p/>
 * Created by naturs on 2017/6/26.
 */
public class TextRecorder {

    private static final RecordManager sManager = new RecordManager();

    private static boolean sInitialized = false;

    public synchronized static void init(Scheduler scheduler,
                            TextLineConverter.Factory defaultConverterFactory,
                            TextLineProcessor.Factory defaultProcessorFactory,
                            LogPrinter printer) {
        if (sInitialized) {
            throw new IllegalStateException(TextRecorder.class.getSimpleName() + " has been initialized.");
        }
        sInitialized = true;
        sManager.init(scheduler, defaultConverterFactory, defaultProcessorFactory);
        TextLineLogPrinter.init(printer);
    }

    // 创建当前对象的线程
    private final Thread creatorThread;
    // 日志内容
    private final ArrayList<TextLine> lines;
    // 日志tag
    private Object tag;
    private TextLineConverter.Factory converterFactory;
    private TextLineProcessor.Factory processorFactory;

    public TextRecorder() {
        creatorThread = Thread.currentThread();
        lines = new ArrayList<TextLine>();
    }

    public TextRecorder tag(Object tag) {
        this.tag = tag;
        return this;
    }

    public void converterFactory(TextLineConverter.Factory factory) {
        this.converterFactory = factory;
    }

    public void processorFactory(TextLineProcessor.Factory factory) {
        this.processorFactory = factory;
    }

    public TextRecorder append(String message) {
        return append(false, message);
    }

    public TextRecorder append(boolean rawMessage, String message) {
        return append(rawMessage, message, null);
    }

    public TextRecorder append(Throwable t) {
        return append(false, null, t);
    }

    public TextRecorder append(boolean rawMessage, Throwable t) {
        return append(rawMessage, null, t);
    }

    public TextRecorder append(String message, Throwable t) {
        return append(false, message, t);
    }

    public TextRecorder append(boolean rawMessage, String message, Throwable t) {
        return append(GenericTextLine.TYPE_MESSAGE_NORMAL, rawMessage, message, null, t);
    }

    public TextRecorder appendJson(String message, String json) {
        return append(GenericTextLine.TYPE_MESSAGE_JSON, false, message, json, null);
    }

    public TextRecorder appendXml(String message, String xml) {
        return append(GenericTextLine.TYPE_MESSAGE_XML, false, message, xml, null);
    }

    public TextRecorder appendBlankLine() {
        return append(GenericTextLine.TYPE_BLANK_LINE, true, null, null, null);
    }

    public TextRecorder appendDivider() {
        return append(GenericTextLine.TYPE_DIVIDER, true, null, null, null);
    }

    public TextRecorder append(int messageType, boolean rawMessage, String message, String extraMessage, Throwable t) {
        TextLine line = new GenericTextLine(messageType, rawMessage, message, extraMessage, t, TextRecorder.class);
        return append(line);
    }

    public TextRecorder append(TextLine line) {
        checkThread();
        if (line == null) {
            return this;
        }
        this.lines.add(line);
        return this;
    }

    /**
     * 同步提交请求
     */
    public void commit() {
        checkThread();
        TextLineBundle bundle = toBundle();
        resetThis();
        sManager.emit(bundle);
    }

    /**
     * 异步提交请求，异步线程可通过{@link Scheduler}指定。
     */
    public void apply() {
        checkThread();
        TextLineBundle bundle = toBundle();
        resetThis();
        sManager.submit(bundle);
    }

    /**
     * 确保每个线程用的是不同的对象，防止数据错乱
     */
    private void checkThread() {
        if (creatorThread != Thread.currentThread()) {
            throw new RuntimeException("Please invoke methods in the thread that create "
                    + getClass().getSimpleName() + " object.");
        }
    }

    private TextLineBundle toBundle() {
        if (lines.isEmpty()) {
            return null;
        }
        for (TextLine line : lines) {
            // 防止自定义的TextLine的tag被覆盖
            if (line.getTag() == null) {
                line.setTag(this.tag);
            }
        }
        return new TextLineBundle(lines, converterFactory, processorFactory);
    }

    private void resetThis() {
        this.tag = null;
        this.lines.clear();
        this.converterFactory = null;
        this.processorFactory = null;
    }
}
