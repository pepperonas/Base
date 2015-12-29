package com.pepperonas.jbasx.base;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class StringUtils {

    /**
     * Splits a String based on a single character, which is usually faster than regex-based String.split().
     *
     * @param string    the string
     * @param delimiter the delimiter
     * @return the string [ ]
     */
    public static String[] fastSplit(String string, char delimiter) {
        List<String> list = new ArrayList<String>();
        int size = string.length();
        int start = 0;
        for (int i = 0; i < size; i++) {
            if (string.charAt(i) == delimiter) {
                if (start < i) {
                    list.add(string.substring(start, i));
                } else {
                    list.add("");
                }
                start = i + 1;
            } else if (i == size - 1) {
                list.add(string.substring(start, size));
            }
        }
        String[] elements = new String[list.size()];
        list.toArray(elements);
        return elements;
    }


    /**
     * URL-Encodes a given string using UTF-8 (some web pages have problems with UTF-8 and umlauts, consider
     * {@link #encodeUrlIso(String)} also). No UnsupportedEncodingException to handle as it is dealt with in this
     * method.
     *
     * @param stringToEncode the string to encode
     * @return the string
     */
    public static String encodeUrl(String stringToEncode) {
        try {
            return URLEncoder.encode(stringToEncode, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            throw new RuntimeException(e1);
        }
    }


    /**
     * URL-encodes a given string using ISO-8859-1, which may work better with web pages and umlauts compared to UTF-8.
     * No UnsupportedEncodingException to handle as it is dealt with in this method.
     *
     * @param stringToEncode the string to encode
     * @return the string
     */
    public static String encodeUrlIso(String stringToEncode) {
        try {
            return URLEncoder.encode(stringToEncode, "ISO-8859-1");
        } catch (UnsupportedEncodingException e1) {
            throw new RuntimeException(e1);
        }
    }


    /**
     * URL-Decodes a given string using UTF-8. No UnsupportedEncodingException to handle as it is dealt with in this
     * method.
     *
     * @param stringToDecode the string to decode
     * @return the string
     */
    public static String decodeUrl(String stringToDecode) {
        try {
            return URLDecoder.decode(stringToDecode, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            throw new RuntimeException(e1);
        }
    }


    /**
     * URL-Decodes a given string using ISO-8859-1. No UnsupportedEncodingException to handle as it is dealt with in
     * this method.
     *
     * @param stringToDecode the string to decode
     * @return the string
     */
    public static String decodeUrlIso(String stringToDecode) {
        try {
            return URLDecoder.decode(stringToDecode, "ISO-8859-1");
        } catch (UnsupportedEncodingException e1) {
            throw new RuntimeException(e1);
        }
    }


    /**
     * Generates the MD5 digest for a given String based on UTF-8. The digest is padded with zeroes in the front if
     * necessary.
     *
     * @param stringToEncode the string to encode
     * @return MD5 digest (32 characters).
     */
    public static String generateMD5String(String stringToEncode) {
        return generateDigestString(stringToEncode, "MD5", "UTF-8", 32);
    }


    /**
     * Generates the SHA-1 digest for a given String based on UTF-8. The digest is padded with zeroes in the front if
     * necessary. The SHA-1 algorithm is considers to produce less collisions than MD5.
     *
     * @param stringToEncode the string to encode
     * @return SHA -1 digest (40 characters).
     */
    public static String generateSHA1String(String stringToEncode) {
        return generateDigestString(stringToEncode, "SHA-1", "UTF-8", 40);
    }


    /**
     * Generate digest string string.
     *
     * @param stringToEncode the string to encode
     * @param digestAlgo     the digest algo
     * @param encoding       the encoding
     * @param lengthToPad    the length to pad
     * @return the string
     */
    public static String
    generateDigestString(String stringToEncode, String digestAlgo, String encoding, int lengthToPad) {
        // Loosely inspired by http://workbench.cadenhead.org/news/1428/creating-md5-hashed-passwords-java
        MessageDigest digester;
        try {
            digester = MessageDigest.getInstance(digestAlgo);
        } catch (NoSuchAlgorithmException nsae) {
            throw new RuntimeException(nsae);
        }
        try {
            digester.update(stringToEncode.getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return toHexString(digester.digest(), lengthToPad);
    }


    /**
     * To hex string string.
     *
     * @param bytes       the bytes
     * @param lengthToPad the length to pad
     * @return the string
     */
    public static String toHexString(byte[] bytes, int lengthToPad) {
        BigInteger hash = new BigInteger(1, bytes);
        String digest = hash.toString(16);

        while (digest.length() < lengthToPad) {
            digest = "0" + digest;
        }
        return digest;
    }


    /**
     * Simple HTML/XML entity resolving: Only supports unicode enitities and a very limited number text represented
     * entities (apos, quot, gt, lt, and amp). There are many more: http://www.w3.org/TR/REC-html40/sgml/dtd.html
     *
     * @param entity The entity name without & and ; (null throws NPE)
     * @return Resolved entity or the entity itself if it could not be resolved.
     */
    public static String resolveEntity(String entity) {
        if (entity.length() > 1 && entity.charAt(0) == '#') {
            if (entity.charAt(1) == 'x') {
                return String.valueOf((char) Integer.parseInt(entity.substring(2), 16));
            } else {
                return String.valueOf((char) Integer.parseInt(entity.substring(1)));
            }
        } else if (entity.equals("apos")) {
            return "'";
        } else if (entity.equals("quot")) {
            return "\"";
        } else if (entity.equals("gt")) {
            return ">";
        } else if (entity.equals("lt")) {
            return "<";
        } else if (entity.equals("amp")) {
            return "&";
        } else {
            return entity;
        }
    }


    /**
     * Cuts the string at the end if it's longer than maxLength and appends "..." to it. The length of the resulting
     * string including "..." is always less or equal to the given maxLength. It's valid to pass a null text; in this
     * case null is returned.
     *
     * @param text      the text
     * @param maxLength the max length
     * @return the string
     */
    public static String ellipsize(String text, int maxLength) {
        if (text != null && text.length() > maxLength) {
            return text.substring(0, maxLength - 3) + "...";
        }
        return text;
    }


    /**
     * Split lines string [ ].
     *
     * @param text           the text
     * @param skipEmptyLines the skip empty lines
     * @return the string [ ]
     */
    public static String[] splitLines(String text, boolean skipEmptyLines) {
        if (skipEmptyLines) {
            return text.split("[\n\r]+");
        } else {
            return text.split("\\r?\\n");
        }
    }


    /**
     * Find lines containing list.
     *
     * @param text       the text
     * @param searchText the search text
     * @return the list
     */
    public static List<String> findLinesContaining(String text, String searchText) {
        String[] splitLinesSkipEmpty = splitLines(text, true);
        List<String> matching = new ArrayList<String>();
        for (String line : splitLinesSkipEmpty) {
            if (line.contains(searchText)) {
                matching.add(line);
            }
        }
        return matching;
    }


    /**
     * Returns a concatenated string consisting of the given lines seperated by a new line character \n. The last line
     * does not have a \n at the end.
     *
     * @param lines the lines
     * @return the string
     */
    public static String concatLines(List<String> lines) {
        StringBuilder builder = new StringBuilder();
        int countMinus1 = lines.size() - 1;
        for (int i = 0; i < countMinus1; i++) {
            builder.append(lines.get(i)).append('\n');
        }
        if (!lines.isEmpty()) {
            builder.append(lines.get(countMinus1));
        }
        return builder.toString();
    }


    /**
     * Join iterable on comma string.
     *
     * @param iterable the iterable
     * @return the string
     */
    public static String joinIterableOnComma(Iterable<?> iterable) {
        if (iterable != null) {

            StringBuilder buf = new StringBuilder();
            Iterator<?> it = iterable.iterator();
            while (it.hasNext()) {
                buf.append(it.next());
                if (it.hasNext()) {
                    buf.append(',');
                }
            }
            return buf.toString();
        } else {
            return "";
        }
    }


    /**
     * Join array on comma string.
     *
     * @param array the array
     * @return the string
     */
    public static String joinArrayOnComma(int[] array) {
        if (array != null) {
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < array.length; i++) {
                if (i != 0) {
                    buf.append(',');
                }
                buf.append(array[i]);
            }
            return buf.toString();
        } else {
            return "";
        }

    }


    /**
     * Join array on comma string.
     *
     * @param array the array
     * @return the string
     */
    public static String joinArrayOnComma(String[] array) {
        if (array != null) {
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < array.length; i++) {
                if (i != 0) {
                    buf.append(',');
                }
                buf.append(array[i]);
            }
            return buf.toString();
        } else {
            return "";
        }
    }


    /**
     * Pad start string.
     *
     * @param string    the string
     * @param minLength the min length
     * @return the string
     */
    public static String padStart(String string, int minLength) {
        return padStart(string, minLength, ' ');
    }


    /**
     * Pad start string.
     *
     * @param string    the string
     * @param minLength the min length
     * @param padChar   the pad char
     * @return the string
     */
    public static String padStart(String string, int minLength, char padChar) {
        if (string.length() >= minLength) {
            return string;
        }
        StringBuilder sb = new StringBuilder(minLength);
        for (int i = string.length(); i < minLength; i++) {
            sb.append(padChar);
        }
        sb.append(string);
        return sb.toString();
    }


    /**
     * Pad end string.
     *
     * @param string    the string
     * @param minLength the min length
     * @return the string
     */
    public static String padEnd(String string, int minLength) {
        return padEnd(string, minLength, ' ');
    }


    /**
     * Pad end string.
     *
     * @param string    the string
     * @param minLength the min length
     * @param padChar   the pad char
     * @return the string
     */
    public static String padEnd(String string, int minLength, char padChar) {
        if (string.length() >= minLength) {
            return string;
        }
        StringBuilder sb = new StringBuilder(minLength);
        sb.append(string);
        for (int i = string.length(); i < minLength; i++) {
            sb.append(padChar);
        }
        return sb.toString();
    }
}
