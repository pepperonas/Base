package com.pepperonas.andbasx.concurrency;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class ThreadUtils {

    private static final String TAG = "ThreadUtils";


    public static Void runDelayed(int delay, final Callable<Void> callable) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    callable.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, delay);
        return null;
    }


    public static Void runFromBackground(final Callable<Void> callable) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    callable.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return null;
    }

}
