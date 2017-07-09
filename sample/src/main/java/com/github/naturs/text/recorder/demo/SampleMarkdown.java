package com.github.naturs.text.recorder.demo;

import com.github.naturs.text.recorder.MarkdownTextLine;
import com.github.naturs.text.recorder.Schedulers;
import com.github.naturs.text.recorder.TextRecorder;
import com.github.naturs.text.recorder.converter.MarkdownTextLineConverterFactory;
import com.github.naturs.text.recorder.processor.GenericTextLineFileProcessorFactory;

/**
 *
 * Created by naturs on 2017/7/9.
 */
public class SampleMarkdown {

    public static void main(String[] args) {
        TextRecorder.init(Schedulers.io(),
                MarkdownTextLineConverterFactory.create(),
                GenericTextLineFileProcessorFactory.create("temp", 500 * 1024, 5, 0.2f, "md"),
                null
        );

        final TextRecorder recorder = TextRecorder.with("markdown");

        MarkdownTextLine textLine = MarkdownTextLine.with().text("I'm a text.");
        recorder.append(textLine);

        RuntimeException exception = new RuntimeException("mock an exception.");
        textLine = MarkdownTextLine.with().throwable("I'm an exception.", exception);
        recorder.append(textLine);

        textLine = MarkdownTextLine.with().divider();
        recorder.append(textLine);

        textLine = MarkdownTextLine.with().json("I'm a json.", Sample.JSON);
        recorder.append(textLine);

        textLine = MarkdownTextLine.with().xml("I'm a xml.", Sample.XML);
        recorder.append(textLine);

        recorder.apply();
    }

}
