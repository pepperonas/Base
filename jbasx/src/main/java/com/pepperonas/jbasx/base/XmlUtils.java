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

import com.pepperonas.jbasx.Jbasx;
import com.pepperonas.jbasx.log.Log;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class XmlUtils {

    private static final String TAG = "XmlUtils";


    public static String wrap(String key, String value) {
        if (Jbasx.mLog == Jbasx.LogMode.ALL) {
            Log.d(TAG, "wrap: " + "<" + key + ">" + value + "</" + key + ">");
        }
        return "<" + key + ">" + value + "</" + key + ">";
    }


    public static String unwrap(String xmlString) {
        if (Jbasx.mLog == Jbasx.LogMode.ALL) {
            Log.d(TAG, "unwrap: " + xmlString.split(">")[1].split("</")[0]);
        }
        return xmlString.split(">")[1].split("</")[0];
    }


    public static String wrapAll(String key, String... values) {
        String result = "";
        int i = 0;
        for (String s : values) result += wrap(key + String.valueOf(i++), s);
        if (Jbasx.mLog == Jbasx.LogMode.ALL) {
            Log.d(TAG, "wrapAll: " + result);
        }
        return result;
    }


    public static String[] unwrapAll(String xmlString) {
        String[] xmls = xmlString.split("><");
        String[] result = new String[xmls.length];
        int i = 0;
        for (String s : xmls) result[i++] = s.split(">")[1].split("</")[0];

        if (Jbasx.mLog == Jbasx.LogMode.ALL) {
            Log.d(TAG, "unwrapAll: " + result);
        }
        return result;
    }

}
