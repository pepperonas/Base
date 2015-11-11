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

package com.pepperonas.jbasx.io;

import com.pepperonas.jbasx.format.NumberFormatUtils;

import java.io.File;
import java.text.DecimalFormat;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class FileSizeUtils {

    private static final String TAG = "FileSizeUtils";

    public static final int KB = 0;
    public static final int MB = 1;
    public static final int GB = 2;
    public static final int TB = 3;


    /**
     * Returns the file size in kilobyte.
     *
     * @param fileSize The size of the file {@link File#length()}
     */
    public static long fileSizeInKB(long fileSize) {
        return fileSizeInKB(fileSize, 2);
    }


    /**
     * Returns the file size in kilobyte.
     *
     * @param fileSize The size of the file {@link File#length()}
     */
    public static long fileSizeInKB(long fileSize, int decimalPlaces) {
        return (long) NumberFormatUtils.decimalPlaces((double) fileSize / 1024, decimalPlaces);
    }


    /**
     * Returns the file size in megabyte.
     *
     * @param fileSize The size of the file {@link File#length()}
     */
    public static double fileSizeInMB(double fileSize) {
        return fileSizeInMB(fileSize, 4);
    }


    /**
     * Returns the file size in megabyte.
     *
     * @param fileSize The size of the file {@link File#length()}
     */
    public static double fileSizeInMB(double fileSize, int decimalPlaces) {
        return NumberFormatUtils.decimalPlaces((fileSize / Math.pow(1024, 2)), decimalPlaces);
    }


    /**
     * Returns the file size in gigabyte.
     *
     * @param fileSize The size of the file {@link File#length()}
     */
    public static double fileSizeInGB(double fileSize) {
        return fileSizeInGB(fileSize, 4);
    }


    /**
     * Returns the file size in gigabyte.
     *
     * @param fileSize The size of the file {@link File#length()}
     */
    public static double fileSizeInGB(double fileSize, int decimalPlaces) {
        return NumberFormatUtils.decimalPlaces(fileSize / Math.pow(1024, 3), decimalPlaces);
    }


    /**
     * Returns the file size in terabyte.
     *
     * @param fileSize The size of the file {@link File#length()}
     */
    public static double fileSizeInTB(double fileSize) {
        return fileSizeInTB(fileSize, 4);
    }


    /**
     * Returns the file size in terabyte.
     *
     * @param fileSize The size of the file {@link File#length()}
     */
    public static double fileSizeInTB(double fileSize, int decimalPlaces) {
        return NumberFormatUtils.decimalPlaces(fileSize / Math.pow(1024, 4), decimalPlaces);
    }


    /**
     * Returns the file size.
     * <p/>
     *
     * @param fileSize  The size of the file, also see {@link File#length()}.
     * @param precision The decimal places.
     * @param unit      Use {@link #KB}, {@link #MB} {@link #GB}, or {@link #TB}.
     * @param showUnit  Use {@code true}, or {@code false}.
     */
    public static String formatFileSize(long fileSize, int precision, int unit, boolean showUnit) {
        String s = "#,";
        for (int i = 0; i < precision; i++) s += "#";
        DecimalFormat df = new DecimalFormat(s);
        return applyDecimalFormat(fileSize, unit, df, showUnit);
    }


    /**
     * Returns the file size.
     * <p/>
     *
     * @param fileSize  The size of the file, also see {@link File#length()}.
     * @param precision The decimal places.
     * @param unit      Use {@link #KB}, {@link #MB} {@link #GB}, or {@link #TB}.
     * @param showUnit  Use {@code true}, or {@code false}.
     */
    public static String formatFileSize(double fileSize, int precision, int unit, boolean showUnit) {
        String s = "#,";
        for (int i = 0; i < precision; i++) s += "#";
        DecimalFormat df = new DecimalFormat(s);
        return applyDecimalFormat((long) fileSize, unit, df, showUnit);
    }


    private static String applyDecimalFormat(long fileSize, int unit, DecimalFormat df, boolean b) {
        switch (unit) {
            case MB: return df.format(fileSize / Math.pow(1024, 2)) + (b ? " MB" : "");
            case GB: return df.format(fileSize / Math.pow(1024, 3)) + (b ? " GB" : "");
            case TB: return df.format(fileSize / Math.pow(1024, 4)) + (b ? " TB" : "");
            case KB:
            default: return df.format(fileSize / 1024) + (b ? " KB" : "");
        }
    }

}
