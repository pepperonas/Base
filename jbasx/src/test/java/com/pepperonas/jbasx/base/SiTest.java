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
