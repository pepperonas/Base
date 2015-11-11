package com.pepperonas.samplerecordbreaker;

import android.app.Application;

import com.pepperonas.andbasx.AndBasx;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class App extends Application {

    private static final String TAG = "App";


    @Override
    public void onCreate() {
        super.onCreate();

        /*AndCommon*/
        AndBasx.init(this, AndBasx.LogMode.ALL);

    }

}
