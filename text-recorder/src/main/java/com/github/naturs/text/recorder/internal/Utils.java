package com.github.naturs.text.recorder.internal;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

/**
 * 自带的工具类
 * Created by naturs on 2017/6/27.
 */
public class Utils {
    private static final String SUFFIX = ".java";

    /**
     * 将异常堆栈转换成字符串
     */
    public static String getStackTrace(Throwable t) {
        if (t == null) {
            return null;
        }
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    public static StackTraceElement getStackTraceElement(Class<?> invokeClass) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        StackTraceElement targetElement = null;

        boolean shouldTrace = false;
        for (StackTraceElement element : stackTrace) {
            boolean isInvokeMethod = element.getClassName().equals(invokeClass.getName());
            if (shouldTrace && !isInvokeMethod) {
                targetElement = element;
                break;
            }
            shouldTrace = isInvokeMethod;
        }
        return targetElement;
    }

    public static String getMethodInfo(StackTraceElement element) {
        String fileName;
        if (element.getFileName() != null) {
            fileName = element.getFileName();

        } else {
            String className = element.getClassName();

            String[] classNameInfo = className.split("\\.");
            if (classNameInfo.length > 0) {
                className = classNameInfo[classNameInfo.length - 1] + SUFFIX;
            }

            if (className.contains("$")) {
                className = className.split("\\$")[0] + SUFFIX;
            }

            fileName = className;
        }
        return "[(" + fileName + ":" + element.getLineNumber() + ")#" + element.getMethodName() + "]";
    }

    /**
     * 将json格式化为方便阅读的格式
     * @param rawJson 原始json数据
     * @param indentFactor 缩进的空格数
     */
    public static String formatJson(String rawJson, int indentFactor) {
        String message;
        try {
            if (rawJson.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(rawJson);
                message = jsonObject.toString(indentFactor);
            } else if (rawJson.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(rawJson);
                message = jsonArray.toString(indentFactor);
            } else {
                message = rawJson;
            }
        } catch (Exception e) {
            message = rawJson;
        }

        return message;
    }

    /**
     * 将xml格式化为方便阅读的格式
     * @param rawXml 原始xml数据
     * @param indentFactor 缩进的空格数
     */
    public static String formatXml(String rawXml, int indentFactor) {
        try {
            Source xmlInput = new StreamSource(new StringReader(rawXml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount",
                    String.valueOf(indentFactor));
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString().replaceFirst(">", ">\n");
        } catch (Exception e) {
            return rawXml;
        }
    }

    public static void flushQuietly(Flushable flushable) {
        if (flushable != null) {
            try {
                flushable.flush();
            } catch (Exception ignore) {
            }
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignore) {
            }
        }
    }
}
