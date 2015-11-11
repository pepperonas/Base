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

package com.pepperonas.jbasx.log;

import com.pepperonas.jbasx.Jbasx;
import com.pepperonas.jbasx.format.TimeFormatUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class Log {

    private final static long LOG_FILE_LEN = 4194304; // 4MB


    /**
     * Send a VERBOSE log message.
     *
     * @param tag Used to identify the source of a log message. It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void v(String tag, String msg) {
        System.out.println("V/" + Jbasx.getUniqueLogId() + tag + " - " + msg);
        if (Jbasx.writeLog()) writeLog("V/" + Jbasx.getUniqueLogId(), tag, msg);
    }


    /**
     * Send a INFO log message.
     *
     * @param tag Used to identify the source of a log message. It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void i(String tag, String msg) {
        System.out.println("I/" + Jbasx.getUniqueLogId() + tag + " - " + msg);
        if (Jbasx.writeLog()) writeLog("I/" + Jbasx.getUniqueLogId(), tag, msg);
    }


    /**
     * Send a DEBUG log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void d(String tag, String msg) {
        System.out.println("D/" + Jbasx.getUniqueLogId() + tag + " - " + msg);
        if (Jbasx.writeLog()) writeLog("D/" + Jbasx.getUniqueLogId(), tag, msg);
    }


    /**
     * Send a DEBUG log message.
     *
     * @param tag     Used to identify the source of a log message.  It usually identifies
     *                the class or activity where the log call occurs.
     * @param msg     The message you would like logged.
     * @param strings The list of String objects you would like logged.
     */
    public static void d(String tag, String msg, List<String> strings) {
        int i = 0;
        for (String s : strings) d(tag, msg + " [" + (i++) + "]" + s);
    }


    /**
     * Send a DEBUG log message.
     *
     * @param tag     Used to identify the source of a log message.  It usually identifies
     *                the class or activity where the log call occurs.
     * @param msg     The message you would like logged.
     * @param strings The array of String objects you would like logged.
     */
    public static void d(String tag, String msg, String[] strings) {
        int i = 0;
        for (String s : strings) d(tag, msg + " [" + (i++) + "]" + s);
    }


    /**
     * Send a WARN log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void w(String tag, String msg) {
        System.out.println("W/" + Jbasx.getUniqueLogId() + tag + " - " + msg);
        if (Jbasx.writeLog()) writeLog("W/" + Jbasx.getUniqueLogId(), tag, msg);
    }


    /**
     * Send a WARN log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log.
     */
    public static void w(String tag, String msg, Throwable tr) {
        System.out.println("W/" + Jbasx.getUniqueLogId() + tag + " - " + msg + '\n' + getStackTraceString(tr));
        if (Jbasx.writeLog()) writeLog("W/" + Jbasx.getUniqueLogId(), tag, msg, tr);
    }


    /**
     * Send an ERROR log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void e(String tag, String msg) {
        System.out.println("E/" + Jbasx.getUniqueLogId() + tag + " - " + msg);
        if (Jbasx.writeLog()) writeLog("E/" + Jbasx.getUniqueLogId(), tag, msg);
    }


    /**
     * Send a ERROR log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log.
     */
    public static void e(String tag, String msg, Throwable tr) {
        System.out.println("E/" + Jbasx.getUniqueLogId() + tag + " - " + msg + '\n' + getStackTraceString(tr));
        if (Jbasx.writeLog()) writeLog("E/" + Jbasx.getUniqueLogId(), tag, msg, tr);
    }


    /**
     * Send a What a Terrible Failure log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void wtf(String tag, String msg) {
        System.out.println("WTF/" + Jbasx.getUniqueLogId() + tag + " - " + msg);
        if (Jbasx.writeLog()) writeLog("WTF/" + Jbasx.getUniqueLogId(), tag, msg);
    }


    /**
     * Send a What a Terrible Failure log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log.
     */
    public static void wtf(String tag, String msg, Throwable tr) {
        System.out.println("WTF/" + Jbasx.getUniqueLogId() + tag + " - " + msg + '\n' + getStackTraceString(tr));
        if (Jbasx.writeLog()) writeLog("WTF/" + Jbasx.getUniqueLogId(), tag, msg, tr);
    }


    /**
     * Handy function to get a loggable stack trace from a Throwable.
     *
     * @param tr An exception to log.
     */
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }


    private static void writeLog(final String s, final String tag, final String msg) {
        new Thread() {
            public void run() {
                write2LogFile(s, tag, msg);
            }
        }.start();
    }


    private static void writeLog(String s, String tag, String msg, Throwable tr) {
        write2LogFile(s, tag, msg + '\n' + getStackTraceString(tr));
    }


    private static void write2LogFile(String s, String tag, String msg) {
        String log;
        if (Jbasx.writeLogWithStamp()) {
            log = "[" + TimeFormatUtils.getTimestamp(true) + "] " + s + tag + " -\n" + msg;
        } else {
            log = s + tag + " - " + msg;
        }

        if (com.pepperonas.jbasx.base.TextUtils.isEmpty(Jbasx.getLogFileName())) {
            return;
        }

        String logPath = Jbasx.getLogFilePath() + File.separator + Jbasx.getLogFileName();
        File logFile = new File(logPath);
        if (!logFile.exists()) {
            com.pepperonas.jbasx.io.FileUtils.create(logPath);
        }
        if (logFile.length() >= LOG_FILE_LEN) {
            resizeLogFile(logFile);
        }

        try {
            FileOutputStream fos = new FileOutputStream(logFile, true);
            fos.write(log.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static boolean resizeLogFile(File logFile) {
        boolean success = false;
        BufferedOutputStream bos = null;
        RandomAccessFile raf = null;
        try {
            if (com.pepperonas.jbasx.base.TextUtils.isEmpty(Jbasx.getLogFileName())) {
                return false;
            }
            String tempLog = Jbasx.getLogFilePath() + "/temp.log";
            com.pepperonas.jbasx.io.FileUtils.delete(tempLog);
            if (!com.pepperonas.jbasx.io.FileUtils.create(tempLog)) {
                return false;
            }

            bos = new BufferedOutputStream(new FileOutputStream(tempLog));
            raf = new RandomAccessFile(logFile, "r");
            raf.seek(LOG_FILE_LEN / 2);
            int readLen;
            byte[] readBuff = new byte[1024];
            while ((readLen = raf.read(readBuff)) != -1) {
                bos.write(readBuff, 0, readLen);
            }

            success = logFile.delete();
            com.pepperonas.jbasx.io.FileUtils.move(tempLog, logFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (raf != null) {
                    raf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

}
