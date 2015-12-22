package com.pepperonas.jbasx.math;

import com.pepperonas.jbasx.base.Constants;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class ConvertUtils {

    public static int msToKmh(float ms) { return (int) (ms * 3.6d); }


    public static double msToKmh(double ms) { return ms * 3.6d; }


    public static double celsiusToFahrenheit(double celsius) {
        return (celsius * 1.8d) + 32.d;
    }


    public static double celsiusToKelvin(double celsius) {
        return celsius - Constants.KELVIN_ZERO_CELSIUS;
    }


    public static double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32d) / 1.8d;
    }


    public static double fahrenheitToKelvin(double fahrenheit) {
        return ((fahrenheit - 32d) / 1.8d) - Constants.KELVIN_ZERO_CELSIUS;
    }


    public static double kelvinToCelsius(double kelvin) {
        return kelvin + Constants.KELVIN_ZERO_CELSIUS;
    }


    public static double kelvinToFahrenheit(double kelvin) {
        return ((kelvin + Constants.KELVIN_ZERO_CELSIUS) * 1.8d) + 32.d;
    }
}
