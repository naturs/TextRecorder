package com.github.naturs.text.recorder.converter;

import com.github.naturs.text.recorder.GenericTextLine;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * Created by naturs on 2017/6/27.
 */
public class GenericTextLineConverterTest {

    GenericTextLineConverter mGenericTextLineConverter;

    @Before
    public void setUp() {
        mGenericTextLineConverter = new GenericTextLineConverter();
    }

    @Test
    public void test_DefaultLogLineConverter_convert_normal() {
        GenericTextLine line = new GenericTextLine(GenericTextLine.TYPE_MESSAGE_NORMAL, false, "This is a log.", null, null,
                null, GenericTextLineConverterTest.class);
        String rawLine = mGenericTextLineConverter.convert(null, line);
        System.out.println(rawLine);
    }

    @Test
    public void test_DefaultLogLineConverter_convert_throwable() {
        RuntimeException exception = new RuntimeException("has error");
        GenericTextLine line = new GenericTextLine(GenericTextLine.TYPE_MESSAGE_NORMAL, false,
                "This is a throwable log.", null, exception, null, GenericTextLineConverterTest.class);
        String rawLine = mGenericTextLineConverter.convert(null, line);
        System.out.println(rawLine);
    }

    @Test
    public void test_DefaultLogLineConverter_convert_json() {
        String json = "{\"name\":\"abc\",\"age\":18,\"other\":{\"other1\":\"otherValue1\",\"other2\":otherValue2}}";
        GenericTextLine line = new GenericTextLine(GenericTextLine.TYPE_MESSAGE_JSON, false, "json结果：", json, null,
                null, GenericTextLineConverterTest.class);
        String rawLine = mGenericTextLineConverter.convert(null, line);
        System.out.println(rawLine);
    }

    @Test
    public void test_DefaultLogLineConverter_convert_xml() {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<resources>" +
                "<item name=\"k1\" messageType=\"a\">value1</item>" +
                "<item name=\"k2\" messageType=\"b\">value2</item>" +
                "<integer name=\"k3\">5</integer>" +
                "</resources>";
        GenericTextLine line = new GenericTextLine(GenericTextLine.TYPE_MESSAGE_XML, false, "xml结果：", xml, null,
                null, GenericTextLineConverterTest.class);
        String rawLine = mGenericTextLineConverter.convert(null, line);
        System.out.println(rawLine);
    }

}
