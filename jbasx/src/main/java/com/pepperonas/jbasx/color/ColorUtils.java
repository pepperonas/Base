package com.pepperonas.jbasx.color;

import com.pepperonas.jbasx.color.android.ColorXs;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class ColorUtils {

    /**
     * Convert brightness to max int int.
     *
     * @param percent the percent
     * @return the int
     */
    public static int convertBrightnessToMaxInt(int percent) {
        if (percent >= 100) return 255;
        else if (percent < 1) return 0;
        return (int) ((float) percent * 2.55f);
    }


    /**
     * Convert percent to hex transparency string.
     *
     * @param _p the p
     * @return the string
     */
    public static String convertPercentToHexTransparency(int _p) {
        String hex = Integer.toHexString((int) Math.round((Math.round((double) _p / 100d * 100) / 100.0d) * 255)).toUpperCase();
        if (hex.length() == 1) hex = "0" + hex;
        return String.valueOf(hex);
    }


    /**
     * Convert percent to integer hex transparency int.
     *
     * @param percent the percent
     * @return the int
     */
    public static int convertPercentToIntegerHexTransparency(int percent) {
        return Integer.parseInt(convertPercentToHexTransparency(percent).toLowerCase(), 16);
    }


    /**
     * Sets alpha component.
     *
     * @param color the color
     * @param alpha the alpha
     * @return the alpha component
     */
    public static int setAlphaComponent(int color, int alpha) {
        if (alpha < 0 || alpha > 255) {
            throw new IllegalArgumentException("alpha must be between 0 and 255.");
        }
        return (color & 0x00ffffff) | (alpha << 24);
    }


    /**
     * Sets brightness.
     *
     * @param color               the color
     * @param brightnessInPercent the brightness in percent
     * @return the brightness
     */
    public static int setBrightness(String color, int brightnessInPercent) {
        return ColorXs.parseColor("#" + ColorUtils.convertPercentToHexTransparency(brightnessInPercent) + color.replace("0x", ""));
    }


    /**
     * Parse {@link com.pepperonas.jbasx.div.MaterialColor} to it's integer representation.
     *
     * @param color The color in {@link String} format (such as '#ffebee').
     * @return The color in it's integer representation.
     */
    public static int toInt(String color) {
        return ColorXs.parseColor(color);
    }


    /**
     * Convert a hex formatted Color value to it's HTML representation.
     *
     * @param color The color in hex format (such as '0xFFEBEE').
     * @return The color in HTML format (such as '#ffebee').
     * @since 0.1.0 "hexToHtml" (previous: "hexIntToHtml").
     */
    public static String hexToHtml(String color) {
        if (color.toLowerCase().contains("0x".toLowerCase())) color = color.replace("0x", "#").toUpperCase();
        return color;
    }


    /**
     * Convert a HTML formatted Color value to it's hex representation.
     *
     * @param color The color in HTML format (such as '#ffebee').
     * @return The color in hex format (such as '0xffebee').
     * @since 0.1.0 "htmlToHex" (previous: "htmlToHexInt")
     */
    public static String htmlToHex(String color) {
        if (color.contains("#")) color = color.replace("#", "0x").toLowerCase();
        return color;
    }


    /**
     * Convert a hex formatted Color value with alpha to it's HTML representation.
     * NOTE: This function will apply 100% opacity to the value.
     *
     * @param color The color in hex format (such as '0xFFEBEEFF').
     * @return The color in HTML format (such as '#ffebee').
     */
    public static String webAlphaToHtml(String color) {
        if (color.contains("0x")) {
            color = color.replace("0x", "#").toLowerCase();
            color = color.substring(0, 7);
        }
        return color;
    }

}
