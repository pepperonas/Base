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

package com.pepperonas.jbasx.color;

import com.pepperonas.jbasx.base.StringUtils;
import com.pepperonas.jbasx.base.jUtils;
import com.pepperonas.jbasx.div.MaterialColor;
import com.pepperonas.jbasx.log.Log;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class ColorUtilsTest {

    private static final String TAG = "ColorUtilsTest";


    @Test
    public void testConvertPercentToHexTransparency() throws Exception {
        for (int i = 100; i >= 0; i--) {
            String s = ColorUtils.convertPercentToHexTransparency(i);
            Assert.assertTrue(s.length() <= 2);
            Log.d(TAG, "testConvertPercentToHexTransparency " + StringUtils.padStart(i + "% -> ", 10) + s);
        }
    }


    @Test
    public void testConvertPercentToIntegerHexTransparency() throws Exception {
        for (int i = 0; i <= 100; i++) {
            int value = ColorUtils.convertPercentToIntegerHexTransparency(i);
            Assert.assertTrue(value >= 0 && value <= 255);
            Log.d(TAG, "testConvertPercentToIntegerHexTransparency " + StringUtils.padStart(i + "% -> ", 10) + value);
        }
    }


    @Test
    public void testToInt() throws Exception {
        List<String> stringValues = jUtils._stringValues(MaterialColor.class);
        List<String> fieldNames = jUtils._fields(MaterialColor.class);
        int i = 0;
        for (String s : stringValues) {
            Assert.assertTrue(String.valueOf(ColorUtils.toInt(s)).length() != 0);
            Log.d(TAG, "testToInt  " + StringUtils.padEnd(fieldNames.get(i++), 20) + "(PASSED)");
        }
    }


    @Test
    public void testSetAlphaComponent() throws Exception {
        for (int i = 0; i < 100; i++) {
            Assert.assertTrue(String.valueOf(ColorUtils.setAlphaComponent(0x00ff00, ColorUtils.convertBrightnessToMaxInt(i))).length() != 0);
        }
    }


    @Test
    public void testConvertBrightnessToMaxInt() throws Exception {
        for (int i = -1; i < 102; i++) {
            int value = ColorUtils.convertBrightnessToMaxInt(i);
            Assert.assertTrue(value >= 0 && value <= 255);
            Log.d(TAG, "testConvertPercentToHexTransparency " + StringUtils.padStart(i + "% -> ", 10) + value);
        }
    }


    @Test
    public void testSetBrightness() {
        int value = ColorUtils.setBrightness("0xf57c00", 50);
        Log.d(TAG, "testSetBrightness  " + value);
    }
}