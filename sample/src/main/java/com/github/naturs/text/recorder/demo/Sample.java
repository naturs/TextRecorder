package com.github.naturs.text.recorder.demo;

import com.github.naturs.text.recorder.LogPrinter;
import com.github.naturs.text.recorder.Schedulers;
import com.github.naturs.text.recorder.TextRecorder;
import com.github.naturs.text.recorder.converter.GenericTextLineConverterFactory;
import com.github.naturs.text.recorder.processor.GenericTextLineFileProcessorFactory;

import java.util.Random;

/**
 *
 * Created by naturs on 2017/6/29.
 */
public class Sample {

    static final String JSON = "{\"name\":\"abc\",\"age\":18,\"other\":{\"other1\":\"otherValue1\",\"other2\":otherValue2}}";

    static final String XML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
            "<resources>" +
            "<item name=\"k1\" messageType=\"a\">value1</item>" +
            "<item name=\"k2\" messageType=\"b\">value2</item>" +
            "<integer name=\"k3\">5</integer>" +
            "</resources>";

    public static void main(String[] args) {
        LogPrinter printer = new LogPrinter() {
            @Override
            public boolean isLoggable(String tag) {
                return true;
            }

            @Override
            public void print(String tag, String message) {
                System.out.println(tag + "->" + message);
            }

            @Override
            public void print(String tag, Throwable t) {
                t.printStackTrace();
            }
        };
        TextRecorder.init(Schedulers.io(),
                GenericTextLineConverterFactory.create(),
                GenericTextLineFileProcessorFactory.create("temp", 500 * 1024, 5, 0.2f, "log"),
                printer
        );

        final TextRecorder recorder = TextRecorder.with("module");

        for (;;) {
            Random random = new Random();
            int i = random.nextInt(5);
            recorder.append(Thread.currentThread() + "");
            switch (i) {
                case 0:
                    recorder.appendBlankLine();
                    break;

                case 1: // XML
                    recorder.appendXml("This is a xml -> " + System.currentTimeMillis(), XML);
                    break;

                case 2: // JSON
                    recorder.appendJson("This is a json -> " + System.currentTimeMillis(), JSON);
                    break;

                case 3: // Throwable
                    RuntimeException e3 = new RuntimeException("Test record throwable.");
                    recorder.append("This is an exception -> " + System.currentTimeMillis(), e3);
                    break;

                case 4:
                    recorder.appendDivider();
                    break;

                case 5:
                    recorder.append(true, "This is a raw message -> " + System.currentTimeMillis());
                    break;

                case 6:
                    RuntimeException e6 = new RuntimeException("Test record throwable for rawMessage.");
                    recorder.append(true, "This is a raw throwable -> " + System.currentTimeMillis(), e6);
                    break;
            }

            recorder.appendBlankLine();
            recorder.apply();

            sleep(random.nextInt(50));
        }
    }

    static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
