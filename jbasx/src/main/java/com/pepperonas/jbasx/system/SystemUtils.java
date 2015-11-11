package com.pepperonas.jbasx.system;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class SystemUtils {

    public static String getOs() {
        return System.getProperty("os.name");
    }


    public static boolean isLinux() {
        return getOs().toLowerCase().contains("linux");
    }

}
