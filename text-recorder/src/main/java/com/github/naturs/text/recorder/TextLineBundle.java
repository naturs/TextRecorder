package com.github.naturs.text.recorder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 对{@link TextLine}的数据进行封装
 *
 * <p/>
 *
 * Created by naturs on 2017/6/28.
 */
class TextLineBundle {

    private final List<TextLine> lines;
    private final ArrayList<TextLineConverter.Factory> converterFactories;
    private final ArrayList<TextLineProcessor.Factory> processorFactories;

    TextLineBundle(List<TextLine> lines,
                   TextLineConverter.Factory converterFactory,
                   TextLineProcessor.Factory processorFactory) {
        if (lines != null) {
            lines = new ArrayList<TextLine>(lines);
            this.lines = Collections.unmodifiableList(lines);
        } else {
            this.lines = null;
        }

        this.converterFactories = new ArrayList<TextLineConverter.Factory>();
        if (converterFactory != null) {
            this.converterFactories.add(converterFactory);
        }

        this.processorFactories = new ArrayList<TextLineProcessor.Factory>();
        if (processorFactory != null) {
            this.processorFactories.add(processorFactory);
        }
    }

    boolean isEmpty() {
        return lines == null || lines.isEmpty();
    }

    void addConverterFactory(TextLineConverter.Factory factory) {
        converterFactories.add(factory);
    }

    void addProcessorFactory(TextLineProcessor.Factory factory) {
        processorFactories.add(factory);
    }

    ArrayList<TextLineConverter.Factory> converterFactories() {
        return converterFactories;
    }

    ArrayList<TextLineProcessor.Factory> processorFactories() {
        return processorFactories;
    }

    List<TextLine> lines() {
        return lines;
    }
}
