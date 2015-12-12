package com.pepperonas.testlib;

import com.pepperonas.aesprefs.AesPrefs;
import com.pepperonas.jbasx.log.Log;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class AesTest {

    private static final String TAG = "AesTest";
    public static final String TOUCH_TWICE_EXIT = "TOUCH_TWICE_EXIT";
    public static final String TEST0 = "a0";
    public static final String TEST1 = "b0222";
    public static final String TEST2 = "c13";
    public static final String TEST3 = "d13";


    public AesTest() {
        Log.i(TAG, "AesTest starting...");

        //        AesPrefs.initToggle("T1", false);
        //        AesPrefs.initToggle("T1", true);
        //        AesPrefs.readToggle("T1");
        //        AesPrefs.toggle("T1");

        //        AesPrefs.initToggle(TGL_TOUCH_TWICE_EXIT, false);
        //        Log.d(TAG, "AesTest READ: " + AesPrefs.readToggle(TGL_TOUCH_TWICE_EXIT));
        //        boolean toggle = AesPrefs.toggle(TGL_TOUCH_TWICE_EXIT);
        //        Log.d(TAG, "AesTest BOOL= " + toggle);

        AesPrefs.initInt(TEST0, 3);
        AesPrefs.initInt(TEST0, 6);

        AesPrefs.initString(TEST1, "Hello");
        AesPrefs.initString(TEST1, "World");

        AesPrefs.initBoolean(TEST2, true);
        AesPrefs.initBoolean(TEST2, false);

    }

}
