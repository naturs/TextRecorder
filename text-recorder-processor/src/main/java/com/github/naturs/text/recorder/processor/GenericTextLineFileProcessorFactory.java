package com.github.naturs.text.recorder.processor;

import com.github.naturs.text.recorder.GenericTextLine;
import com.github.naturs.text.recorder.TextLine;
import com.github.naturs.text.recorder.TextLineProcessor;

/**
 * 处理{@link GenericTextLine}的工厂，会将{@link GenericTextLine}内容保存到本地文件
 *
 * Created by naturs on 2017/6/28.
 */
public class GenericTextLineFileProcessorFactory extends TextLineProcessor.Factory {

    public static GenericTextLineFileProcessorFactory create(String rootDirPath) {
        return create(rootDirPath, 10 * 1024 * 1024L, 7, 0.5f, "txt");
    }

    /**
     *
     * @param rootDirPath 防止文件的根目录
     * @param maxFileSize 每个文件的最大大小
     * @param maxFileCount 每个文件夹下面最大文件数量
     * @param fileSplitRatio 单文件超出大小后的切割比例，从后往前切割，取值范围[0, 1]，
     *                       比如fileSplitRatio=0.8f，则保留文件后面80%的内容，即舍弃文件开头20%的内容。
     */
    public static GenericTextLineFileProcessorFactory create(String rootDirPath,
                                                             long maxFileSize,
                                                             int maxFileCount,
                                                             float fileSplitRatio,
                                                             String fileSuffix) {
        return new GenericTextLineFileProcessorFactory(rootDirPath, maxFileSize, maxFileCount, fileSplitRatio, fileSuffix);
    }

    private final String rootDirPath;
    private final long maxFileSize;
    private final int maxFileCount;
    private final float fileSplitRatio;
    private final String fileSuffix;

    private GenericTextLineFileProcessorFactory(String rootDirPath,
                                                long maxFileSize,
                                                int maxFileCount,
                                                float fileSplitRatio,
                                                String fileSuffix) {
        this.rootDirPath = rootDirPath;
        this.maxFileSize = maxFileSize;
        this.maxFileCount = maxFileCount;
        this.fileSplitRatio = fileSplitRatio;
        this.fileSuffix = fileSuffix;
    }

    @Override
    public TextLineProcessor processor(Class<? extends TextLine> type) {
        if (isAssignableFrom(type, GenericTextLine.class)) {
            return new GenericTextLineFileProcessor(
                    rootDirPath, maxFileSize, maxFileCount, fileSplitRatio, fileSuffix
            );
        }
        return null;
    }

}
