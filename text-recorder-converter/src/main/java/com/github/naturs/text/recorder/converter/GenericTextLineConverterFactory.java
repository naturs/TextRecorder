package com.github.naturs.text.recorder.converter;

import com.github.naturs.text.recorder.GenericTextLine;
import com.github.naturs.text.recorder.TextLine;
import com.github.naturs.text.recorder.TextLineConverter;

/**
 * 处理{@link GenericTextLine}的工厂类
 * <p>
 * Created by naturs on 2017/6/28.
 */
public class GenericTextLineConverterFactory extends TextLineConverter.Factory {

    public static GenericTextLineConverterFactory create() {
        return new GenericTextLineConverterFactory();
    }

    @Override
    public TextLineConverter converter(Class<? extends TextLine> type) {
        if (isEqualsType(type, GenericTextLine.class)) {
            return new GenericTextLineConverter();
        }
        return null;
    }
}
