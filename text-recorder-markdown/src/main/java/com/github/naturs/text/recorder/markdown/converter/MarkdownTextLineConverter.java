package com.github.naturs.text.recorder.markdown.converter;

import com.github.naturs.text.recorder.GenericTextLine;
import com.github.naturs.text.recorder.TextLine;
import com.github.naturs.text.recorder.TextLineConverter;
import com.github.naturs.text.recorder.internal.Utils;
import com.github.naturs.text.recorder.markdown.MarkdownTextLine;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * {@link MarkdownTextLine MarkdownTextLine}的转换方式
 * <p>
 * Created by naturs on 2017/7/8.
 */
public class MarkdownTextLineConverter implements TextLineConverter {

    private static final String CRLF = "\n";
    private static final String SPACE = "  ";
    private static final int INDENT = 4;
    private static final String BLANK_LINE = "\n";
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    private static final char QUOTE_CHAR = '`';
    private static final String QUOTE_REGION_CHAR = "```";
    private static final String QUOTE_REGION_CHAR_XML = QUOTE_REGION_CHAR + "xml";
    private static final String QUOTE_REGION_CHAR_JSON = QUOTE_REGION_CHAR + "json";
    private static final String QUOTE_TAB = "\t";
    private static final String DIVIDER = "---";

    private void appendSpace(StringBuilder builder, GenericTextLine line) {
        if (builder.length() > 0) {
            builder.append(SPACE);
        }
    }

    private void appendCRLF(StringBuilder builder, int count) {
        for (int i = 0; i < count; i++) {
            builder.append(CRLF);
        }
    }

    protected void appendTimeAndMethodInfo(StringBuilder builder, MarkdownTextLine line) {
        if (line.isRawMessage()) {
            return;
        }
        boolean hasTime = line.getTimestamp() > 0;
        boolean hasMethod = line.getStackTrace() != null;
        boolean hasTimeOrMethod = hasTime || hasMethod;

        if (hasTimeOrMethod) {
            builder.append(QUOTE_CHAR);
        }

        if (hasTime) {
            builder.append(TIME_FORMAT.format(new Date(line.getTimestamp())));
        }

        if (hasMethod) {
            appendSpace(builder, line);
            builder.append(Formatter.getMethodInfo(line.getStackTrace()));
        }

        if (hasTimeOrMethod) {
            builder.append(QUOTE_CHAR);
        }
    }

    protected void appendMessage(StringBuilder builder, MarkdownTextLine line) {
        if (line.getMessage() != null) {
            if (builder.length() > 0) {
                appendCRLF(builder, 2);
            }
            builder.append(QUOTE_TAB).append(line.getMessage());
        }
        if (line.getExtraMessage() != null) {
            if (builder.length() > 0) {
                appendCRLF(builder, 2);
            }
            switch (line.getMessageType()) {
                case GenericTextLine.TYPE_MESSAGE_XML:
                    builder.append(QUOTE_REGION_CHAR_XML);
                    appendCRLF(builder, 1);
                    builder.append(Formatter.formatXml(line.getExtraMessage(), INDENT));
                    appendCRLF(builder, 1);
                    builder.append(QUOTE_REGION_CHAR);
                    break;

                case GenericTextLine.TYPE_MESSAGE_JSON:
                    builder.append(QUOTE_REGION_CHAR_JSON);
                    appendCRLF(builder, 1);
                    builder.append(Formatter.formatJson(line.getExtraMessage(), INDENT));
                    appendCRLF(builder, 1);
                    builder.append(QUOTE_REGION_CHAR);
                    break;

                default:
                    builder.append(QUOTE_TAB).append(line.getExtraMessage());
                    break;
            }
        }

        switch (line.getMessageType()) {
            case GenericTextLine.TYPE_DIVIDER:
                builder.append(DIVIDER);
                break;

            case GenericTextLine.TYPE_BLANK_LINE:
                builder.append(BLANK_LINE);
                break;
        }
    }

    protected void appendThrowable(StringBuilder builder, GenericTextLine line) {
        if (line.getThrowable() != null) {
            appendCRLF(builder, 2);
            builder.append(QUOTE_TAB);
            builder.append(Utils.getStackTrace(line.getThrowable()));
        }
    }

    @Override
    public String convert(Class<? extends TextLine> actualType, TextLine line) {
        if (line instanceof MarkdownTextLine) {
            MarkdownTextLine textLine = (MarkdownTextLine) line;
            StringBuilder builder = new StringBuilder();

            appendTimeAndMethodInfo(builder, textLine);

            appendMessage(builder, textLine);

            appendThrowable(builder, textLine);

            appendCRLF(builder, 2);

            return builder.toString();
        }
        return line.toString();
    }
}
