package com.github.naturs.text.recorder;

import com.github.naturs.text.recorder.internal.Utils;
import org.junit.Test;

/**
 *
 * Created by naturs on 2017/6/27.
 */
public class UtilsTest {

    @Test
    public void test_getStackTrace() {
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            System.out.println(Utils.getStackTrace(e));
        }
    }

    @Test
    public void test_getMethodInfo() {
        StackTraceElement element = Utils.getStackTraceElement(Utils.class);
        System.out.println(Utils.getMethodInfo(element));

        element = new StackTraceElement(
                Utils.class.getName(),
                "methodA",
                "Utils.java",
                -2
        );

        System.out.println(Utils.getMethodInfo(element));
    }

    @Test
    public void test_formatJson() {
        String json = "{\"name\":\"abc\",\"age\":18,\"other\":{\"other1\":\"otherValue1\",\"other2\":otherValue2}}";
        System.out.println(Utils.formatJson(json, 0));
        System.out.println(Utils.formatJson(json, 2));
        System.out.println(Utils.formatJson(json, 4));
        System.out.println(Utils.formatJson(json, 8));
    }

    @Test
    public void test_formatXml() {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<resources>" +
                "<item name=\"k1\" messageType=\"a\">value1</item>" +
                "<item name=\"k2\" messageType=\"b\">value2</item>" +
                "<integer name=\"k3\">5</integer>" +
                "</resources>";

        System.out.println(Utils.formatXml(xml, 0));
        System.out.println(Utils.formatXml(xml, 2));
        System.out.println(Utils.formatXml(xml, 4));
        System.out.println(Utils.formatXml(xml, 8));
    }
}
