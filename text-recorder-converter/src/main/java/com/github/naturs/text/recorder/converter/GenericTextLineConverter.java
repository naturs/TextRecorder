package com.github.naturs.text.recorder.converter;

import com.github.naturs.text.recorder.GenericTextLine;
import com.github.naturs.text.recorder.TextLine;
import com.github.naturs.text.recorder.TextLineConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * {@link GenericTextLine}的默认转换方式
 * Created by naturs on 2017/6/28.
 */
public class GenericTextLineConverter extends TextLineConverter.AbsTextLineConverter {

    private static final String CRLF = "\n";
    private static final String SPACE = "  ";
    private static final int INDENT = 4;
    private static final String EXCEPTION_START =
            "----------------------------EXCEPTION START----------------------------";
    private static final String EXCEPTION_END =
            "----------------------------EXCEPTION END----------------------------";
    private static final String DIVIDER =
            "----------------------------------------------------------------------";
    private static final String BLANK_LINE = "\n";
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    protected void appendSpace(StringBuilder builder, GenericTextLine line) {
        if (builder.length() > 0) {
            builder.append(SPACE);
        }
    }

    protected void appendTime(StringBuilder builder, GenericTextLine line) {
        if (line.isRawMessage()) {
            return;
        }

        if (line.getTimestamp() > 0) {
            appendSpace(builder, line);
            builder.append(TIME_FORMAT.format(new Date(line.getTimestamp())));
        }
    }

    protected void appendMethodInfo(StringBuilder builder, GenericTextLine line) {
        if (line.isRawMessage()) {
            return;
        }

        if (line.getStackTrace() != null) {
            appendSpace(builder, line);
            builder.append(getMethodInfo(line.getStackTrace()));
        }
    }

    protected void appendMessage(StringBuilder builder, GenericTextLine line) {
        if (line.getMessage() != null) {
            appendSpace(builder, line);
            builder.append(line.getMessage());
        }
        if (line.getExtraMessage() != null) {
            switch (line.getMessageType()) {
                case GenericTextLine.TYPE_MESSAGE_XML:
                    builder.append(CRLF);
                    builder.append(getReadableXml(line.getExtraMessage(), INDENT));
                    break;

                case GenericTextLine.TYPE_MESSAGE_JSON:
                    builder.append(CRLF);
                    builder.append(getReadableJson(line.getExtraMessage(), INDENT));
                    break;

                case GenericTextLine.TYPE_DIVIDER:
                    builder.append(CRLF).append(DIVIDER);
                    break;

                case GenericTextLine.TYPE_BLANK_LINE:
                    builder.append(CRLF).append(BLANK_LINE);
                    break;
            }
        }
    }

    protected void appendThrowable(StringBuilder builder, GenericTextLine line) {
        if (line.getThrowable() != null) {
            builder.append(CRLF);
            builder.append(EXCEPTION_START);
            builder.append(CRLF);
            builder.append(getStackTrace(line.getThrowable()));
            builder.append(CRLF);
            builder.append(EXCEPTION_END);
        }
    }

    @Override
    public String convert(Class<? extends TextLine> actualType, TextLine line) {
        if (line instanceof GenericTextLine) {
            GenericTextLine textLine = (GenericTextLine) line;
            StringBuilder builder = new StringBuilder();

            appendTime(builder, textLine);

            appendMethodInfo(builder, textLine);

            appendMessage(builder, textLine);

            appendThrowable(builder, textLine);

            if (builder.length() > 0 && !CRLF.equals(builder.toString())) {
                builder.append(CRLF);
            }

            return builder.toString();
        }
        return line.toString();
    }

}
