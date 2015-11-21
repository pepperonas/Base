/**
 * Copyright 2014 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pepperonas.sample_activityrecognition;

import android.content.Context;
import android.content.res.Resources;

import com.google.android.gms.location.DetectedActivity;
import com.pepperonas.aesprefs.AesPrefs;
import com.pepperonas.jbasx.log.Log;

/**
 * Constants used in this sample.
 */
public final class Constants {

    private static final String TAG = "Constants";


    private Constants() { }


    public static final String PACKAGE_NAME = "com.google.android.gms.location.activityrecognition";

    public static final String BROADCAST_ACTION = PACKAGE_NAME + ".BROADCAST_ACTION";

    public static final String ACTIVITY_EXTRA = PACKAGE_NAME + ".ACTIVITY_EXTRA";

    public static final String SHARED_PREFERENCES_NAME = PACKAGE_NAME + ".SHARED_PREFERENCES";

    public static final String ACTIVITY_UPDATES_REQUESTED_KEY = PACKAGE_NAME + ".ACTIVITY_UPDATES_REQUESTED";

    public static final String DETECTED_ACTIVITIES = PACKAGE_NAME + ".DETECTED_ACTIVITIES";

    /**
     * The desired time between activity detections. Larger values result in fewer activity
     * detections while improving battery life. A value of 0 results in activity detections at the
     * fastest possible rate. Getting frequent updates negatively impact battery life and a real
     * app may prefer to request less frequent updates.
     */
    public static final long DETECTION_INTERVAL_IN_MILLISECONDS = 15 * 1000 * 60;

    /**
     * List of DetectedActivity types that we monitor in this sample.
     */
    protected static final int[] MONITORED_ACTIVITIES = {
            DetectedActivity.STILL,
            DetectedActivity.ON_FOOT,
            DetectedActivity.WALKING,
            DetectedActivity.RUNNING,
            DetectedActivity.ON_BICYCLE,
            DetectedActivity.IN_VEHICLE,
            DetectedActivity.TILTING,
            DetectedActivity.UNKNOWN
    };


    /**
     * Returns a human readable String corresponding to a detected activity type.
     */
    public static String getActivityString(Context context, int detectedActivityType) {
        Resources resources = context.getResources();
        switch (detectedActivityType) {
            case DetectedActivity.IN_VEHICLE:
                log("IN_VEHICLE");
                return resources.getString(R.string.in_vehicle);
            case DetectedActivity.ON_BICYCLE:
                log("ON_BICYCLE");
                return resources.getString(R.string.on_bicycle);
            case DetectedActivity.ON_FOOT:
                log("ON_FOOT");
                return resources.getString(R.string.on_foot);
            case DetectedActivity.RUNNING:
                log("RUNNING");
                return resources.getString(R.string.running);
            case DetectedActivity.STILL:
                log("STILL");
                return resources.getString(R.string.still);
            case DetectedActivity.TILTING:
                log("TILTING");
                return resources.getString(R.string.tilting);
            case DetectedActivity.UNKNOWN:
                log("UNKNOWN");
                return resources.getString(R.string.unknown);
            case DetectedActivity.WALKING:
                log("WALKING");
                return resources.getString(R.string.walking);
            default:
                return resources.getString(R.string.unidentifiable_activity, detectedActivityType);
        }
    }


    private static void log(String which) {
        if (AesPrefs.getInt(which, -1) == -1) {
            AesPrefs.putInt(which, 0);
        } else {
            int tmp = AesPrefs.getInt(which, -1) + 1;
            AesPrefs.putInt(which, tmp);
            Log.d(TAG, "log  " + which + " is now: " + tmp);
        }

    }
}
