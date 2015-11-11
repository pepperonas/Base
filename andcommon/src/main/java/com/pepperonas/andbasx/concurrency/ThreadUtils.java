package com.pepperonas.andbasx.concurrency;

import android.os.Handler;

import java.util.concurrent.Callable;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class ThreadUtils {

    private static final String TAG = "ThreadUtils";


    public static Void runDelayed(int delay, final Callable<Void> func) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    func.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, delay);
        return null;
    }

}
