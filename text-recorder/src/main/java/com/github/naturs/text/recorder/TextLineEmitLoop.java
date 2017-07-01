package com.github.naturs.text.recorder;

import java.util.ArrayList;

/**
 *
 * Created by naturs on 2017/6/26.
 */
class TextLineEmitLoop implements TextLineEmitter {

    private static final String TAG = "TextLineEmitLoop";

    private final RecordManager manager;
    private boolean emitting;
    private ArrayList<TextLineBundle> queue;

    TextLineEmitLoop(RecordManager manager) {
        this.manager = manager;
    }

    @Override
    public void submit(final TextLineBundle bundle) {
        manager.schedule(new Runnable() {
            @Override
            public void run() {
                emitCatchException(bundle);
            }
        });
    }

    @Override
    public void emit(TextLineBundle bundle) {
        emitCatchException(bundle);
    }

    private void emitCatchException(TextLineBundle bundle) {
        try {
            emitInner(bundle);
        } catch (Exception e) {
            if (TextLineLogPrinter.isLoggable(TAG)) {
                TextLineLogPrinter.print(TAG, e);
            }
        }
    }

    private void emitInner(TextLineBundle bundle) throws Exception {
        if (bundle == null || bundle.isEmpty()) {
            return;
        }

        synchronized (this) {
            if (emitting) {
                ArrayList<TextLineBundle> q = queue;
                if (q == null) {
                    q = new ArrayList<TextLineBundle>();
                    queue = q;
                }
                q.add(bundle);
                return;
            }
            emitting = true;
        }

        boolean skipFinal = false;

        try {
            processLogLine(bundle);

            for(;;) {
                ArrayList<TextLineBundle> q;
                synchronized (this) {
                    q = queue;
                    if (q == null) {
                        emitting = false;
                        skipFinal = true;
                        return;
                    }
                    queue = null;
                }
                processLogLines(q);
            }
        } finally {
            if (!skipFinal) {
                synchronized (this) {
                    emitting = false;
                }
            }
        }
    }

    private void processLogLine(TextLineBundle bundle) throws Exception {
//        Class<? extends TextLine> clazz = null;
//        for (TextLine line : bundle.lines()) {
//            Class<? extends TextLine> temp = line.getClass();
//            if (clazz == null) {
//                clazz = temp;
//            } else if (clazz.equals(temp)) {
//
//            } else {
//
//            }
//        }
        manager.process(bundle);
    }

    private void processLogLines(ArrayList<TextLineBundle> list) throws Exception {
        if (list == null || list.isEmpty()) {
            return;
        }

        for (TextLineBundle bundle : list) {
            processLogLine(bundle);
        }
    }

}
