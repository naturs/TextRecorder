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
}
