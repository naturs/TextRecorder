package com.github.naturs.text.recorder.converter;

import com.github.naturs.text.recorder.MarkdownTextLine;
import com.github.naturs.text.recorder.TextLine;
import com.github.naturs.text.recorder.TextLineConverter;

/**
 * 处理{@link MarkdownTextLine}的工厂类
 * <p>
 * Created by naturs on 2017/7/9.
 */
public class MarkdownTextLineConverterFactory extends TextLineConverter.Factory {

    public static MarkdownTextLineConverterFactory create() {
        return new MarkdownTextLineConverterFactory();
    }

    @Override
    public TextLineConverter converter(Class<? extends TextLine> type) {
        if (isEqualsType(type, MarkdownTextLine.class)) {
            return new MarkdownTextLineConverter();
        }
        return null;
    }
}
