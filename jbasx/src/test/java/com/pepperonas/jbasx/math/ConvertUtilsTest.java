package com.pepperonas.jbasx.math;

import com.pepperonas.jbasx.log.Log;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class ConvertUtilsTest {

    private static final String TAG = "ConvertUtilsTest";


    @Test
    public void testMsToKmh() throws Exception {
        int v = ConvertUtils.msToKmh(100f);
        Log.d(TAG, "testMsToKmh " + v);
        Assert.assertEquals(360, v);
    }


    @Test
    public void testMsToKmh_double() throws Exception {
        double v = ConvertUtils.msToKmh(20.33d);
        Log.d(TAG, "testMsToKmh_double " + v);
        Assert.assertEquals(73.188, v);
    }


    @Test
    public void testCelsiusToFahrenheit() throws Exception {
        double v = ConvertUtils.celsiusToFahrenheit(223.874d);
        Log.d(TAG, "testCelsiusToFahrenheit " + v);
        Assert.assertEquals(434.9732d, v, 0.000001d);
    }


    @Test
    public void testFahrenheitToCelsius() throws Exception {
        double v = ConvertUtils.fahrenheitToCelsius(87);
        Log.d(TAG, "testFahrenheitToCelsius " + v);
        Assert.assertEquals(30.555556d, v, 0.000001d);
    }

}
