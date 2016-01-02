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

import com.pepperonas.jbasx.base.TextUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class StringFormatUtils {

    private static final String TAG = "StringFormatUtils";

    public static final String regEmail = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    public static final String regCnChar = "[\u4E00-\u9FFF]+";
    public static final String regIpAddress = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";


    /**
     * Format decimal string.
     *
     * @param value     the value
     * @param precision the precision
     * @return the string
     */
    public static String formatDecimal(double value, int precision) {
        DecimalFormat df = getDecimalFormat(precision, "#");
        return df.format(value);
    }


    /**
     * Format decimal string.
     *
     * @param value     the value
     * @param precision the precision
     * @return the string
     */
    public static String formatDecimal(String value, int precision) {
        DecimalFormat df = getDecimalFormat(precision, "#");
        return df.format(value);
    }


    /**
     * Format decimal force precision string.
     *
     * @param value     the value
     * @param precision the precision
     * @return the string
     */
    public static String formatDecimalForcePrecision(double value, int precision) {
        DecimalFormat df = getDecimalFormat(precision, "0");
        return df.format(value);
    }


    /**
     * Format decimal force precision string.
     *
     * @param value     the value
     * @param precision the precision
     * @return the string
     */
    public static String formatDecimalForcePrecision(String value, int precision) {
        DecimalFormat df = getDecimalFormat(precision, "0");
        return df.format(value);
    }


    /**
     * Cut text string.
     *
     * @param text      the text
     * @param maxLength the max length
     * @return the string
     */
    public static String cutText(String text, int maxLength) {
        if (TextUtils.isEmpty(text) || maxLength < 0) {
            return text;
        }
        int length = text.length();
        if (length > maxLength) {
            return text.substring(0, maxLength);
        }
        return text;
    }


    /**
     * Is email boolean.
     *
     * @param text the text
     * @return the boolean
     */
    public static boolean isEmail(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return false;
        }

        Pattern pattern = Pattern.compile(regEmail);
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }


    /**
     * Is ip 4 address boolean.
     *
     * @param text the text
     * @return the boolean
     */
    public static boolean isIp4Address(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return false;
        }

        Pattern pattern = Pattern.compile(regIpAddress);
        Matcher macher = pattern.matcher(text);
        return macher.matches();
    }


    /**
     * Stringify string.
     *
     * @param t the t
     * @return the string
     */
    public static String stringify(Throwable t) {
        if (t == null) {
            return null;
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }


    /**
     * Extract urls list.
     *
     * @param text the text
     * @return the list
     */
    public static List<String> extractUrls(CharSequence text) {
        List<String> urls = new ArrayList<String>();

        Matcher matcher = urlPattern.matcher(text);
        while (matcher.find()) {
            urls.add(matcher.group());
        }

        return urls;
    }


    /**
     * Gets decimal format.
     *
     * @param precision the precision
     * @param force     the force
     * @return the decimal format
     */
    private static DecimalFormat getDecimalFormat(int precision, String force) {
        String s = force + ".";
        for (int i = 0; i < precision; i++) s += force;
        return new DecimalFormat(s);
    }


    private static Pattern urlPattern = Pattern
            .compile("((?:(http|https|Http|Https):\\/\\/"
                     + "(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|"
                     + "(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_\\.\\"
                     + "+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})"
                     + "?\\@)?)?((?:(?:[a-zA-Z0-9][a-zA-Z0-9\\-\\_]{0,64}\\.)"
                     + "+(?:(?:aero|arpa|asia|a[cdefgilmnoqrstuwxz])"
                     + "|(?:biz|b[abdefghijmnorstvwyz])|(?:cat|com|"
                     + "coop|c[acdfghiklmnoruvxyz])|d[ejkmoz]|(?:edu|e[cegrstu])|f[ijkmor]"
                     + "|(?:gov|g[abdefghilmnpqrstuwy])|h[kmnrtu]|(?:info|int|i[delmnoqrst])"
                     + "|(?:jobs|j[emop])|k[eghimnrwyz]|l[abcikrstuvy]|"
                     + "(?:mil|mobi|museum|m[acdeghklmnopqrstuvwxyz])|"
                     + "(?:name|net|n[acefgilopruz])|(?:org|om)|(?:pro|p[aefghklmnrstwy])|"
                     + "qa|r[eouw]|s[abcdeghijklmnortuvyz]|(?:tel|travel|t[cdfghjklmnoprtvwz])"
                     + "|u[agkmsyz]|v[aceginu]|w[fs]|y[etu]|z[amw]))|(?:(?:25[0-5]|2[0-4][0-9]"
                     + "|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                     + "|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]"
                     + "|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])))"
                     + "(?:\\:\\d{1,5})?)(\\/(?:(?:[a-zA-Z0-9\\;\\/\\?\\:\\@\\&\\=\\#\\~%\\-\\."
                     + "\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?");

}
