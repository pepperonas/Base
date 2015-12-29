package com.pepperonas.jbasx.system;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class SystemUtils {

    /**
     * Gets os.
     *
     * @return the os
     */
    public static String getOs() {
        return System.getProperty("os.name");
    }


    /**
     * Is linux boolean.
     *
     * @return the boolean
     */
    public static boolean isLinux() {
        return getOs().toLowerCase().contains("linux");
    }

}
