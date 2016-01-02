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

package com.pepperonas.jbasx.format;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class NumberFormatUtils {

    /**
     * Remove last digits long.
     *
     * @param value           the value
     * @param numbersToRemove the numbers to remove
     * @return the long
     */
    public static long removeLastDigits(long value, int numbersToRemove) {
        return (long) (value / Math.pow(10, numbersToRemove));
    }


    /**
     * Sets a fixed number of digits after the decimal point.
     *
     * @param value     The value which should be cut off.
     * @param precision The number of digits after the decimal point.
     * @return the double
     */
    public static double decimalPlaces(double value, int precision) {
        return (double) Math.round(value * Math.pow(10, precision)) / Math.pow(10, precision);
    }

}
