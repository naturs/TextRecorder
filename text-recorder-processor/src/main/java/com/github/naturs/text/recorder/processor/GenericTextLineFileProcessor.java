package com.github.naturs.text.recorder.processor;

import com.github.naturs.text.recorder.GenericTextLine;
import com.github.naturs.text.recorder.TextLine;
import com.github.naturs.text.recorder.TextLineLogPrinter;
import com.github.naturs.text.recorder.TextLineProcessor;
import com.github.naturs.text.recorder.internal.Utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 能将{@link GenericTextLine}保存到本地的处理器。
 * 该类会使用{@link GenericTextLine#getTag()}作为目录，日志内容按天保存。
 * <p>
 * 规则：
 *
 * <li>每个文件有最大大小限制，超出大小后按fileSplitRatio的值切割文件，保留文件后面的内容</li>
 * <li>每个文件夹有文件数量限制，超出数量后按文件名排序，删除最前面的文件</li>
 *
 * <p/>
 *
 * Created by naturs on 2017/6/28.
 */
public class GenericTextLineFileProcessor implements TextLineProcessor {
    private static final String TAG = "GenericTextLineFileProcessor";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    protected final String rootDirPath;
    protected final long maxFileSize;
    protected final int maxFileCount;
    protected final float fileSplitRatio;
    protected final String fileSuffix;

    /**
     *
     * @param rootDirPath 防止文件的根目录
     * @param maxFileSize 每个文件的最大大小
     * @param maxFileCount 每个文件夹下面最大文件数量
     * @param fileSplitRatio 单文件超出大小后的切割比例，从后往前切割，取值范围[0, 1]，
     *                       比如fileSplitRatio=0.8f，则保留文件后面80%的内容，即舍弃文件开头20%的内容。
     * @param fileSuffix 文件后缀名
     */
    public GenericTextLineFileProcessor(String rootDirPath,
                                        long maxFileSize,
                                        int maxFileCount,
                                        float fileSplitRatio,
                                        String fileSuffix) {
        this.rootDirPath = rootDirPath;
        this.maxFileSize = maxFileSize;
        this.maxFileCount = maxFileCount;
        this.fileSplitRatio = Math.max(0, Math.min(1.0f, fileSplitRatio));
        this.fileSuffix = fileSuffix;
    }

    @Override
    public void process(Class<? extends TextLine> actualType, List<TextLine> lines) throws Exception {

        OutputStreamWriter outputStreamWriter = null;
        String prevFileName = null;

        // 记录下lines中所包含的所有文件夹
        Set<String> allDirs = new HashSet<String>();

        try {
            for (TextLine line : lines) {
                File fileDir = getFileDir(line.getTag());
                boolean success = fileDir.exists() || fileDir.mkdirs();

                if (!success) {
                    throw new IllegalStateException("Create file directory failed: " + fileDir.getAbsolutePath());
                }

                allDirs.add(fileDir.getAbsolutePath());

                // 当前Processor只处理已经转换了的message，所以要配合TextLineConverter使用
                final String convertedMessage = line.getConvertedMessage();
                if (convertedMessage == null) {
                    continue;
                }

                // 根据时间生成文件名
                String fileName = getFileName(line);

                // 第一个LogLine，或者后面的LogLine对应的文件和之前的LogLine对应的文件不一致，
                // 就关闭之前的文件流，并且创建新的文件流
                if (prevFileName == null || !prevFileName.equals(fileName)) {
                    // 关闭之前的文件流
                    Utils.flushQuietly(outputStreamWriter);
                    Utils.closeQuietly(outputStreamWriter);

                    File file = new File(fileDir, fileName);
                    prevFileName = fileName;
                    trimFileSize(file, maxFileSize, fileSplitRatio);

                    OutputStream outputStream = new FileOutputStream(file, true);
                    outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                }
                outputStreamWriter.write(convertedMessage);
                outputStreamWriter.flush();
            }
        } finally {
            Utils.flushQuietly(outputStreamWriter);
            Utils.closeQuietly(outputStreamWriter);
            trimDirCount(allDirs);
        }
    }

    protected File getFileDir(Object tag) {
        if (tag == null || tag.toString().isEmpty() || "".equals(tag.toString().trim())) {
            return new File(rootDirPath);
        }
        return new File(rootDirPath, tag.toString().trim());
    }

    protected String getFileName(TextLine line) {
        return DATE_FORMAT.format(new Date(line.getTimestamp()))
                + "." + (fileSuffix == null ? "txt" : fileSuffix);
    }

    /**
     * 将目录中的文件数删除到指定数量
     */
    protected void trimDirCount(Set<String> fileDirPaths) {
        if (maxFileCount < 0 || fileDirPaths == null || fileDirPaths.isEmpty()) {
            return;
        }
        for (String fileDirPath : fileDirPaths) {
            File fileDir = new File(fileDirPath);
            if (fileDir.exists() && fileDir.isDirectory()) {
                File[] files = fileDir.listFiles();
                if (files != null) {
                    int count = files.length;
                    if (count > maxFileCount) {

                        if (TextLineLogPrinter.isLoggable(TAG)) {
                            TextLineLogPrinter.print(TAG, "文件夹 " + fileDir.getAbsolutePath() + " 中目前有 " +
                                count + " 个文件，最大允许 " + maxFileCount + " 个文件，开始删除多余文件...");
                        }

                        List<File> fileList = new ArrayList<File>(count);
                        fileList.addAll(Arrays.asList(files));
                        // 按文件名的自然顺序排序
                        Collections.sort(fileList, new Comparator<File>() {
                            @Override
                            public int compare(File o1, File o2) {
                                return o1.getName().compareTo(o2.getName());
                            }
                        });

                        int needDeleteCount = count - maxFileCount;
                        int deletedCount = 0;
                        for (int i = 0; i < count; i++) {
                            File file = fileList.get(i);
                            if (deletedCount < needDeleteCount) {
                                boolean success = file.delete();

                                if (TextLineLogPrinter.isLoggable(TAG)) {
                                    TextLineLogPrinter.print(TAG, "删除文件 " + file.getName()
                                            + " " + (success ? "成功" : "失败"));
                                }

                                if (success) {
                                    deletedCount++;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 删除过大文件的部分内容
     */
    protected static void trimFileSize(File file, long maxAllowFileSize, float remainRatio) {
        if (maxAllowFileSize < 0) {
            return;
        }
        if (!file.exists() || !file.isFile() || file.length() <= maxAllowFileSize) {
            return;
        }

        if (TextLineLogPrinter.isLoggable(TAG)) {
            TextLineLogPrinter.print(TAG, "文件 " + file.getAbsolutePath()
                    + "（length=" + file.length() + "）超出限制大小（maxAllowFileSize="
                    + maxAllowFileSize +", fileSplitRatio="
                    + remainRatio + "），开始切割文件...");
        }

        RandomAccessFile originAccessFile = null;
        FileOutputStream tempFileOutputStream = null;
        File tempFile = null;
        boolean splitSuccess = false;

        try {
            tempFile = new File(file.getParentFile(), file.getName() + ".tmp");

            if (TextLineLogPrinter.isLoggable(TAG)) {
                TextLineLogPrinter.print(TAG, "临时文件为：" + tempFile.getAbsolutePath());
            }

            originAccessFile = new RandomAccessFile(file, "rw");
            tempFileOutputStream = new FileOutputStream(tempFile, false);

            long fileTotalSize = originAccessFile.length();
            long seekPoints = (long) (fileTotalSize * (1.0f - remainRatio));

            originAccessFile.seek(seekPoints);

            byte [] buffer = new byte[1024];
            int read;
            // 读取插入点后的内容
            while((read = originAccessFile.read(buffer)) > 0) {
                // 将读取的数据写入临时文件中
                tempFileOutputStream.write(buffer, 0, read);
            }
            splitSuccess = true;
        } catch (Exception e) {
            if (TextLineLogPrinter.isLoggable(TAG)) {
                TextLineLogPrinter.print(TAG, e);
            }
        } finally {
            Utils.flushQuietly(tempFileOutputStream);
            Utils.closeQuietly(tempFileOutputStream);
            Utils.closeQuietly(originAccessFile);
        }

        if (splitSuccess) {

            if (TextLineLogPrinter.isLoggable(TAG)) {
                TextLineLogPrinter.print(TAG, "旧文件 " + file.getAbsolutePath() + " 分割成功，"
                        + " 新文件大小：" + tempFile.length());
            }

            boolean deleteOldFileSuccess = file.delete();

            if (TextLineLogPrinter.isLoggable(TAG)) {
                TextLineLogPrinter.print(TAG, "旧文件 " + file.getAbsolutePath()
                        + " 删除" + (deleteOldFileSuccess ? "成功" : "失败"));
            }

            if (deleteOldFileSuccess) {
                boolean renameSuccess = tempFile.renameTo(file);

                if (TextLineLogPrinter.isLoggable(TAG)) {
                    TextLineLogPrinter.print(TAG, "临时文件重命名为：" + file.getAbsolutePath()
                            + " " + (renameSuccess ? "成功" : "失败"));
                }
            }
        }
    }
}
