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

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class CalculationUtils {

    private static final String TAG = "CalculationUtils";


    /**
     * Percent double.
     *
     * @param percentageValue the percentage value
     * @param basicValue      the basic value
     * @return the double
     */
    public static double percent(double percentageValue, double basicValue) {
        return (percentageValue * (double) 100 / basicValue);
    }


    /**
     * Round to half float.
     *
     * @param x the x
     * @return the float
     */
    public static float roundToHalf(float x) {
        return (float) (Math.ceil(x * 2) / 2);
    }


    /**
     * Calculate the distance in meters between two geographical coordinates.
     *
     * @param latA The latitude of point A.
     * @param lngA The longitude of point A.
     * @param latB The latitude of point B.
     * @param lngB The longitude of point B.
     * @return The distance in meters.
     */
    public static double distanceBetween2GeoPositionsInMeters(double latA, double lngA, double latB, double lngB) {
        double earthRadius = 6371;
        double dLat = Math.toRadians(latB - latA);
        double dLng = Math.toRadians(lngB - lngA);
        double a = Math.sin(dLat / 2)
                   * Math.sin(dLat / 2)
                   + Math.cos(Math.toRadians(latA))
                     * Math.cos(Math.toRadians(latB))
                     * Math.sin(dLng / 2)
                     * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (int) (earthRadius * c * 1000);
    }

}
