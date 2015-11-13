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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class TimeFormatUtils {

    private static final String TAG = "TimeFormatUtils";


    public enum Format {

        NONE(0), FILE(1), FILE_SHOW_SEC(2), GUI(3);

        int i;


        Format(int i) { this.i = i; }
    }


    public enum Daytime {

        MORNING(0), AFTERNOON(1), EVENING(2), NIGHT(3);

        int d;


        Daytime(int d) { this.d = d; }
    }


    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";


    public static String utcToLocal(String utcTime) {
        String localTime = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(UTC_FORMAT);
            Date date = sdf.parse(utcTime);
            sdf.applyPattern(DEFAULT_FORMAT);
            localTime = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localTime;
    }


    public static String formatTime(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date(time);
        return sdf.format(date);
    }


    public static String formatTime(Date date, String format) {
        if (com.pepperonas.jbasx.base.TextUtils.isEmpty(format) || date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }


    public static String formatTime(String timeStr, String srcFormat, String dstFormat) {
        long time = formatTime(timeStr, srcFormat);
        return formatTime(time, dstFormat);
    }


    public static long formatTime(String time, String format) {
        if (com.pepperonas.jbasx.base.TextUtils.isEmpty(time)) {
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
     * Use {@link Format}.
     *
     * @return The formatted timestamp.
     */
    public static String stamp(Format format) {
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


    public static String getTimestamp(boolean showSeconds) {
        if (showSeconds) return new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        else return new SimpleDateFormat("yyyyMMddhhmm").format(new Date());
    }


    public static String getTimestampMillis() {
        return new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date());
    }


    private static String getStamp() {
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
        if (timeOfDay >= 6 && timeOfDay < 12) return Daytime.MORNING;
        else if (timeOfDay >= 12 && timeOfDay < 18) return Daytime.AFTERNOON;
        else if (timeOfDay >= 18 && timeOfDay < 22) return Daytime.EVENING;
        return Daytime.NIGHT;
    }

}
