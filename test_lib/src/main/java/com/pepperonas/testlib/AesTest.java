package com.pepperonas.testlib;

import com.pepperonas.aesprefs.AesPrefs;
import com.pepperonas.jbasx.log.Log;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class AesTest {

    private static final String TAG = "AesTest";
    public static final String TGL_TOUCH_TWICE_EXIT = "TGL_TOUCH_TWICE_EXIT2";


    public AesTest() {
        Log.i(TAG, "AesTest starting...");


        AesPrefs.initToggle("T1", false);
        AesPrefs.initToggle("T1", true);
        AesPrefs.readToggle("T1");
        AesPrefs.toggle("T1");

        AesPrefs.initToggle(TGL_TOUCH_TWICE_EXIT, false);
        Log.d(TAG, "AesTest READ: " + AesPrefs.readToggle(TGL_TOUCH_TWICE_EXIT));
        boolean toggle = AesPrefs.toggle(TGL_TOUCH_TWICE_EXIT);
        Log.d(TAG, "AesTest BOOL= " + toggle);
    }

}
