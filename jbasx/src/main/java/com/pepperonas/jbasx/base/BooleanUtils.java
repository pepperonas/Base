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

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class BooleanUtils {

    private static final String TAG = "BooleanUtils";


    public static boolean stringEquals(CharSequence arg, String... strings) {
        for (String s : strings) if (s.equals(arg)) return true;
        return false;
    }


    public static boolean stringContains(CharSequence arg, String... strings) {
        for (String s : strings) if (s.contains(arg)) return true;
        return false;
    }


    public static boolean stringEqualsIgnoreCase(String arg, String... strings) {
        for (String s : strings) if (s.toLowerCase().equals(arg.toLowerCase())) return true;
        return false;
    }


    public static boolean stringContainsIgnoreCase(String arg, String... strings) {
        for (String s : strings) if (s.toLowerCase().contains(arg.toLowerCase())) return true;
        return false;
    }

}
