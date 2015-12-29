package com.pepperonas.jbasx.format;

import com.pepperonas.jbasx.log.Log;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Date;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class TimeFormatUtilsTest {

    private static final String TAG = "TimeFormatUtilsTest";


    @Test
    public void testUtcToLocal() throws Exception {
        String s = TimeFormatUtils.utcToLocal(new Date(1449919103945L));
        Log.d(TAG, "testUtcToLocal  " + s);
        Assert.assertEquals("2015-12-12 12:18:23", s);
    }


    @Test
    public void testGetTimestampLexical() throws Exception {
        /*will fail if year >= 2100*/
        String s = TimeFormatUtils.getTimestampLexical(true);
        Assert.assertTrue(s.length() == 14 && s.substring(0, 2).equals("20"));
        Log.d(TAG, "testGetTimestampLexical (incl. sec.)    " + s);

        s = TimeFormatUtils.getTimestampLexical(false);
        Assert.assertTrue(s.length() == 12 && s.substring(0, 2).equals("20"));
        Log.d(TAG, "testGetTimestampLexical (no sec.)       " + s);
    }


    @Test
    public void testGetTimestamp() throws Exception {
        /*will fail if year >= 2100*/
        String s = TimeFormatUtils.getTimestamp(TimeFormatUtils.Format.NONE);
        Assert.assertTrue(s.length() > 10 && s.substring(4, 6).equals("20"));
        Log.d(TAG, "testGetTimestamp (NONE)          " + s);

        s = TimeFormatUtils.getTimestamp(TimeFormatUtils.Format.FILE_SHOW_SEC);
        Assert.assertTrue(s.length() > 10 && s.substring(6, 8).equals("20"));
        Log.d(TAG, "testGetTimestamp (FILE_SHOW_SEC) " + s);

        s = TimeFormatUtils.getTimestamp(TimeFormatUtils.Format.GUI);
        Assert.assertTrue(s.length() > 10 && s.substring(6, 8).equals("20"));
        Log.d(TAG, "testGetTimestamp (GUI)           " + s);
    }


    @Test
    public void testGetTimestampMillis() throws Exception {
        /*will fail if year >= 2100*/
        String s = TimeFormatUtils.getTimestampMillis();
        Assert.assertTrue(s.length() == 17 && s.substring(0, 2).equals("20"));
        Log.d(TAG, "testGetTimestampMillis  " + s);
    }


    @Test
    public void testGetDaytime() throws Exception {
        TimeFormatUtils.Daytime daytime = TimeFormatUtils.getDaytime();
        System.out.println(daytime);
        Assert.assertTrue(daytime.toString().equals("Morning")
                          || daytime.toString().equals("Afternoon")
                          || daytime.toString().equals("Evening")
                          || daytime.toString().equals("Night"));
    }


    @Test
    public void testToTimeString() throws Exception {
        String timeString = TimeFormatUtils.toTimeString(60L * 60L * 1000L, false, false);
        System.out.println(timeString);

        timeString = TimeFormatUtils.toTimeString(60L * 60L * 1000L, false, true);
        System.out.println(timeString);

        timeString = TimeFormatUtils.toTimeString(60L * 60L * 100L, false, false);
        System.out.println(timeString);

        timeString = TimeFormatUtils.toTimeString(60L * 60L * 10L, false, false);
        System.out.println(timeString);

        timeString = TimeFormatUtils.toTimeString(60L * 60L * 10L, true, false);
        System.out.println(timeString);

        timeString = TimeFormatUtils.toTimeString(60L * 60L * 10L, true, true);
        System.out.println(timeString);

    }
}