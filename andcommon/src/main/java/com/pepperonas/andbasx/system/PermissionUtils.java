package com.pepperonas.andbasx.system;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import com.pepperonas.andbasx.AndBasx;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class PermissionUtils {

    private static final String TAG = "PermissionUtils";


    public static boolean ensurePermissions(@NonNull String... permissions) {
        for (String permission : permissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (PackageManager.PERMISSION_GRANTED != AndBasx.getContext().checkSelfPermission(permission)) {
                    Log.i(TAG, "ensurePermissions:  " + permission + " is not granted.");
                    return false;
                } else Log.i(TAG, "ensurePermissions:  " + permission + " is granted.");
            } else {
                Log.i(TAG, "ensurePermissions: Build.VERSION( " + Build.VERSION.SDK_INT + ") < 23. Return true.");
                return true;
            }
        }
        return true;
    }


    /**
     * Handle action in {@link Activity#onRequestPermissionsResult}.
     *
     * @param activity    The calling {@link Activity}.
     * @param requestCode The request code to handle in {@link Activity#onRequestPermissionsResult}.
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void launchIntentToManageOverlayPermission(Activity activity, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(intent, requestCode);
    }

}
