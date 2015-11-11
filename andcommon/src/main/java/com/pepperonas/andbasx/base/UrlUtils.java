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

package com.pepperonas.andbasx.base;

import android.net.Uri;
import android.text.TextUtils;

import com.pepperonas.andbasx.AndBasx;
import com.pepperonas.jbasx.log.Log;

import java.net.URLEncoder;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class UrlUtils {

    private static final String TAG = "UrlUtils";


    public static String encodeUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            if (AndBasx.mLog == AndBasx.LogMode.ALL || AndBasx.mLog == AndBasx.LogMode.DEFAULT) {
                Log.w(TAG, "encodeUrl - Url was empty, returning " + url);
            }
            return url;
        }

        String encodedUrl = null;
        String[] temp = url.split("/");
        int length = temp.length;
        for (int index = 0; index < length; index++) {
            try {
                temp[index] = URLEncoder.encode(temp[index], "UTF-8");
                temp[index] = temp[index].replace("+", "%20");
            } catch (Exception e) {
                e.printStackTrace();
                return url;
            }
            encodedUrl += temp[index];
            if (index < (length - 1)) {
                encodedUrl += "/";
            }
        }
        if (AndBasx.mLog == AndBasx.LogMode.ALL) {
            Log.d(TAG, "encodeUrl returns: " + encodedUrl);
        }
        return encodedUrl;
    }


    public static Uri parseUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            if (AndBasx.mLog == AndBasx.LogMode.ALL) {
                Log.d(TAG, "parseUrl failed (Url was empty).");
            }
            return null;
        }

        Uri uri = null;
        try {
            uri = Uri.parse(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (AndBasx.mLog == AndBasx.LogMode.ALL) {
            Log.d(TAG, "parseUrl returns: " + uri);
        }
        return uri;
    }


    public static String getParam(String url, String key) {
        Uri uri = parseUrl(url);
        if (uri == null) {
            if (AndBasx.mLog == AndBasx.LogMode.ALL || AndBasx.mLog == AndBasx.LogMode.DEFAULT) {
                Log.d(TAG, "getParam failed.");
            }
            return null;
        }

        String value = null;
        try {
            value = uri.getQueryParameter(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (AndBasx.mLog == AndBasx.LogMode.ALL) {
            Log.d(TAG, "getParam returns: " + value);
        }
        return value;
    }

}
