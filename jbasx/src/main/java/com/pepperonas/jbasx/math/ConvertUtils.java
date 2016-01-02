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

package com.pepperonas.jbasx.math;

import com.pepperonas.jbasx.base.Constants;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class ConvertUtils {

    /**
     * Ms to min long.
     *
     * @param ms the ms
     * @return the long
     */
    public static long msToMin(long ms) {
        return (ms * 1000L * 60L);
    }


    /**
     * Ms to h long.
     *
     * @param ms the ms
     * @return the long
     */
    public static long hToMs(long ms) {
        return (ms * 1000L * 60L * 60L);
    }


    /**
     * S to min long.
     *
     * @param s the s
     * @return the long
     */
    public static long minToS(long s) {
        return (s * 60L);
    }


    /**
     * S to h long.
     *
     * @param s the s
     * @return the long
     */
    public static long hToS(long s) {
        return (s * 60L * 60L);
    }


    /**
     * Ms to kmh int.
     *
     * @param ms the ms
     * @return the int
     */
    public static int msToKmh(float ms) { return (int) (ms * 3.6d); }


    /**
     * Ms to kmh double.
     *
     * @param ms the ms
     * @return the double
     */
    public static double msToKmh(double ms) { return ms * 3.6d; }


    /**
     * Celsius to fahrenheit double.
     *
     * @param celsius the celsius
     * @return the double
     */
    public static double celsiusToFahrenheit(double celsius) {
        return (celsius * 1.8d) + 32.d;
    }


    /**
     * Celsius to kelvin double.
     *
     * @param celsius the celsius
     * @return the double
     */
    public static double celsiusToKelvin(double celsius) {
        return celsius - Constants.KELVIN_ZERO_CELSIUS;
    }


    /**
     * Fahrenheit to celsius double.
     *
     * @param fahrenheit the fahrenheit
     * @return the double
     */
    public static double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32d) / 1.8d;
    }


    /**
     * Fahrenheit to kelvin double.
     *
     * @param fahrenheit the fahrenheit
     * @return the double
     */
    public static double fahrenheitToKelvin(double fahrenheit) {
        return ((fahrenheit - 32d) / 1.8d) - Constants.KELVIN_ZERO_CELSIUS;
    }


    /**
     * Kelvin to celsius double.
     *
     * @param kelvin the kelvin
     * @return the double
     */
    public static double kelvinToCelsius(double kelvin) {
        return kelvin + Constants.KELVIN_ZERO_CELSIUS;
    }


    /**
     * Kelvin to fahrenheit double.
     *
     * @param kelvin the kelvin
     * @return the double
     */
    public static double kelvinToFahrenheit(double kelvin) {
        return ((kelvin + Constants.KELVIN_ZERO_CELSIUS) * 1.8d) + 32.d;
    }
}
