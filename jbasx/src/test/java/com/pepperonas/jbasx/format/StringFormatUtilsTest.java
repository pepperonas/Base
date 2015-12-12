package com.pepperonas.jbasx.format;

import com.pepperonas.jbasx.log.Log;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class StringFormatUtilsTest {

    private static final String TAG = "StringFormatUtilsTest";


    @Test
    public void testFormatDecimal() throws Exception {
        String s = StringFormatUtils.formatDecimal(200.12345678d, 3);
        Log.d(TAG, "testFormatDecimal  " + s);
        Assert.assertEquals("200.123", s);
    }


    @Test
    public void testFormatDecimalForcePrecision() throws Exception {
        String s = StringFormatUtils.formatDecimalForcePrecision(200.123d, 7);
        Log.d(TAG, "testFormatDecimalForcePrecision  " + s);
        Assert.assertEquals("200.1230000", s);
    }


    @Test
    public void testCutText() throws Exception {

    }


    @Test
    public void testIsEmail() throws Exception {
        String email = "pepperonas@gmail.com";
        boolean isEmail = StringFormatUtils.isEmail(email);
        Log.d(TAG, "testIsEmail  " + email + " (" + isEmail + ")");
        Assert.assertEquals(true, isEmail);
    }


    @Test
    public void testIsIp4Address() throws Exception {
        String ip = "192.168.178.53";
        boolean isIp = StringFormatUtils.isIp4Address(ip);
        Log.d(TAG, "testIsIp4Address  " + ip + " (" + isIp + ")");
        Assert.assertEquals(true, isIp);
    }


    @Test
    public void testStringify() throws Exception {

    }


    @Test
    public void testExtractUrls() throws Exception {

    }

}