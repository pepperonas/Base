package com.pepperonas.jbasx.format;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class NumberFormatUtils {

    /**
     * Remove last digits long.
     *
     * @param value           the value
     * @param numbersToRemove the numbers to remove
     * @return the long
     */
    public static long removeLastDigits(long value, int numbersToRemove) {
        return (long) (value / Math.pow(10, numbersToRemove));
    }


    /**
     * Sets a fixed number of digits after the decimal point.
     *
     * @param value     The value which should be cut off.
     * @param precision The number of digits after the decimal point.
     * @return the double
     */
    public static double decimalPlaces(double value, int precision) {
        return (double) Math.round(value * Math.pow(10, precision)) / Math.pow(10, precision);
    }

}
