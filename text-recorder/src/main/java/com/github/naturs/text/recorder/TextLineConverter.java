package com.github.naturs.text.recorder;

import com.github.naturs.text.recorder.internal.Utils;

/**
 * 将{@link TextLine}转换为字符串的转换器
 *
 * Created by naturs on 2017/6/27.
 */
public interface TextLineConverter {

    /**
     * 转换为字符串
     * @param actualType 参数line的具体类型
     * @param line 记录
     * @return 转换后的结果
     */
    String convert(Class<? extends TextLine> actualType, TextLine line);

    /**
     * {@link TextLineConverter}的工厂类
     */
    abstract class Factory {
        /**
         * 根据某种类型的{@link TextLine}来获取对应的{@link TextLineConverter}
         * @param type {@link TextLine}的具体类型
         * @return 返回匹配type的Converter，可返回null，代表该工厂不处理该type类型的TextLine
         */
        public abstract TextLineConverter converter(Class<? extends TextLine> type);

        /**
         * expectedType是否为type本身，或者是否为type的父类
         */
        protected static boolean isAssignableFrom(Class<? extends TextLine> type,
                                                  Class<? extends TextLine> expectedType) {
            return expectedType.isAssignableFrom(type);
        }

        /**
         * 类型是否完全一样
         */
        protected static boolean isEqualsType(Class<? extends TextLine> type,
                                              Class<? extends TextLine> expectedType) {
            return expectedType == type;
        }

        protected static StackTraceElement getInvokeStackTrace() {
            return Utils.getStackTraceElement(TextRecorder.class);
        }

        protected static String getReadableXml(String rawXml, int indentFactor) {
            return Utils.formatXml(rawXml, indentFactor);
        }

        protected static String getReadableJson(String rawJson, int indentFactor) {
            return Utils.formatJson(rawJson, indentFactor);
        }

        protected String getMethodInfo(StackTraceElement element) {
            return Utils.getMethodInfo(element);
        }

    }

    abstract class AbsTextLineConverter implements TextLineConverter {
        protected static StackTraceElement getInvokeStackTrace() {
            return Utils.getStackTraceElement(TextRecorder.class);
        }

        protected static String getReadableXml(String rawXml, int indentFactor) {
            return Utils.formatXml(rawXml, indentFactor);
        }

        protected static String getReadableJson(String rawJson, int indentFactor) {
            return Utils.formatJson(rawJson, indentFactor);
        }

        protected static String getMethodInfo(StackTraceElement element) {
            return Utils.getMethodInfo(element);
        }

        protected static String getStackTrace(Throwable t) {
            return Utils.getStackTrace(t).trim();
        }

    }
}
