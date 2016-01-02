/*
 * Copyright (c) 2016 Martin Pfeffer
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
import android.preference.Preference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.Log;

import com.pepperonas.andbasx.system.AppUtils;
import com.pepperonas.andcommon.BuildConfig;
import com.pepperonas.jbasx.format.NumberFormatUtils;
import com.pepperonas.jbasx.format.TimeFormatUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Pfeffer (pepperonas)
 */

public class AesPrefs {

    private static final String TAIL = "=";

    /**
     * Constants
     */
    private static final String TAG = "AesPrefs";

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


        /**
         * Instantiates a new Log mode.
         *
         * @param i the
         */
        LogMode(int i) {this.mode = i;}
    }


    private static LogMode mLog = LogMode.DEFAULT;


    /**
     * Log mode.
     *
     * @param logMode the log mode
     */
    public static void logMode(@NonNull LogMode logMode) {
        mLog = logMode;
    }


    /**
     * Init.
     *
     * @param context  the context
     * @param filename the filename
     * @param password the password
     * @param logMode  the log mode
     */
    public static void init(@NonNull Context context, @NonNull String filename, @NonNull String password, @Nullable LogMode logMode) {
        if (logMode == null) {
            mLog = LogMode.NONE;
        } else mLog = logMode;
        init(context, filename, password);
    }


    /**
     * Init complete config.
     *
     * @param context  the context
     * @param filename the filename
     * @param password the password
     * @param logMode  the log mode
     */
    public static void initCompleteConfig(@NonNull Context context, @NonNull String filename, @NonNull String password, @Nullable LogMode logMode) {
        if (logMode == null) {
            mLog = LogMode.NONE;
        } else mLog = logMode;
        init(context, filename, password);
        initOrIncrementLaunchCounter();
        initInstallationDate();
    }


    /**
     * Init.
     *
     * @param context  the context
     * @param filename the filename
     * @param password the password
     */
    public static void init(@NonNull Context context, @NonNull String filename, @NonNull String password) {
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


    /**
     * Decrypted key string.
     * <p/>
     * Resolves a encrypted key. A encrypted key is returned by
     * {@link Preference.OnPreferenceClickListener}
     * or {@link android.content.SharedPreferences.OnSharedPreferenceChangeListener}.
     *
     * @param encryptedKey the key
     * @return the string
     */
    public static String decryptedKey(@NonNull String encryptedKey) {
        long start = System.currentTimeMillis();
        encryptedKey = encryptedKey.substring(0, encryptedKey.length() - 1);

        try {
            String decryptedKey = Crypt.decrypt(mPassword, encryptedKey, mIv);
            if (mLog == LogMode.ALL || mLog == LogMode.GET) {
                Log.d(TAG, "Decrypted key: " + decryptedKey);
            }
            mDuration += System.currentTimeMillis() - start;
            return decryptedKey;
        } catch (Exception e) {
            if (mLog == LogMode.ALL || mLog == LogMode.GET) {
                Log.e(TAG, "Error while decrypting key.");
            }
            mDuration += System.currentTimeMillis() - start;
            return "";
        }
    }


    /**
     * Gets encrypted key.
     *
     * @param key the key
     * @return the encrypted key
     */
    public static String getEncryptedKey(@NonNull String key) {
        String _key = Crypt.encrypt(mPassword, key, mIv) + TAIL;
        return _key.substring(0, _key.length() - 1);
    }


    /**
     * Register on shared preference change listener.
     *
     * @param listener the listener
     */
    public static void registerOnSharedPreferenceChangeListener(
            @NonNull SharedPreferences.OnSharedPreferenceChangeListener listener) {

        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);
        sp.registerOnSharedPreferenceChangeListener(listener);
    }


    /**
     * Unregister on shared preference change listener.
     *
     * @param listener the listener
     */
    public static void unregisterOnSharedPreferenceChangeListener(
            @NonNull SharedPreferences.OnSharedPreferenceChangeListener listener) {

        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);
        sp.unregisterOnSharedPreferenceChangeListener(listener);
    }


    /**
     * Sets click listeners on preferences.
     *
     * @param listener    the listener
     * @param preferences the preferences
     */
    public static void setClickListenersOnPreferences(
            @NonNull Preference.OnPreferenceClickListener listener, @NonNull Preference... preferences) {

        for (Preference preference : preferences) {
            preference.setOnPreferenceClickListener(listener);
        }
    }


    /**
     * Remove click listeners from preferences.
     *
     * @param preferences the preferences
     */
    public static void removeClickListenersFromPreferences(
            @NonNull Preference... preferences) {

        for (Preference preference : preferences) {
            preference.setOnPreferenceClickListener(null);
        }
    }


    /**
     * Sets change listeners on preferences.
     *
     * @param listener    the listener
     * @param preferences the preferences
     */
    public static void setChangeListenersOnPreferences(
            @NonNull Preference.OnPreferenceChangeListener listener, @NonNull Preference... preferences) {

        for (Preference preference : preferences) {
            preference.setOnPreferenceChangeListener(listener);
        }
    }


    /**
     * Remove change listeners from preferences.
     *
     * @param preferences the preferences
     */
    public static void removeChangeListenersFromPreferences(
            @NonNull Preference... preferences) {

        for (Preference preference : preferences) {
            preference.setOnPreferenceChangeListener(null);
        }
    }


    /**
     * Contains boolean.
     *
     * @param key the key
     * @return the boolean
     */
    public static boolean contains(@NonNull String key) {
        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);
        String _key = Crypt.encrypt(mPassword, key, mIv) + TAIL;
        long iv = sp.getLong(_key, 0);
        key = _key.substring(0, _key.length() - 1);
        return sp.contains(key);
    }


    /**
     * Put.
     *
     * @param key   the key
     * @param value the value
     */
    public static void put(@NonNull String key, @Nullable String value) {
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


    /**
     * Get string.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the string
     */
    @Nullable
    public static String get(@NonNull String key, @Nullable String defaultValue) {
        long start = System.currentTimeMillis();
        String param = key;

        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        String _key = Crypt.encrypt(mPassword, key, mIv) + TAIL;
        long iv = sp.getLong(_key, 0);
        key = _key.substring(0, _key.length() - 1);

        if (!sp.contains(key)) {
            if (mLog != LogMode.NONE) {
                if (defaultValue == null) {
                    Log.e(TAG, "WARNING: Key '" + param + "' not found (return:  null)");
                } else
                    Log.e(TAG, "WARNING: Key '" + param + "' not found (return: " + (defaultValue.isEmpty() ? "\"\"" : defaultValue) + ")");
            }
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


    /**
     * Gets nullable.
     *
     * @param key the key
     * @return the nullable
     */
    @Nullable
    public static String getNullable(@NonNull String key) {
        long start = System.currentTimeMillis();
        String param = key;

        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        String _key = Crypt.encrypt(mPassword, key, mIv) + TAIL;
        long iv = sp.getLong(_key, 0);
        key = _key.substring(0, _key.length() - 1);

        if (!sp.contains(key)) {
            if (mLog != LogMode.NONE) {
                Log.e(TAG, "WARNING: Key '" + param + "' not found (return: null)");
            }
            return null;
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
            return null;
        }
    }


    /**
     * Put int.
     *
     * @param key   the key
     * @param value the value
     */
    public static void putInt(@NonNull String key, int value) {
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


    /**
     * Gets int.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the int
     */
    public static int getInt(@NonNull String key, int defaultValue) {
        long start = System.currentTimeMillis();
        String param = key;

        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        String _key = Crypt.encrypt(mPassword, key, mIv) + TAIL;
        long iv = sp.getLong(_key, 0);
        key = _key.substring(0, _key.length() - 1);

        if (!sp.contains(key)) {
            if (mLog != LogMode.NONE) {
                Log.e(TAG, "WARNING: Key '" + param + "' not found (return: " + defaultValue + ")");
            }
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


    /**
     * Gets int nullable.
     *
     * @param key the key
     * @return the int nullable
     */
    @Nullable
    public static Integer getIntNullable(@NonNull String key) {
        long start = System.currentTimeMillis();
        String param = key;

        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        String _key = Crypt.encrypt(mPassword, key, mIv) + TAIL;
        long iv = sp.getLong(_key, 0);
        key = _key.substring(0, _key.length() - 1);

        if (!sp.contains(key)) {
            if (mLog != LogMode.NONE) {
                Log.e(TAG, "WARNING: Key '" + param + "' not found (return: null)");
            }
            return null;
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
            return null;
        }
    }


    /**
     * Put long.
     *
     * @param key   the key
     * @param value the value
     */
    public static void putLong(@NonNull String key, long value) {
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


    /**
     * Gets long.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the long
     */
    public static long getLong(@NonNull String key, long defaultValue) {
        long start = System.currentTimeMillis();
        String param = key;

        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        String _key = Crypt.encrypt(mPassword, key, mIv) + TAIL;
        long iv = sp.getLong(_key, 0);
        key = _key.substring(0, _key.length() - 1);

        if (!sp.contains(key)) {
            if (mLog != LogMode.NONE) {
                Log.e(TAG, "WARNING: Key '" + param + "' not found (return: " + defaultValue + ")");
            }
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


    /**
     * Gets long nullable.
     *
     * @param key the key
     * @return the long nullable
     */
    @Nullable
    public static Long getLongNullable(@NonNull String key) {
        long start = System.currentTimeMillis();
        String param = key;

        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        String _key = Crypt.encrypt(mPassword, key, mIv) + TAIL;
        long iv = sp.getLong(_key, 0);
        key = _key.substring(0, _key.length() - 1);

        if (!sp.contains(key)) {
            if (mLog != LogMode.NONE) {
                Log.e(TAG, "WARNING: Key '" + param + "' not found (return: null)");
            }
            return null;
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
            return null;
        }
    }


    /**
     * Put double.
     *
     * @param key   the key
     * @param value the value
     */
    public static void putDouble(@NonNull String key, double value) {
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


    /**
     * Gets double.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the double
     */
    public static double getDouble(@NonNull String key, double defaultValue) {
        long start = System.currentTimeMillis();
        String param = key;

        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        String _key = Crypt.encrypt(mPassword, key, mIv) + TAIL;
        long iv = sp.getLong(_key, 0);
        key = _key.substring(0, _key.length() - 1);

        if (!sp.contains(key)) {
            if (mLog != LogMode.NONE) {
                Log.e(TAG, "WARNING: Key '" + param + "' not found (return: " + defaultValue + ")");
            }
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


    /**
     * Gets double nullable.
     *
     * @param key the key
     * @return the double nullable
     */
    @Nullable
    public static Double getDoubleNullable(@NonNull String key) {
        long start = System.currentTimeMillis();
        String param = key;

        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        String _key = Crypt.encrypt(mPassword, key, mIv) + TAIL;
        long iv = sp.getLong(_key, 0);
        key = _key.substring(0, _key.length() - 1);

        if (!sp.contains(key)) {
            if (mLog != LogMode.NONE) {
                Log.e(TAG, "WARNING: Key '" + param + "' not found (return: null)");
            }
            return null;
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
            return null;
        }
    }


    /**
     * Put float.
     *
     * @param key   the key
     * @param value the value
     */
    public static void putFloat(@NonNull String key, float value) {
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


    /**
     * Gets float.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the float
     */
    public static float getFloat(@NonNull String key, float defaultValue) {
        long start = System.currentTimeMillis();
        String param = key;

        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        String _key = Crypt.encrypt(mPassword, key, mIv) + TAIL;
        long iv = sp.getLong(_key, 0);
        key = _key.substring(0, _key.length() - 1);

        if (!sp.contains(key)) {
            if (mLog != LogMode.NONE) {
                Log.e(TAG, "WARNING: Key '" + param + "' not found (return: " + defaultValue + ")");
            }
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


    /**
     * Gets float nullable.
     *
     * @param key the key
     * @return the float nullable
     */
    @Nullable
    public static Float getFloatNullable(@NonNull String key) {
        long start = System.currentTimeMillis();
        String param = key;

        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        String _key = Crypt.encrypt(mPassword, key, mIv) + TAIL;
        long iv = sp.getLong(_key, 0);
        key = _key.substring(0, _key.length() - 1);

        if (!sp.contains(key)) {
            if (mLog != LogMode.NONE) {
                Log.e(TAG, "WARNING: Key '" + param + "' not found (return: null)");
            }
            return null;
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
            return null;
        }
    }


    /**
     * Put boolean.
     *
     * @param key   the key
     * @param value the value
     */
    public static void putBoolean(@NonNull String key, boolean value) {
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


    /**
     * Gets boolean.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the boolean
     */
    public static boolean getBoolean(@NonNull String key, boolean defaultValue) {
        long start = System.currentTimeMillis();
        String param = key;

        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        String _key = Crypt.encrypt(mPassword, key, mIv) + TAIL;
        long iv = sp.getLong(_key, 0);
        key = _key.substring(0, _key.length() - 1);

        if (!sp.contains(key)) {
            if (mLog != LogMode.NONE) {
                Log.e(TAG, "WARNING: Key '" + param + "' not found (return: " + defaultValue + ")");
            }
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


    /**
     * Gets boolean nullable.
     *
     * @param key the key
     * @return the boolean nullable
     */
    @Nullable
    public static Boolean getBooleanNullable(@NonNull String key) {
        long start = System.currentTimeMillis();
        String param = key;

        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        String _key = Crypt.encrypt(mPassword, key, mIv) + TAIL;
        long iv = sp.getLong(_key, 0);
        key = _key.substring(0, _key.length() - 1);

        if (!sp.contains(key)) {
            if (mLog != LogMode.NONE) {
                Log.e(TAG, "WARNING: Key '" + param + "' not found (return: null)");
            }
            return null;
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
            return null;
        }
    }


    /**
     * Store array.
     *
     * @param key    the key
     * @param values the values
     */
    public static void storeArray(@NonNull String key, @Nullable List<String> values) {
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


    /**
     * Restore array list.
     *
     * @param key the key
     * @return the list
     */
    public static List<String> restoreArray(@NonNull String key) {
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
                    Log.e(TAG, "WARNING: Key '" + key + "_" + i + "' not found (return: " + "new ArrayList<String>(0))");
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


    /**
     * Put.
     *
     * @param stringId the string id
     * @param value    the value
     */
    public static void putRes(@StringRes int stringId, @Nullable String value) {
        put(mCtx.getString(stringId), value);
    }


    /**
     * Get string.
     *
     * @param stringId     the string id
     * @param defaultValue the default value
     * @return the string
     */
    public static String getRes(@StringRes int stringId, @Nullable String defaultValue) {
        return get(mCtx.getString(stringId), defaultValue);
    }


    /**
     * Gets no log.
     *
     * @param stringId     the string id
     * @param defaultValue the default value
     * @return the no log
     */
    public static String getNoLogRes(@StringRes int stringId, @Nullable String defaultValue) {
        return getNoLog(mCtx.getString(stringId), defaultValue);
    }


    /**
     * Gets no log.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the no log
     */
    public static String getNoLog(@NonNull String key, @Nullable String defaultValue) {
        LogMode tmp = mLog;
        mLog = LogMode.NONE;
        String s = get(key, defaultValue);
        mLog = tmp;
        return s;
    }


    /**
     * Put int.
     *
     * @param stringId the string id
     * @param value    the value
     */
    public static void putIntRes(@StringRes int stringId, int value) {
        putInt(mCtx.getString(stringId), value);
    }


    /**
     * Gets int.
     *
     * @param stringId     the string id
     * @param defaultValue the default value
     * @return the int
     */
    public static int getIntRes(@StringRes int stringId, int defaultValue) {
        return getInt(mCtx.getString(stringId), defaultValue);
    }


    /**
     * Gets int no log.
     *
     * @param stringId     the string id
     * @param defaultValue the default value
     * @return the int no log
     */
    public static int getIntNoLogRes(@StringRes int stringId, int defaultValue) {
        return getIntNoLog(mCtx.getString(stringId), defaultValue);
    }


    /**
     * Gets int no log.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the int no log
     */
    public static int getIntNoLog(@NonNull String key, int defaultValue) {
        LogMode tmp = mLog;
        mLog = LogMode.NONE;
        int i = getInt(key, defaultValue);
        mLog = tmp;
        return i;
    }


    /**
     * Put long.
     *
     * @param stringId the string id
     * @param value    the value
     */
    public static void putLongRes(@StringRes int stringId, long value) {
        putLong(mCtx.getString(stringId), value);
    }


    /**
     * Gets long.
     *
     * @param stringId     the string id
     * @param defaultValue the default value
     * @return the long
     */
    public static long getLongRes(@StringRes int stringId, long defaultValue) {
        return getLong(mCtx.getString(stringId), defaultValue);
    }


    /**
     * Gets long no log.
     *
     * @param stringId     the string id
     * @param defaultValue the default value
     * @return the long no log
     */
    public static long getLongNoLogRes(@StringRes int stringId, long defaultValue) {
        return getLongNoLog(mCtx.getString(stringId), defaultValue);
    }


    /**
     * Gets long no log.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the long no log
     */
    public static long getLongNoLog(@NonNull String key, long defaultValue) {
        LogMode tmp = mLog;
        mLog = LogMode.NONE;
        long l = getLong(key, defaultValue);
        mLog = tmp;
        return l;
    }


    /**
     * Put double.
     *
     * @param stringId the string id
     * @param value    the value
     */
    public static void putDoubleRes(@StringRes int stringId, double value) {
        putDouble(mCtx.getString(stringId), value);
    }


    /**
     * Gets double.
     *
     * @param stringId     the string id
     * @param defaultValue the default value
     * @return the double
     */
    public static double getDoubleRes(@StringRes int stringId, double defaultValue) {
        return getDouble(mCtx.getString(stringId), defaultValue);
    }


    /**
     * Gets double no log.
     *
     * @param stringId     the string id
     * @param defaultValue the default value
     * @return the double no log
     */
    public static double getDoubleNoLogRes(@StringRes int stringId, double defaultValue) {
        return getDoubleNoLog(mCtx.getString(stringId), defaultValue);
    }


    /**
     * Gets double no log.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the double no log
     */
    public static double getDoubleNoLog(@NonNull String key, double defaultValue) {
        LogMode tmp = mLog;
        mLog = LogMode.NONE;
        double d = getDouble(key, defaultValue);
        mLog = tmp;
        return d;
    }


    /**
     * Put float.
     *
     * @param stringId the string id
     * @param value    the value
     */
    public static void putFloatRes(@StringRes int stringId, float value) {
        putFloat(mCtx.getString(stringId), value);
    }


    /**
     * Gets float.
     *
     * @param stringId     the string id
     * @param defaultValue the default value
     * @return the float
     */
    public static float getFloatRes(@StringRes int stringId, float defaultValue) {
        return getFloat(mCtx.getString(stringId), defaultValue);
    }


    /**
     * Gets float no log.
     *
     * @param stringId     the string id
     * @param defaultValue the default value
     * @return the float no log
     */
    public static float getFloatNoLogRes(@StringRes int stringId, float defaultValue) {
        return getFloatNoLog(mCtx.getString(stringId), defaultValue);
    }


    /**
     * Gets float no log.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the float no log
     */
    public static float getFloatNoLog(@NonNull String key, float defaultValue) {
        LogMode tmp = mLog;
        mLog = LogMode.NONE;
        float f = getFloat(key, defaultValue);
        mLog = tmp;
        return f;
    }


    /**
     * Put boolean.
     *
     * @param stringId the string id
     * @param value    the value
     */
    public static void putBooleanRes(@StringRes int stringId, boolean value) {
        putBoolean(mCtx.getString(stringId), value);
    }


    /**
     * Gets boolean.
     *
     * @param stringId     the string id
     * @param defaultValue the default value
     * @return the boolean
     */
    public static boolean getBooleanRes(@StringRes int stringId, boolean defaultValue) {
        return getBoolean(mCtx.getString(stringId), defaultValue);
    }


    /**
     * Gets boolean no log.
     *
     * @param stringId     the string id
     * @param defaultValue the default value
     * @return the boolean no log
     */
    public static boolean getBooleanNoLogRes(@StringRes int stringId, boolean defaultValue) {
        return getBooleanNoLog(mCtx.getString(stringId), defaultValue);
    }


    /**
     * Gets boolean no log.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the boolean no log
     */
    public static boolean getBooleanNoLog(@NonNull String key, boolean defaultValue) {
        LogMode tmp = mLog;
        mLog = LogMode.NONE;
        boolean b = getBoolean(key, defaultValue);
        mLog = tmp;
        return b;
    }


    /**
     * Init string.
     *
     * @param key   the key
     * @param value the value
     */
    public static void initString(@NonNull String key, @NonNull String value) {
        LogMode tmp = mLog;
        mLog = LogMode.NONE;
        if (getNullable(key) == null) {
            put(key, value);
        } else {
            Log.w(TAG, "initString failed ('" + key + "' already exists, value=" + (get(key, "")) + ").");
            mLog = tmp;
            return;
        }
        mLog = tmp;
        if (mLog == LogMode.ALL) {
            Log.i(TAG, "initString: '" + key + "' as " + value);
        }
    }


    /**
     * Init string.
     *
     * @param keyId the key id
     * @param value the value
     */
    public static void initStringRes(@StringRes int keyId, @NonNull String value) {
        initString(mCtx.getString(keyId), value);
    }


    /**
     * Init int.
     *
     * @param key   the key
     * @param value the value
     */
    public static void initInt(@NonNull String key, int value) {
        LogMode tmp = mLog;
        mLog = LogMode.NONE;
        if (getIntNullable(key) == null) {
            putInt(key, value);
        } else {
            Log.w(TAG, "initInt failed ('" + key + "' already exists, value=" + (getInt(key, 0)) + ").");
            mLog = tmp;
            return;
        }
        mLog = tmp;
        if (mLog == LogMode.ALL) {
            Log.i(TAG, "initInt: '" + key + "' as " + value);
        }
    }


    /**
     * Init int.
     *
     * @param keyId the key id
     * @param value the value
     */
    public static void initIntRes(@StringRes int keyId, int value) {
        initInt(mCtx.getString(keyId), value);
    }


    /**
     * Init boolean.
     *
     * @param key   the key
     * @param value the value
     */
    public static void initBoolean(@NonNull String key, boolean value) {
        LogMode tmp = mLog;
        mLog = LogMode.NONE;
        if (getBooleanNullable(key) == null) {
            putBoolean(key, value);
        } else {
            Log.w(TAG, "initBoolean failed ('" + key + "' already exists, value=" + (getBoolean(key, false)) + ").");
            mLog = tmp;
            return;
        }
        mLog = tmp;
        if (mLog == LogMode.ALL) {
            Log.i(TAG, "initBoolean: '" + key + "' as " + value);
        }
    }


    /**
     * Init boolean.
     *
     * @param keyId the key id
     * @param value the value
     */
    public static void initBooleanRes(@StringRes int keyId, boolean value) {
        initBoolean(mCtx.getString(keyId), value);
    }


    /**
     * Init float.
     *
     * @param key   the key
     * @param value the value
     */
    public static void initFloat(@NonNull String key, float value) {
        LogMode tmp = mLog;
        mLog = LogMode.NONE;
        if (getFloatNullable(key) == null) {
            putFloat(key, value);
        } else {
            Log.w(TAG, "initFloat failed ('" + key + "' already exists, value=" + (getFloat(key, value)) + ").");
            mLog = tmp;
            return;
        }
        mLog = tmp;
        if (mLog == LogMode.ALL) {
            Log.i(TAG, "initFloat: '" + key + "' as " + value);
        }
    }


    /**
     * Init float.
     *
     * @param keyId the key id
     * @param value the value
     */
    public static void initFloatRes(@StringRes int keyId, float value) {
        initFloat(mCtx.getString(keyId), value);
    }


    /**
     * Init double.
     *
     * @param key   the key
     * @param value the value
     */
    public static void initDouble(@NonNull String key, double value) {
        LogMode tmp = mLog;
        mLog = LogMode.NONE;
        if (getDoubleNullable(key) == null) {
            putDouble(key, value);
        } else {
            Log.w(TAG, "initDouble failed ('" + key + "' already exists, value=" + (getDouble(key, value)) + ").");
            mLog = tmp;
            return;
        }
        mLog = tmp;
        if (mLog == LogMode.ALL) {
            Log.i(TAG, "initDouble: '" + key + "' as " + value);
        }
    }


    /**
     * Init double.
     *
     * @param keyId the key id
     * @param value the value
     */
    public static void initDoubleRes(@StringRes int keyId, double value) {
        initDouble(mCtx.getString(keyId), value);
    }


    /**
     * Init long.
     *
     * @param key   the key
     * @param value the value
     */
    public static void initLong(@NonNull String key, long value) {
        LogMode tmp = mLog;
        mLog = LogMode.NONE;
        if (getLongNullable(key) == null) {
            putLong(key, value);
        } else {
            Log.w(TAG, "initLong failed ('" + key + "' already exists, value=" + (getLong(key, value)) + ").");
            mLog = tmp;
            return;
        }
        mLog = tmp;
        if (mLog == LogMode.ALL) {
            Log.i(TAG, "initLong: '" + key + "' as " + value);
        }
    }


    /**
     * Init long.
     *
     * @param keyId the key id
     * @param value the value
     */
    public static void initLongRes(@StringRes int keyId, long value) {
        initLong(mCtx.getString(keyId), value);
    }


    /**
     * Store array.
     *
     * @param stringId the string id
     * @param values   the values
     */
    public static void storeArrayRes(@StringRes int stringId, List<String> values) {
        storeArray(mCtx.getString(stringId), values);
    }


    /**
     * Restore array list.
     *
     * @param stringId the string id
     * @return the list
     */
    @Nullable
    public static List<String> restoreArrayRes(@StringRes int stringId) {
        return restoreArray(mCtx.getString(stringId));
    }


    /**
     * Gets encrypted content.
     *
     * @return the encrypted content
     */
    @Nullable
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


    /**
     * Count entries int.
     *
     * @return the int
     */
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


    /**
     * Delete all.
     */
    public static void deleteAll() {
        SharedPreferences sp = mCtx.getSharedPreferences(mFilename, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }


    /**
     * Init or increment launch counter.
     */
    public static void initOrIncrementLaunchCounter() {
        LogMode tmp = mLog;
        mLog = LogMode.NONE;
        if (getIntNullable("aes_app_launches") == null) {
            // first launch insert 0
            putInt("aes_app_launches", 0);
        } else {
            putInt("aes_app_launches", (getInt("aes_app_launches", 0) + 1));
        }
        mLog = tmp;
    }


    /**
     * Gets launch counter.
     *
     * @return the launch counter
     */
    public static int getLaunchCounter() {
        return AesPrefs.getInt("aes_app_launches", 0);
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


    /**
     * Init installation date.
     */
    public static void initInstallationDate() {
        LogMode tmp = mLog;
        mLog = LogMode.NONE;
        if (getLongNullable("aes_inst_date") == null) {
            putLong("aes_inst_date", System.currentTimeMillis());
        }
        mLog = tmp;
    }


    /**
     * Gets installation date.
     *
     * @return the installation date
     */
    public static long getInstallationDate() {
        return getLong("aes_inst_date", 0L);
    }


    /**
     * Print installation date.
     */
    public static void printInstallationDate() {
        LogMode tmp = mLog;
        mLog = LogMode.NONE;
        Log.i(TAG, "Installation date: " + TimeFormatUtils.formatTime(getLong("aes_inst_date", 0L), TimeFormatUtils.DEFAULT_FORMAT));
        mLog = tmp;
    }


    /**
     * Reset execution time.
     */
    public static void resetExecutionTime() {
        mDuration = 0L;
    }


    /**
     * Gets execution time.
     *
     * @return the execution time
     */
    public static long getExecutionTime() {
        return mDuration;
    }


    /**
     * Print execution time.
     */
    public static void printExecutionTime() {
        Log.i(TAG, "Execution time: " + String.valueOf(NumberFormatUtils.decimalPlaces((double) mDuration / 1000, 3) + " sec."));
    }


    public static class Version {

        /**
         * Show version info.
         */
        public static void showVersionInfo() {
            Log.i(TAG, "---VERSION-INFO---");
            Log.w(TAG, getVersionInfo());
        }


        /**
         * Gets version name.
         *
         * @return the version name
         */
        public static String getVersionName() {
            return BuildConfig.VERSION_NAME;
        }


        /**
         * Gets version info.
         *
         * @return the version info
         */
        public static String getVersionInfo() {
            return "aesprefs-" + getVersionName() + ".aar";
        }
    }

}