/*
 * Copyright (c) 2015 Martin Pfeffer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pepperonas.andbasx.system;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.pepperonas.andbasx.AndBasx;
import com.pepperonas.andbasx.base.ToastUtils;
import com.pepperonas.jbasx.log.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class SystemUtils {

    private static final String TAG = "SystemUtils";

    public static final int MAX_BRIGHTNESS = 255;
    public static final int MIN_BRIGHTNESS = 0;


    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }


    /**
     * Handle action in {@link Activity#onRequestPermissionsResult}.
     *
     * @param activity    The calling {@link Activity}.
     * @param requestCode The request code to handle in {@link Activity#onRequestPermissionsResult}.
     */
    private void launchAppSettings(Activity activity, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(intent, requestCode);
    }


    public static void closeEntireApp(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        activity.startActivity(intent);
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        activity.finish();
    }


    public static boolean isInstalled(String packageName) {
        PackageInfo packageInfo;
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        final PackageManager packageManager = AndBasx.getContext().getPackageManager();
        List<PackageInfo> packageInformation = packageManager.getInstalledPackages(0);
        if (packageInformation == null) {
            return false;
        }
        for (PackageInfo packageInfo1 : packageInformation) {
            packageInfo = packageInfo1;
            final String name = packageInfo.packageName;
            if (packageName.equals(name)) {
                return true;
            }
        }
        return false;
    }


    public static int getStatusBarHeight() {
        int height = 0;
        if (AndBasx.getContext() == null) {
            return height;
        }
        Resources resources = AndBasx.getContext().getResources();
        int resId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            height = resources.getDimensionPixelSize(resId);
        }
        return height;
    }


    public static void uninstallApp(String packageName) {
        boolean installed = isInstalled(packageName);
        if (!installed) {
            ToastUtils.toastShort("Package not installed.");
            return;
        }

        boolean isRooted = isRooted();
        if (isRooted) {
            runRootCmd("pm uninstall " + packageName);
        } else {
            Uri uri = Uri.parse("package:" + packageName);
            Intent intent = new Intent(Intent.ACTION_DELETE, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            AndBasx.getContext().startActivity(intent);
        }
    }


    public static int getSystemScreenBrightness() {
        int screenBrightness = 255;
        try {
            screenBrightness = Settings.System.getInt(
                    AndBasx.getContext().getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenBrightness;
    }


    public static void setSystemScreenBrightness(int brightness) {
        try {
            if (brightness < MIN_BRIGHTNESS) {
                brightness = MIN_BRIGHTNESS;
            }
            if (brightness > MAX_BRIGHTNESS) {
                brightness = MAX_BRIGHTNESS;
            }
            ContentResolver resolver = AndBasx.getContext().getContentResolver();
            Uri uri = Settings.System
                    .getUriFor(Settings.System.SCREEN_BRIGHTNESS);
            Settings.System.putInt           (resolver, Settings.System.SCREEN_BRIGHTNESS,
                                   brightness);
            resolver.notifyChange(uri, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static int getBrightnessMode() {
        int brightnessMode = Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;
        try {
            brightnessMode = Settings.System.getInt       (
                    AndBasx.getContext().getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return brightnessMode;
    }


    /**
     * @param brightnessMode 1 auto, 0 manual
     */
    public static void setBrightnessMode(int brightnessMode) {
        try {
            Settings.System.putInt                                        (
                    AndBasx.getContext().getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE, brightnessMode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Requires:
     * {@link android.Manifest.permission#CHANGE_WIFI_STATE}
     */
    public static void setWifiEnabled(boolean enable) {
        try {
            WifiManager wifiManager = (WifiManager)
                    AndBasx.getContext().getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(enable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean isWifiEnabled() {
        boolean enabled = false;
        try {
            WifiManager wifiManager = (WifiManager)
                    AndBasx.getContext().getSystemService(Context.WIFI_SERVICE);
            enabled = wifiManager.isWifiEnabled();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return enabled;
    }


    public static boolean isRooted() {
        String binaryName = "su";
        boolean rooted = false;
        String[] places = {"/sbin/", "/system/bin/", "/system/xbin/",
                           "/data/local/xbin/", "/data/local/bin/", "/system/sd/xbin/",
                           "/system/bin/failsafe/", "/data/local/"};
        for (String where : places) {
            if (new File(where + binaryName).exists()) {
                rooted = true;
                break;
            }
        }
        return rooted;
    }


    public static void lockScreen() {
        DevicePolicyManager deviceManager = (DevicePolicyManager)
                AndBasx.getContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        deviceManager.lockNow();
    }


    public static void inputKeyEvent(int keyCode) {
        try {
            runRootCmd("Input key-event: " + keyCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String runCmd(String cmd) {
        if (TextUtils.isEmpty(cmd)) {
            return null;
        }
        Process process;
        String result = null;

        String[] commands = {"/system/bin/sh", "-c", cmd};

        try {
            process = Runtime.getRuntime().exec(commands);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read;
            InputStream errIs = process.getErrorStream();
            while ((read = errIs.read()) != -1) {
                baos.write(read);
            }
            baos.write('\n');

            InputStream inIs = process.getInputStream();
            while ((read = inIs.read()) != -1) {
                baos.write(read);
            }

            byte[] data = baos.toByteArray();
            result = new String(data);

            Log.d(TAG, "runCmd result: " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static void runRootCmd(String cmd) {
        if (TextUtils.isEmpty(cmd)) {
            return;
        }
        Process process;
        try {
            process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(
                    process.getOutputStream());
            os.writeBytes(cmd + " ;\n");
            os.flush();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read;
            InputStream errIs = process.getErrorStream();
            while ((read = errIs.read()) != -1) {
                baos.write(read);
            }
            baos.write('\n');

            InputStream inIs = process.getInputStream();
            while ((read = inIs.read()) != -1) {
                baos.write(read);
            }

            byte[] data = baos.toByteArray();
            String result = new String(data);

            Log.d(TAG, "runRootCmd result: " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static int getDistance(MotionEvent e1, MotionEvent e2) {
        float x = e1.getX() - e2.getX();
        float y = e1.getY() - e2.getY();
        return (int) Math.sqrt(x * x + y * y);
    }


    public static long getMaxMemory() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        Log.d(TAG, "Application max memory: " + maxMemory);
        return maxMemory;
    }


    public static boolean isDebuggable() {
        return ((AndBasx.getContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
    }


    public static String getApplicationDir() {
        PackageManager packageManager = AndBasx.getContext().getPackageManager();
        String packageName = AndBasx.getContext().getPackageName();
        String applicationDir = null;
        try {
            PackageInfo p = packageManager.getPackageInfo(packageName, 0);
            applicationDir = p.applicationInfo.dataDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return applicationDir;
    }

}
