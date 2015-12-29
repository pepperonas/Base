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

package com.pepperonas.jbasx.format;

import com.pepperonas.jbasx.base.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class TimeFormatUtils {

    private static final String TAG = "TimeFormatUtils";


    public enum Format {

        NONE(0), FILE(1), FILE_SHOW_SEC(2), GUI(3);

        int i;


        /**
         * Instantiates a new Format.
         *
         * @param i the
         */
        Format(int i) { this.i = i; }
    }


    public enum Daytime {

        Morning(0), Afternoon(1), Evening(2), Night(3);

        int d;


        @Override
        public String toString() {
            return this.name();
        }


        /**
         * Instantiates a new Daytime.
         *
         * @param d the d
         */
        Daytime(int d) { this.d = d; }
    }


    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_FORMAT_YMD_HMS = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_FORMAT_YMD_HM = "yyyy-MM-dd HH:mm";
    public static final String DEFAULT_FORMAT_MD_HM = "MM-dd HH:mm";

    public static final String LOG_FORMAT = "yyyy.MM.dd HH:mm:ss:SSS";

    public static final String UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";


    /**
     * To time string force show hours string.
     *
     * @param millis      the millis
     * @param showSeconds the show seconds
     * @param showUnits   the show units
     * @return the string
     */
    public static String toTimeStringForceShowHours(long millis, boolean showSeconds, boolean showUnits) {
        int h = (int) (millis / 1000) / 3600;
        int m = (int) (millis / 1000) % 3600 / 60;
        int s = (int) (millis / 1000) % 60;

        if (!showUnits) return showSeconds ? String.format("%02d:%02d:%02d", h, m, s) : String.format("%02d:%02d", h, m);
        else return showSeconds ? String.format("%02dh %02dmin %02ds", h, m, s) : String.format("%02dh %02dmin", h, m);
    }


    /**
     * To time string string.
     *
     * @param millis      the millis
     * @param showSeconds the show seconds
     * @param showUnits   the show units
     * @return the string
     */
    public static String toTimeString(long millis, boolean showSeconds, boolean showUnits) {
        int h = (int) (millis / 1000) / 3600;
        int m = (int) (millis / 1000) % 3600 / 60;
        int s = (int) (millis / 1000) % 60;

        if (h == 0 && m == 0) {
            if (!showUnits) return showSeconds ? String.format("%1d", s) : "0s";
            else return showSeconds ? String.format("%1ds", s) : "0s";
        }

        if (h == 0) {
            if (!showUnits) return showSeconds ? String.format("%02d:%02d", m, s) : String.format("%1d", m);
            else return showSeconds ? String.format("%1dmin %1ds", m, s) : String.format("%1d min", m);
        }

        if (!showUnits) return showSeconds ? String.format("%02d:%02d:%02d", h, m, s) : String.format("%02d:%02d", h, m);
        else return showSeconds ? String.format("%1dh %1dmin %1ds", h, m, s) : String.format("%1dh %1dmin", h, m);
    }


    /**
     * To time string string.
     *
     * @param millis         the millis
     * @param showSeconds    the show seconds
     * @param separatorHours the separator hours
     * @param separatorMin   the separator min
     * @param separatorSec   the separator sec
     * @return the string
     */
    public static String toTimeString(long millis, boolean showSeconds, String separatorHours, String separatorMin, String separatorSec) {
        int h = (int) (millis / 1000) / 3600;
        int m = (int) (millis / 1000) % 3600 / 60;
        int s = (int) (millis / 1000) % 60;

        if (h == 0 && m == 0) {
            if (!showSeconds) return "0min";
            else return "0s";
        }

        if (h == 0) {
            if (separatorMin.equals(":") && !showSeconds) {
                separatorMin = "min";
            }
            return showSeconds ? String.format("%02d" + separatorMin + "%02d" + separatorSec, m, s)
                               : String.format("%02d" + separatorMin, m);
        }

        return showSeconds ? String.format("%02d" + separatorHours + "%02d" + separatorMin + "%02d" + separatorSec, h, m, s)
                           : String.format("%02d" + separatorHours + "%02d" + separatorMin, h, m);
    }


    /**
     * Utc to local string.
     *
     * @param date the date
     * @return the string
     */
    public static String utcToLocal(Date date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(UTC_FORMAT);
            sdf.applyPattern(DEFAULT_FORMAT);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Utc to local string.
     *
     * @param date the date
     * @return the string
     */
    public static String utcToLocal(Date date, Locale locale) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(UTC_FORMAT, locale);
            sdf.applyPattern(DEFAULT_FORMAT);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Format time string.
     *
     * @param time   the time
     * @param format the format
     * @return the string
     */
    public static String formatTime(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date(time);
        return sdf.format(date);
    }


    /**
     * Format time string.
     *
     * @param time   the time
     * @param format the format
     * @return the string
     */
    public static String formatTime(long time, String format, Locale locale) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        Date date = new Date(time);
        return sdf.format(date);
    }


    /**
     * Format time string.
     *
     * @param date   the date
     * @param format the format
     * @return the string
     */
    public static String formatTime(Date date, String format) {
        if (TextUtils.isEmpty(format) || date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }


    /**
     * Format time string.
     *
     * @param date   the date
     * @param format the format
     * @return the string
     */
    public static String formatTime(Date date, String format, Locale locale) {
        if (TextUtils.isEmpty(format) || date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        return sdf.format(date);
    }


    /**
     * Format time string.
     *
     * @param timeStr   the time str
     * @param srcFormat the src format
     * @param dstFormat the dst format
     * @return the string
     */
    public static String formatTime(String timeStr, String srcFormat, String dstFormat) {
        long time = formatTime(timeStr, srcFormat);
        return formatTime(time, dstFormat);
    }


    /**
     * Format time string.
     *
     * @param timeStr   the time str
     * @param srcFormat the src format
     * @param dstFormat the dst format
     * @return the string
     */
    public static String formatTime(String timeStr, String srcFormat, String dstFormat, Locale locale) {
        long time = formatTime(timeStr, srcFormat);
        return formatTime(time, dstFormat, locale);
    }


    /**
     * Format time long.
     *
     * @param time   the time
     * @param format the format
     * @return the long
     */
    public static long formatTime(String time, String format) {
        if (TextUtils.isEmpty(time)) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        long modified = 0;
        try {
            Date date = sdf.parse(time);
            modified = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return modified;
    }


    /**
     * Format time long.
     *
     * @param time   the time
     * @param format the format
     * @return the long
     */
    public static long formatTime(String time, String format, Locale locale) {
        if (TextUtils.isEmpty(time)) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        long modified = 0;
        try {
            Date date = sdf.parse(time);
            modified = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return modified;
    }


    /**
     * Use {@link Format}.
     *
     * @param format The stamp's format.
     * @return The formatted timestamp.
     */
    public static String getTimestamp(Format format) {
        String dt = getStamp();

        String month = dt.substring(4, 6);
        if (month.startsWith("1")) {
            int _month = Integer.parseInt(month);
            _month++;
            month = String.valueOf(_month);
        } else {
            month = month.substring(1);
            int _month = Integer.parseInt(month);
            _month++;
            month = String.format("%02d", _month);
        }
        String divMonth = "", divDateTime = "", divTime = "";

        switch (format) {

            case NONE: divMonth = ""; divDateTime = ""; divTime = "";
                break;

            case FILE: divMonth = "_"; divDateTime = "_"; divTime = "_";
                break;

            case FILE_SHOW_SEC:
                return dt.substring(6, 8) + "_" + month + "_" + dt.substring(0, 4) + "_" +
                       dt.substring(8, 10) + "_" + dt.substring(10, 12) + "_" + dt.substring(12, 14);

            case GUI: divMonth = "."; divDateTime = " - "; divTime = ":";
                break;
        }

        return dt.substring(6, 8) + divMonth + month + divMonth + dt.substring(0, 4) + divDateTime +
               dt.substring(8, 10) + divTime + dt.substring(10, 12);
    }


    /**
     * Gets timestamp lexical.
     *
     * @param showSeconds the show seconds
     * @return the timestamp lexical
     */
    public static String getTimestampLexical(boolean showSeconds) {
        if (showSeconds) return new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        else return new SimpleDateFormat("yyyyMMddhhmm").format(new Date());
    }


    /**
     * Gets timestamp lexical.
     *
     * @param showSeconds the show seconds
     * @return the timestamp lexical
     */
    public static String getTimestampLexical(boolean showSeconds, Locale locale) {
        if (showSeconds) return new SimpleDateFormat("yyyyMMddhhmmss", locale).format(new Date());
        else return new SimpleDateFormat("yyyyMMddhhmm", locale).format(new Date());
    }


    /**
     * Gets timestamp millis.
     *
     * @return the timestamp millis
     */
    public static String getTimestampMillis() {
        return new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date());
    }


    /**
     * Gets timestamp millis.
     *
     * @return the timestamp millis
     */
    public static String getTimestampMillis(Locale locale) {
        return new SimpleDateFormat("yyyyMMddhhmmssSSS", locale).format(new Date());
    }


    /**
     * Gets stamp.
     *
     * @return the stamp
     */
    public static String getStamp() {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        return gregorianCalendar.get(Calendar.YEAR)
               + String.format("%02d", gregorianCalendar.get(Calendar.MONTH))
               + String.format("%02d", gregorianCalendar.get(Calendar.DAY_OF_MONTH))
               + String.format("%02d", gregorianCalendar.get(Calendar.HOUR_OF_DAY))
               + String.format("%02d", gregorianCalendar.get(Calendar.MINUTE))
               + String.format("%02d", gregorianCalendar.get(Calendar.SECOND))
               + String.format("%04d", gregorianCalendar.get(Calendar.MILLISECOND));
    }


    /**
     * Use {@link Daytime}.
     *
     * @return The actual daytime.
     */
    public static Daytime getDaytime() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay >= 6 && timeOfDay < 12) return Daytime.Morning;
        else if (timeOfDay >= 12 && timeOfDay < 18) return Daytime.Afternoon;
        else if (timeOfDay >= 18 && timeOfDay < 22) return Daytime.Evening;
        return Daytime.Night;
    }

}
