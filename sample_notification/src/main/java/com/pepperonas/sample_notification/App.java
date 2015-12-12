package com.pepperonas.sample_notification;

import android.app.Application;

import com.pepperonas.andbasx.AndBasx;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AndBasx.init(this);
    }
}
