package com.github.naturs.text.recorder;

import java.util.List;

/**
 * 处理{@link TextLine}的处理器
 *
 * Created by naturs on 2017/6/26.
 */
public interface TextLineProcessor {

    void process(Class<? extends TextLine> actualType, List<TextLine> lines) throws Exception;

    /**
     * {@link TextLineProcessor}的工厂类
     */
    abstract class Factory {
        /**
         * 获取能处理某种类型的{@link TextLine}的{@link TextLineProcessor}
         * @param type {@link TextLine}的具体类型
         * @return 如果类型匹配，则返回对应的Processor，否则返回null
         */
        public abstract TextLineProcessor processor(Class<? extends TextLine> type);

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
    }

}
