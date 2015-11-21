package com.pepperonas.sample_activityrecognition;

import android.app.Application;

import com.pepperonas.aesprefs.AesPrefs;
import com.pepperonas.andbasx.AndBasx;
import com.pepperonas.jbasx.Jbasx;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class App extends Application {

    private static final String TAG = "App";


    @Override
    public void onCreate() {
        super.onCreate();

        /*Jbasx*/
        Jbasx.Version.showVersionInfo();

        /*AndCommon*/
        AndBasx.init(this, AndBasx.LogMode.ALL);
        AndBasx.storeLogFileOnExternalStorage("sample_activityrecognition.log", true);

        AesPrefs.initCompleteConfig(this, "aes_config", "ihdO/()#+HJs3)", AesPrefs.LogMode.ALL);

    }

}
