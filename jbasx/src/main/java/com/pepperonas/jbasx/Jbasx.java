package com.pepperonas.jbasx;

import com.pepperonas.jbasx.log.Log;

import java.io.File;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class Jbasx {

    public static final String TAG = "Jbasx";

    private static final String LIBRARY_NAME = "jbasx";

    private static final String VERSION_NAME = "0.1.0";

    private static String mLogFilePath = new File(System.getProperty("user.home")).getPath();
    private static String mLogFileName = "jbasx.log";
    private static boolean mFileLog = false;
    private static boolean mTimestamp = true;
    private static String mUid = "";


    /**
     * Gets unique log id.
     *
     * @return the unique log id
     */
    public static String getUniqueLogId() {
        return mUid;
    }


    /**
     * Write log boolean.
     *
     * @return the boolean
     */
    public static boolean writeLog() {
        return mFileLog;
    }


    /**
     * Write log with stamp boolean.
     *
     * @return the boolean
     */
    public static boolean writeLogWithStamp() {
        return mTimestamp;
    }


    /**
     * Gets log file name.
     *
     * @return the log file name
     */
    public static String getLogFileName() {
        return mLogFileName;
    }


    /**
     * Gets log file path.
     *
     * @return the log file path
     */
    public static String getLogFilePath() {
        return mLogFilePath;
    }


    public enum LogMode {
        NONE(-1), DEFAULT(0), ALL(3);

        private final int mode;


        /**
         * Instantiates a new Log mode.
         *
         * @param i the
         */
        LogMode(int i) {this.mode = i;}
    }


    public static LogMode mLog = LogMode.DEFAULT;


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) { }


    /**
     * Set the log behaviour.
     *
     * @param logMode the log mode
     * @see LogMode
     */
    public static void setLog(LogMode logMode) {
        mLog = logMode;
    }


    /**
     * Write the log to a text file.
     *
     * @param path      where the file should be stored.
     * @param fileName  The filename of the text file.
     * @param timestamp Whenever a timestamp should be set or not.
     */
    public static void setLogWriter(String path, String fileName, boolean timestamp) {
        Jbasx.setLog(Jbasx.LogMode.ALL);
        mLogFilePath = path;
        mLogFileName = fileName;
        mFileLog = true;
        mTimestamp = timestamp;
    }


    /**
     * Sets unique identifier.
     *
     * @param uniqueIdentifier the unique identifier
     */
    public static void setUniqueIdentifier(String uniqueIdentifier) {
        mUid = uniqueIdentifier;
    }


    public static class Version {

        /**
         * Show the current version of the library.
         */
        public static void showVersionInfo() {
            Log.i(TAG, versionInfo());
        }


        /**
         * Gets version info.
         *
         * @return <p>---JBASX---<br> {@value #LIBRARY_NAME}-{@value #VERSION_NAME}</p>
         */
        public static String getVersionInfo() {
            return "---JBASX---\n" +
                   Jbasx.Version.versionInfo();
        }


        /**
         * Version name string.
         *
         * @return '{@value #VERSION_NAME}'
         */
        public static String versionName() { return VERSION_NAME; }


        /**
         * Version info string.
         *
         * @return '{@value #LIBRARY_NAME}-{@value #VERSION_NAME}'
         */
        public static String versionInfo() {
            return (LIBRARY_NAME + "-" + VERSION_NAME + ".jar");
        }


        /**
         * Gets license.
         *
         * @return the license text.
         */
        public static String getLicense() {
            return "Copyright (c) 2015 Martin Pfeffer\n" +
                   " \n" +
                   "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                   "you may not use this file except in compliance with the License.\n" +
                   "You may obtain a copy of the License at\n" +
                   " \n" +
                   "     http://www.apache.org/licenses/LICENSE-2.0\n" +
                   " \n" +
                   "Unless required by applicable law or agreed to in writing, software\n" +
                   "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                   "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                   "See the License for the specific language governing permissions and\n" +
                   "limitations under the License.";
        }
    }

}
