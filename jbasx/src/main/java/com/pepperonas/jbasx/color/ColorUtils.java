package com.pepperonas.jbasx.color;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class ColorUtils {

    /**
     * Parse {@link com.pepperonas.jbasx.div.MaterialColor} to it's integer representation.
     *
     * @param color The color in {@link String} format (such as '#ffebee').
     * @return The color in it's integer representation.
     */
    public static int toInt(String color) {
        return com.pepperonas.jbasx.color.android.ColorXs.parseColor(color);
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
