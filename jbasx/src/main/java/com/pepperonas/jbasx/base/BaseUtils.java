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

package com.pepperonas.jbasx.base;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class BaseUtils {

    private static final String TAG = "BaseUtils";

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);


    /**
     * Convert to hex int.
     *
     * @param n the n
     * @return the int
     */
    public static int convertToHex(int n) {
        return Integer.valueOf(String.valueOf(n), 16);
    }


    /**
     * Generate a random integer. The parameters specify the range,
     * in which the values are generated.
     *
     * @param min The minimum value the function may return.
     * @param max The maximum value the function may return.
     * @return A random integer.
     */
    public static int randomInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }


    /**
     * Timestamp ms long.
     *
     * @return the long
     */
    public static long tsMs() {
        return System.currentTimeMillis();
    }


    /**
     * Timestamp ns long.
     *
     * @return The timestamp in nanoseconds.
     */
    public static long tsNs() {
        return System.nanoTime();
    }


    /**
     * Generate unique id int.
     *
     * @return the int
     */
    public static int generateUniqueId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1;
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

}
