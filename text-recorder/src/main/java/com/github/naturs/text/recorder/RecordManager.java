package com.github.naturs.text.recorder;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by naturs on 2017/6/27.
 */
class RecordManager {

    private Scheduler scheduler;
    private TextLineConverter.Factory defaultConverterFactory;
    private TextLineProcessor.Factory defaultProcessorFactory;
    private TextLineEmitter emitter;

    void init(Scheduler scheduler,
              TextLineConverter.Factory defaultConverterFactory,
              TextLineProcessor.Factory defaultProcessorFactory) {
        this.scheduler = scheduler;
        this.defaultConverterFactory = defaultConverterFactory;
        this.defaultProcessorFactory = defaultProcessorFactory;
        emitter = new TextLineEmitLoop(this);
    }

    void schedule(Runnable r) {
        scheduler.schedule(r);
    }

    void process(TextLineBundle bundle) throws Exception {
        // 每次提交的TextLine必须为同一类型
        final Class<? extends TextLine> type = checkActualType(bundle.lines());

        bundle.addConverterFactory(defaultConverterFactory);
        bundle.addProcessorFactory(defaultProcessorFactory);

        TextLineProcessor processor = null;
        ArrayList<TextLineProcessor.Factory> processorFactories = bundle.processorFactories();
        for (TextLineProcessor.Factory factory : processorFactories) {
            processor = factory.processor(type);
            if (processor != null) {
                break;
            }
        }

        if (processor == null) {
            throw new IllegalStateException("Could not locate TextLineProcessor for TextLine:" + type);
        }

        TextLineConverter converter = null;
        ArrayList<TextLineConverter.Factory> converterFactories = bundle.converterFactories();
        for (TextLineConverter.Factory factory : converterFactories) {
            converter = factory.converter(type);
            if (converter != null) {
                break;
            }
        }

        if (converter != null) {
            for (TextLine line : bundle.lines()) {
                line.setConvertedMessage(converter.convert(type, line));
            }
        }

        processor.process(type, bundle.lines());
    }

    void submit(TextLineBundle bundle) {
        emitter.submit(bundle);
    }

    void emit(TextLineBundle bundle) {
        emitter.emit(bundle);
    }

    private Class<? extends TextLine> checkActualType(List<? extends TextLine> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new NullPointerException("The submitted lines has no content.");
        }
        Class<? extends TextLine> clazz = null;
        for (TextLine line : lines) {
            Class<? extends TextLine> temp = line.getClass();
            if (clazz == null) {
                clazz = temp;
            } else if (!clazz.equals(temp)) {
                throw new IllegalStateException("The submitted lines has different type, e.g:" +
                    clazz + ", " + temp);
            }
        }
        return clazz;
    }
}
