package com.pepperonas.jbasx.base;

import com.pepperonas.jbasx.log.Log;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class SiTest {

    private static final String TAG = "SiTest";


    @Test
    public void testYotta() throws Exception {
        double v = Si.YOTTA;
        Log.d(TAG, "testYotta " + v);
        Assert.assertEquals(1000000000000000000000000d, v);
    }


    @Test
    public void testCenti() throws Exception {
        double v = Si.CENTI;
        Log.d(TAG, "testCenti " + v);
        Assert.assertEquals(0.01d, v);
    }


    @Test
    public void testYocto() throws Exception {
        double v = Si.YOCTO;
        Log.d(TAG, "testYocto " + v);
        Assert.assertEquals(0.000000000000000000000001d, v);
    }

}
