package com.pepperonas.andbasx.base;

import android.os.Environment;

import com.pepperonas.andbasx.AndBasx;
import com.pepperonas.jbasx.log.Log;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class AndroidStorageUtils {

    private static final String TAG = "AndroidStorageUtils";


    /**
     * The app's internal directory (such as '/data/data/com.pepperonas.testapp/files').
     */
    public static String getAppsInternalDataDir() {
        return AndBasx.getContext().getFilesDir().getAbsolutePath();
    }


    /**
     * The app's external files directory (such as '/storage/emulated/0/Android/data/com.pepperonas.testapp/files/dirName').
     */
    public static String getAppsExternalFileDir(String dirName) {
        if (AndBasx.getContext().getExternalFilesDir(dirName) != null) {
            return AndBasx.getContext().getExternalFilesDir(dirName).getAbsolutePath();
        } else {
            Log.e(TAG, "getDownloadDir - Not found.");
            return null;
        }
    }


    /**
     * The app's external cache directory (such as '/storage/emulated/0/Android/data/com.pepperonas.testapp/cache').
     */
    public static String getAppsExternalCacheDir() {
        if (AndBasx.getContext().getExternalCacheDir() != null) {
            return AndBasx.getContext().getExternalCacheDir().getAbsolutePath();
        } else {
            Log.e(TAG, "getAppsExternalCacheDir - Not found.");
            return null;
        }
    }


    /**
     * The external root directory (such as '/storage/emulated/0').
     */
    public static String getExternalRootDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }


    /**
     * The download directory (such as '/storage/emulated/0/Download').
     */
    public static String getDownloadDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download";
    }

}
