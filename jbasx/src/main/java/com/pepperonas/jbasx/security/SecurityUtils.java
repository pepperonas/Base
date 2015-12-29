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

package com.pepperonas.jbasx.security;

import com.pepperonas.jbasx.base.TextUtils;

import java.security.MessageDigest;
import java.util.Locale;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class SecurityUtils {

    private static final String TAG = "SecurityUtils";


    /**
     * Gets md 5.
     *
     * @param text the text
     * @return the md 5
     */
    public static String getMD5(String text) {
        String md5;
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        MessageDigest md5Digest;
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        char[] charArray = text.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int index = 0; index < charArray.length; index++) {
            byteArray[index] = (byte) charArray[index];
        }

        byte[] md5Bytes = md5Digest.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();

        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }

        md5 = hexValue.toString();
        return md5;
    }


    /**
     * Bytes 2 hex string.
     *
     * @param bytes the bytes
     * @return the string
     */
    public static String bytes2Hex(byte[] bytes) {
        String hs = "";
        String stmp;
        for (byte aByte : bytes) {
            stmp = (Integer.toHexString(aByte & 0XFF));
            if (stmp.length() == 1) {
                hs += "0" + stmp;
            } else {
                hs += stmp;
            }
        }
        return hs.toLowerCase(Locale.ENGLISH);
    }


    /**
     * Gets sha 1.
     *
     * @param text the text
     * @return the sha 1
     */
    public static String getSHA1(String text) {
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        MessageDigest sha1Digest;
        try {
            sha1Digest = MessageDigest.getInstance("SHA-1");
        } catch (Exception e) {
            return null;
        }
        byte[] textBytes = text.getBytes();
        sha1Digest.update(textBytes, 0, text.length());
        byte[] sha1hash = sha1Digest.digest();
        return bytes2Hex(sha1hash);
    }

}
