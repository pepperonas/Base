package com.pepperonas.jbasx.concurrency;

import com.pepperonas.jbasx.interfaces.ThreadListener;

import java.util.concurrent.Callable;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class ThreadUtils {

    private static final String TAG = "ThreadUtils";


    public static Void runInBackground(final Callable<Void> callable) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    callable.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return null;
    }


    public static Void calculateStringInBackground(final ThreadListener threadListener, final Callable<String> callable) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    threadListener.onBaseThreadSuccess(callable.call());
                } catch (Exception e) {
                    threadListener.onBaseThreadFailed(e.getMessage());
                }
            }
        }).start();
        return null;
    }

}
