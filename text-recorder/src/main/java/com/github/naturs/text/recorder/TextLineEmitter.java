package com.github.naturs.text.recorder;

/**
 *
 * Created by naturs on 2017/6/26.
 */
interface TextLineEmitter {

    void submit(TextLineBundle bundle);

    void emit(TextLineBundle bundle);

}
