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

        AesPrefs.initInt(TEST0, 3);
        AesPrefs.initInt(TEST0, 6);

        AesPrefs.initString(TEST1, "Hello");
        AesPrefs.initString(TEST1, "World");

        AesPrefs.initBoolean(TEST2, true);
        AesPrefs.initBoolean(TEST2, false);

    }

}
