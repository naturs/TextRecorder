package com.github.naturs.text.recorder.converter;

import com.github.naturs.text.recorder.MarkdownTextLine;
import org.junit.Test;

/**
 *
 * Created by naturs on 2017/7/9.
 */
public class MarkdownTextLineConverterTest {

    private static final String JSON =
            "{\"name\":\"abc\",\"age\":18,\"other\":{\"other1\":\"otherValue1\",\"other2\":otherValue2}}";

    private static final String XML =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
            "<resources>" +
                "<item name=\"k1\" messageType=\"a\">value1</item>" +
                "<item name=\"k2\" messageType=\"b\">value2</item>" +
                "<integer name=\"k3\">5</integer>" +
            "</resources>";

    private static final String LONG_TEXT;

    static {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 100; i ++) {
            builder.append("I'm a text ").append(i).append(";");
        }
        LONG_TEXT = builder.toString();
    }

    @Test
    public void testConvert() {
        MarkdownTextLine textLine1 = MarkdownTextLine.with("markdown").text(LONG_TEXT);
        MarkdownTextLine textLine2 = MarkdownTextLine.with("markdown").text(true, "It's a text.");
        MarkdownTextLine textLine3 = MarkdownTextLine.with("markdown").json("It's a json.", JSON);
        MarkdownTextLine textLine4 = MarkdownTextLine.with("markdown").xml("It's a xml.", XML);
        MarkdownTextLine textLine5 = MarkdownTextLine.with("markdown").throwable(
                "It's an exception.",
                new RuntimeException("I'm an exception.")
        );
        MarkdownTextLine textLine6 = MarkdownTextLine.with("markdown").blankLine();
        MarkdownTextLine textLine7 = MarkdownTextLine.with("markdown").divider();

        MarkdownTextLineConverter converter = new MarkdownTextLineConverter();

        print(converter.convert(MarkdownTextLine.class, textLine1));
        print(converter.convert(MarkdownTextLine.class, textLine2));

        print(converter.convert(MarkdownTextLine.class, textLine7));

        print(converter.convert(MarkdownTextLine.class, textLine3));
        print(converter.convert(MarkdownTextLine.class, textLine4));

        print(converter.convert(MarkdownTextLine.class, textLine7));

        print(converter.convert(MarkdownTextLine.class, textLine5));
        print(converter.convert(MarkdownTextLine.class, textLine6));

    }

    private void print(String s) {
        System.out.print(s);
    }
}
