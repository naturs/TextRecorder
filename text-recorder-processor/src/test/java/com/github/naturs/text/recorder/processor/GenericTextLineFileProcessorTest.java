package com.github.naturs.text.recorder.processor;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 *
 * Created by naturs on 2017/6/30.
 */
public class GenericTextLineFileProcessorTest {

    @Test
    public void setup() throws Exception {
        File file = new File("./../temp", "GenericTextLineFileProcessorTest.txt");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
        for (long i = 0; ; i++) {
            bw.write(String.valueOf(i));
            bw.newLine();
            if (file.length() > 2 * 1024 * 1024) {
                break;
            }
        }
        bw.flush();
        bw.close();

        System.out.println("GenericTextLineFileProcessorTest setup file.length: " + file.length());
    }

    @Test
    public void test_trimFileSize() {
        File file = new File("./../temp", "GenericTextLineFileProcessorTest.txt");
        GenericTextLineFileProcessor.trimFileSize(file, 2 * 1024 * 1024, 0.2f);
        System.out.println("GenericTextLineFileProcessorTest trimFileSize file.length: " + file.length());
    }

}
