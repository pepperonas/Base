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

package com.pepperonas.aesprefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;

import com.pepperonas.andbasx.system.AppUtils;
import com.pepperonas.jbasx.format.NumberFormatUtils;
import com.pepperonas.jbasx.format.TimeFormatUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class AesPrefs {

    /**
     * Constants
     */
    private static final String TAG = "AesPrefs";

    private static final String TAIL = "=";

    /**
     * Member
     */
    private static Context mCtx;

    private static String mFilename;
    private static String mPassword;

    private static long mIv;

    private static long mDuration = 0;


    public enum LogMode {
        NONE(-1), DEFAULT(0), GET(1), SET(2), ALL(3);

        private final int mode;


        LogMode(int i) {this.mode = i;}
    }


    private static LogMode mLog = LogMode.DEFAULT;


    public static void logMode(LogMode logMode) {
        mLog = logMode;
    }


    public static void init(Context context, String filename, String password, LogMode logMode) {
        mLog = logMode;
        init(context, filename, password);
    }


    public static void initCompleteConfig(Context context, String filename, String password, LogMode logMode) {
        mLog = logMode;
        init(context, filename, password);
        initOrIncrementLaunchCounter();
        storeInstallationDate();
    }


    public static void init(Context context, String filename, String password) {
        if (mLog != LogMode.NONE) {
            Log.i(TAG, "Initializing AesPrefs...");
        }

        mCtx = context.getApplicationContext();
        mFilename = filename;
        mPassword = password;
        mIv = System.currentTimeMillis();

        SharedPreferences sp = mCtx.getSharedPreferences("iv_config", Context.MODE_PRIVATE);

        if (sp.contains("aes_iv")) {

            if (mLog != LogMode.NONE) {
                Log.i(TAG, "IV found {" + mIv + "}");
            }

            //  retrieving an IV we can rely on.
            mIv = sp.getLong("aes_iv", -1);

        } else {
            // this IV will be used to keep track of your preference keys.
            // preference values have their own IVs.
            if (mLog != LogMode.NONE) {
                Log.w(TAG, "New IV set {" + mIv + "}");
            }

            mIv = System.currentTimeMillis();
            sp.edit().putLong("aes_iv", mIv).apply();
        }
    }


    public static String getEncryptedKey(String key) {
        SharedPreferences sp = mCtx.getSharedPreferences(
                mFilename, Context.MODE_PRIVATE         );

        String _key = Crypt.encrypt(mPassword, key, mIv) + TAIL;
        return _key.substring(0, _key.length() - 1);
    }


    public static void registerOnSharedPreferenceChangeListener(
            SharedPreferences.OnSharedPreferenceChangeListener listener) {

        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);
        sp.registerOnSharedPreferenceChangeListener(listener);
    }


    public static void unregisterOnSharedPreferenceChangeListener(
            SharedPreferences.OnSharedPreferenceChangeListener listener) {

        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);
        sp.unregisterOnSharedPreferenceChangeListener(listener);
    }


    public static boolean contains(String key) {
        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);
        String _key = Crypt.encrypt(mPassword, key, mIv) + TAIL;
        long iv = sp.getLong(_key, 0);
        key = _key.substring(0, _key.length() - 1);
        return sp.contains(key);
    }


    public static void put(String key, String value) {
        long start = System.currentTimeMillis();

        long iv = System.currentTimeMillis();

        String encryptedKey = Crypt.encrypt(mPassword, key, mIv);
        String encryptedValue = Crypt.encrypt(mPassword, value, iv);
        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        sp.edit()
          .putString(encryptedKey, encryptedValue)
          .putLong(encryptedKey + TAIL, iv)
          .apply();

        if (mLog == LogMode.ALL || mLog == LogMode.SET) {
            Log.d(TAG, "put " + key + " <- " + value);
        }

        mDuration += System.currentTimeMillis() - start;
    }


    public static String get(String key, String defaultValue) {
        long start = System.currentTimeMillis();
        String param = key;

        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        String _key = Crypt.encrypt(mPassword, key, mIv) + TAIL;
        long iv = sp.getLong(_key, 0);
        key = _key.substring(0, _key.length() - 1);

        if (!sp.contains(key) && mLog != LogMode.NONE) {
            Log.e(TAG, "WARNING: Key '" + param + "' not found.\n" +
                       "Return value: " + (defaultValue.isEmpty() ? "\"\"" : defaultValue));
            return defaultValue;
        }

        try {
            mDuration += System.currentTimeMillis() - start;
            String value = Crypt.decrypt(mPassword, sp.getString(key, ""), iv);
            if (mLog == LogMode.ALL || mLog == LogMode.GET) {
                Log.d(TAG, "get  " + param + " -> " + value);
            }
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }


    public static void putInt(String key, int value) {
        long start = System.currentTimeMillis();

        long iv = System.currentTimeMillis();

        String encryptedKey = Crypt.encrypt(mPassword, key, mIv);
        String encryptedValue = Crypt.encrypt(mPassword, String.valueOf(value), iv);
        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        sp.edit()
          .putString(encryptedKey, encryptedValue)
          .putLong(encryptedKey + TAIL, iv)
          .apply();

        if (mLog == LogMode.ALL || mLog == LogMode.SET) {
            Log.d(TAG, "putInt " + key + " <- " + value);
        }

        mDuration += System.currentTimeMillis() - start;
    }


    public static int getInt(String key, int defaultValue) {
        long start = System.currentTimeMillis();
        String param = key;

        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        String _key = Crypt.encrypt(mPassword, key, mIv) + TAIL;
        long iv = sp.getLong(_key, 0);
        key = _key.substring(0, _key.length() - 1);

        if (!sp.contains(key) && mLog != LogMode.NONE) {
            Log.e(TAG, "WARNING: Key '" + param + "' not found.\n" +
                       "Return value: " + defaultValue);
            return defaultValue;
        }

        try {
            mDuration += System.currentTimeMillis() - start;
            int value = Integer.parseInt(Crypt.decrypt(mPassword, sp.getString(key, ""), iv));
            if (mLog == LogMode.ALL || mLog == LogMode.GET) {
                Log.d(TAG, "getInt  " + param + " -> " + value);
            }
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }


    public static void putLong(String key, long value) {
        long start = System.currentTimeMillis();

        long iv = System.currentTimeMillis();

        String encryptedKey = Crypt.encrypt(mPassword, key, mIv);
        String encryptedValue = Crypt.encrypt(mPassword, String.valueOf(value), iv);
        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        sp.edit()
          .putString(encryptedKey, encryptedValue)
          .putLong(encryptedKey + TAIL, iv)
          .apply();

        if (mLog == LogMode.ALL || mLog == LogMode.SET) {
            Log.d(TAG, "putLong " + key + " <- " + value);
        }

        mDuration += System.currentTimeMillis() - start;
    }


    public static long getLong(String key, long defaultValue) {
        long start = System.currentTimeMillis();
        String param = key;

        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        String _key = Crypt.encrypt(mPassword, key, mIv) + TAIL;
        long iv = sp.getLong(_key, 0);
        key = _key.substring(0, _key.length() - 1);

        if (!sp.contains(key) && mLog != LogMode.NONE) {
            Log.e(TAG, "WARNING: Key '" + param + "' not found.\n" +
                       "Return value: " + defaultValue);
            return defaultValue;
        }

        try {
            mDuration += System.currentTimeMillis() - start;
            long value = Long.parseLong(Crypt.decrypt(mPassword, sp.getString(key, ""), iv));
            if (mLog == LogMode.ALL || mLog == LogMode.GET) {
                Log.d(TAG, "getLong  " + param + " -> " + value);
            }
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }


    public static void putDouble(String key, double value) {
        long start = System.currentTimeMillis();

        long iv = System.currentTimeMillis();

        String encryptedKey = Crypt.encrypt(mPassword, key, mIv);
        String encryptedValue = Crypt.encrypt(mPassword, String.valueOf(value), iv);
        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        sp.edit()
          .putString(encryptedKey, encryptedValue)
          .putLong(encryptedKey + TAIL, iv)
          .apply();

        if (mLog == LogMode.ALL || mLog == LogMode.SET) {
            Log.d(TAG, "putDouble " + key + " <- " + value);
        }

        mDuration += System.currentTimeMillis() - start;
    }


    public static double getDouble(String key, double defaultValue) {
        long start = System.currentTimeMillis();
        String param = key;

        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        String _key = Crypt.encrypt(mPassword, key, mIv) + TAIL;
        long iv = sp.getLong(_key, 0);
        key = _key.substring(0, _key.length() - 1);

        if (!sp.contains(key) && mLog != LogMode.NONE) {
            Log.e(TAG, "WARNING: Key '" + param + "' not found.\n" +
                       "Return value: " + defaultValue);
            return defaultValue;
        }

        try {
            mDuration += System.currentTimeMillis() - start;
            double value = Double.parseDouble(Crypt.decrypt(mPassword, sp.getString(key, ""), iv));
            if (mLog == LogMode.ALL || mLog == LogMode.GET) {
                Log.d(TAG, "getDouble  " + param + " -> " + value);
            }
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }


    public static void putFloat(String key, float value) {
        long start = System.currentTimeMillis();

        long iv = System.currentTimeMillis();

        String encryptedKey = Crypt.encrypt(mPassword, key, mIv);
        String encryptedValue = Crypt.encrypt(mPassword, String.valueOf(value), iv);
        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        sp.edit()
          .putString(encryptedKey, encryptedValue)
          .putLong(encryptedKey + TAIL, iv)
          .apply();

        if (mLog == LogMode.ALL || mLog == LogMode.SET) {
            Log.d(TAG, "putFloat " + key + " <- " + value);
        }

        mDuration += System.currentTimeMillis() - start;
    }


    public static float getFloat(String key, float defaultValue) {
        long start = System.currentTimeMillis();
        String param = key;

        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        String _key = Crypt.encrypt(mPassword, key, mIv) + TAIL;
        long iv = sp.getLong(_key, 0);
        key = _key.substring(0, _key.length() - 1);

        if (!sp.contains(key) && mLog != LogMode.NONE) {
            Log.e(TAG, "WARNING: Key '" + param + "' not found.\n" +
                       "Return value: " + defaultValue);
            return defaultValue;
        }

        try {
            mDuration += System.currentTimeMillis() - start;
            float value = Float.parseFloat(Crypt.decrypt(mPassword, sp.getString(key, ""), iv));
            if (mLog == LogMode.ALL || mLog == LogMode.GET) {
                Log.d(TAG, "getFloat  " + param + " -> " + value);
            }
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }


    public static void putBoolean(String key, boolean value) {
        long start = System.currentTimeMillis();

        long iv = System.currentTimeMillis();

        String encryptedKey = Crypt.encrypt(mPassword, key, mIv);
        String encryptedValue = Crypt.encrypt(mPassword, String.valueOf(value), iv);
        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        sp.edit()
          .putString(encryptedKey, encryptedValue)
          .putLong(encryptedKey + TAIL, iv)
          .apply();

        if (mLog == LogMode.ALL || mLog == LogMode.SET) {
            Log.d(TAG, "putBoolean " + key + " <- " + value);
        }

        mDuration += System.currentTimeMillis() - start;
    }


    public static boolean getBoolean(String key, boolean defaultValue) {
        long start = System.currentTimeMillis();
        String param = key;

        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        String _key = Crypt.encrypt(mPassword, key, mIv) + TAIL;
        long iv = sp.getLong(_key, 0);
        key = _key.substring(0, _key.length() - 1);

        if (!sp.contains(key) && mLog != LogMode.NONE) {
            Log.e(TAG, "WARNING: Key '" + param + "' not found.\n" +
                       "Return value: " + defaultValue);
            return defaultValue;
        }

        try {
            mDuration += System.currentTimeMillis() - start;
            boolean value = Boolean.parseBoolean(Crypt.decrypt(mPassword, sp.getString(key, ""), iv));
            if (mLog == LogMode.ALL || mLog == LogMode.GET) {
                Log.d(TAG, "getBoolean  " + param + " -> " + value);
            }
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }


    public static void storeArray(String key, List<String> values) {
        long start = System.currentTimeMillis();

        long iv = System.currentTimeMillis();

        String encryptedKey = Crypt.encrypt(mPassword, key, mIv);
        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        sp.edit()
          .putInt(encryptedKey + "_size", values.size())
          .putLong(encryptedKey + TAIL, iv)
          .apply();

        for (int i = 0; i < values.size(); i++) {
            String encryptedValue = Crypt.encrypt(mPassword, values.get(i), iv);
            sp.edit().putString(encryptedKey + "_" + i, encryptedValue).apply();
        }

        mDuration += System.currentTimeMillis() - start;
    }


    public static List<String> restoreArray(String key) {
        long start = System.currentTimeMillis();

        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        String _key = Crypt.encrypt(mPassword, key, mIv) + TAIL;
        long iv = sp.getLong(_key, 0);
        key = _key.substring(0, _key.length() - 1);
        int size = sp.getInt(key + "_size", 0);

        try {
            List<String> strings = new ArrayList<>();
            for (int i = 0; i < size; i++) {

                if (!sp.contains(key + "_" + i) && mLog != LogMode.NONE) {
                    Log.e(TAG, "WARNING: Key '" + key + "_" + i + "' not found.\n" +
                               "Return value: " + "new ArrayList<String>(0)");
                    return new ArrayList<>();
                }

                strings.add(Crypt.decrypt(mPassword, sp.getString(key + "_" + i, ""), iv));
            }
            mDuration += System.currentTimeMillis() - start;
            return strings;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    public static void put(int stringId, String value) {
        put(mCtx.getString(stringId), value);
    }


    public static String get(int stringId, String defaultValue) {
        return get(mCtx.getString(stringId), defaultValue);
    }


    public static void putInt(int stringId, int value) {
        putInt(mCtx.getString(stringId), value);
    }


    public static int getInt(int stringId, int defaultValue) {
        return getInt(mCtx.getString(stringId), defaultValue);
    }


    public static void putLong(int stringId, long value) {
        putLong(mCtx.getString(stringId), value);
    }


    public static long getLong(int stringId, long defaultValue) {
        return getLong(mCtx.getString(stringId), defaultValue);
    }


    public static void putDouble(int stringId, double value) {
        putDouble(mCtx.getString(stringId), value);
    }


    public static double getDouble(int stringId, double defaultValue) {
        return getDouble(mCtx.getString(stringId), defaultValue);
    }


    public static void putFloat(int stringId, float value) {
        putFloat(mCtx.getString(stringId), value);
    }


    public static float getFloat(int stringId, float defaultValue) {
        return getFloat(mCtx.getString(stringId), defaultValue);
    }


    public static void putBoolean(int stringId, boolean value) {
        putBoolean(mCtx.getString(stringId), value);
    }


    public static boolean getBoolean(int stringId, boolean defaultValue) {
        return getBoolean(mCtx.getString(stringId), defaultValue);
    }


    public static void storeArray(int stringId, List<String> values) {
        storeArray(mCtx.getString(stringId), values);
    }


    public static List<String> restoreArray(int stringId) {
        return restoreArray(mCtx.getString(stringId));
    }


    public static String getEncryptedContent() {
        Map<String, ?> sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE).getAll();

        String result = "";
        for (String key : sp.keySet()) {
            Object value = sp.get(key);
            if (value instanceof String) {
                result += key + " : " + value + "\n";
            }
        }
        return result;
    }


    public static int countEntries() {
        Map<String, ?> prefs = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE).getAll();
        int ctr = 0;

        for (String key : prefs.keySet()) {
            Object value = prefs.get(key);
            if (value instanceof String) {
                ctr++;
            }
        }
        return ctr;
    }


    public static void deleteAll() {
        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }


    public static void initOrIncrementLaunchCounter() {
        LogMode tmp = mLog;
        mLog = LogMode.NONE;
        if (getInt("aes_app_launches", -1) == -1) {
            // first launch returns 0
            putInt("aes_app_launches", 0);
        } else {
            putInt("aes_app_launches", (getInt("aes_app_launches", 0) + 1));
        }
        mLog = tmp;
    }


    /**
     * Initializes a counter to store the app's current version-code.
     * <p/>
     * If the counter exists and the version-code has changed,
     * the counter will be updated and the method returns true.
     *
     * @return Whenever the app was updated or not.
     */
    public static boolean initOrCheckVersionCode() {
        LogMode tmp = mLog;
        mLog = LogMode.NONE;
        int versionCode = getInt("aes_app_version_code", -1);
        if ((versionCode != AppUtils.getVersionCode())
            || (versionCode == -1)) {
            putInt("aes_app_version_code", AppUtils.getVersionCode());
            // restore mLog before return.
            mLog = tmp;
            if (tmp == LogMode.ALL) {
                Log.i(TAG, "initOrCheckVersionCode: " + ((versionCode == -1) ? "Counter initialised." :
                                                         "Version has changed."));
            }
            // return false when counter is initialised.
            return versionCode != -1;
        }
        mLog = tmp;
        if (tmp == LogMode.ALL) {
            Log.i(TAG, "initOrCheckVersionCode: " + "Version has not changed.");
        }
        return false;
    }


    public static int getLaunchCounter() {
        return AesPrefs.getInt("aes_app_launches", 0);
    }


    public static void storeInstallationDate() {
        LogMode tmp = mLog;
        mLog = LogMode.NONE;
        if (getLong("aes_inst_date", 0L) == 0L) {
            putLong("aes_inst_date", System.currentTimeMillis());
        }
        mLog = tmp;
    }


    public static long getInstallationDate() {
        return getLong("aes_inst_date", 0L);
    }


    public static void printInstallationDate() {
        LogMode tmp = mLog;
        mLog = LogMode.NONE;
        Log.i(TAG, "Installation date: " + TimeFormatUtils.formatTime(getLong("aes_inst_date", 0L), TimeFormatUtils.DEFAULT_FORMAT));
        mLog = tmp;
    }


    public static void resetExecutionTime() {
        mDuration = 0L;
    }


    public static long getExecutionTime() {
        return mDuration;
    }


    public static void printExecutionTime() {
        Log.i(TAG, "Execution time: " + String.valueOf(NumberFormatUtils.decimalPlaces((double) mDuration / 1000, 3) + " sec."));
    }


    public static class Version {

        public static void showVersionInfo() {
            Log.i(TAG, "---VERSION-INFO---");
            Log.w(TAG, getVersionInfo());
        }


        public static String getVersionName() {
            return BuildConfig.VERSION_NAME;
        }


        public static String getVersionInfo() {
            return "aesprefs-" + getVersionName() + ".aar";
        }
    }

}