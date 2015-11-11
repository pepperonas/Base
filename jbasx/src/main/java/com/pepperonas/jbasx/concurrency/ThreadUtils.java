package com.pepperonas.jbasx.concurrency;

import com.pepperonas.jbasx.interfaces.ThreadListener;

import java.util.concurrent.Callable;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class ThreadUtils {

    private static final String TAG = "ThreadUtils";


    public static Void runInBackground(final Callable<Void> func) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    func.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return null;
    }


    public static Void calculateStringInBackground(final ThreadListener l, final Callable<String> func) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    l.onBaseThreadSuccess(func.call());
                } catch (Exception e) {
                    l.onBaseThreadFailed(e.getMessage());
                }
            }
        }).start();
        return null;
    }

}
