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

package com.pepperonas.jbasx.math;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class CalcUtils {

    private static final String TAG = "CalcUtils";


    public static double percent(double percentageValue, double basicValue) {
        return (percentageValue * (double) 100 / basicValue);
    }


    public static float roundToHalf(float x) {
        return (float) (Math.ceil(x * 2) / 2);
    }


    /**
     * Calculate the distance in meters between two geographical coordinates.
     *
     * @param latA The latitude of point A.
     * @param lonA The longitude of point A.
     * @param latB The latitude of point B.
     * @param lonB The longitude of point B.
     * @return The distance in meters.
     */
    public static double geoCoordToMeter(double latA, double lonA, double latB, double lonB) {
        double earthRadius = 6378.137d; // km
        double dLat = (latB - latA) * Math.PI / 180d;
        double dLon = (lonB - lonA) * Math.PI / 180d;
        double a = Math.sin(dLat / 2d) * Math.sin(dLat / 2d)
                   + Math.cos(latA * Math.PI / 180d)
                     * Math.cos(latB * Math.PI / 180d)
                     * Math.sin(dLon / 2d) * Math.sin(dLon / 2);
        double c = 2d * Math.atan2(Math.sqrt(a), Math.sqrt(1d - a));
        double d = earthRadius * c;
        return (d * 1000d);
    }

}
