package com.pepperonas.jbasx.io;

import com.pepperonas.jbasx.Jbasx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class IoUtils {

    private static final String TAG = "IoUtils";


    public static String readFile(String sourcePath) {
        return readFile(new File(sourcePath));
    }


    public static String readFile(File sourceFile) {
        FileReader reader;
        try {
            reader = new FileReader(sourceFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuilder res = new StringBuilder();
        String line;
        try {
            while (((line = bufferedReader.readLine()) != null)) {
                res.append(line).append("\n");
            }
            bufferedReader.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res.toString();
    }


    /**
     * Read the content of a ISO-8859-1 encoded file.
     * Use this if German 'Umlaute' are shown messy.
     *
     * @param sourceFile File with data which should be read.
     */
    public static String readFileIso8859_1(File sourceFile) throws IOException {
        StringBuilder builder = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(sourceFile);
            Charset inputCharset = Charset.forName("ISO-8859-1");
            InputStreamReader isr = new InputStreamReader(fis, inputCharset);
            int i;
            while ((i = isr.read()) != -1) {
                builder.append((char) i);
            }
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }


    /**
     * Store text into a given file.
     *
     * @param file File to store.
     * @param text The text which should be stored.
     * @return Whether the operation was successful or not.
     */
    public static boolean write(File file, String text) {
        if (file == null) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL || Jbasx.mLog == Jbasx.LogMode.DEFAULT) {
                com.pepperonas.jbasx.log.Log.e(TAG, "write - " + "failed (File does not exist).");
            }
            return false;
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
            if (Jbasx.mLog == Jbasx.LogMode.ALL || Jbasx.mLog == Jbasx.LogMode.DEFAULT) {
                com.pepperonas.jbasx.log.Log.e(TAG, "write - " + "failed while writing.");
            }
            return false;
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (Jbasx.mLog == Jbasx.LogMode.ALL || Jbasx.mLog == Jbasx.LogMode.DEFAULT) {
            com.pepperonas.jbasx.log.Log.d(TAG, "write " + "passed.");
        }
        return true;
    }


    public static boolean write(InputStream inputStream, File destFile) {
        if (inputStream == null) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL || Jbasx.mLog == Jbasx.LogMode.DEFAULT) {
                com.pepperonas.jbasx.log.Log.d(TAG, "write - " + "failed (Source is null).");
            }
            return false;
        }
        if (destFile == null) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL || Jbasx.mLog == Jbasx.LogMode.DEFAULT) {
                com.pepperonas.jbasx.log.Log.e(TAG, "write - " + "failed (File does not exist).");
            }
            return false;
        }
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(destFile);
            int read;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (Jbasx.mLog == Jbasx.LogMode.ALL || Jbasx.mLog == Jbasx.LogMode.DEFAULT) {
                com.pepperonas.jbasx.log.Log.e(TAG, "write - " + "failed while writing.");
            }
            return false;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Jbasx.mLog == Jbasx.LogMode.ALL || Jbasx.mLog == Jbasx.LogMode.DEFAULT) {
            com.pepperonas.jbasx.log.Log.d(TAG, "write " + "passed.");
        }
        return true;
    }


    public static void writeBuffered(File output, String text, int bufSize) {
        bufSize = (bufSize == 0 ? 8192 : bufSize);
        try {
            FileWriter writer = new FileWriter(output);
            BufferedWriter bufferedWriter = new BufferedWriter(writer, bufSize);
            List<String> list = new ArrayList<String>();
            list.add(text);
            write(list, bufferedWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void writeBuffered(File output, List<String> records, int bufSize) {
        bufSize = (bufSize == 0 ? 8192 : bufSize);
        try {
            FileWriter writer = new FileWriter(output);
            BufferedWriter bufferedWriter = new BufferedWriter(writer, bufSize);
            write(records, bufferedWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void write(File output, byte[] bytes) {
        try {
            FileOutputStream fos = new FileOutputStream(output.getAbsolutePath());
            fos.write(bytes);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static InputStream toInputStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static byte[] toByteArray(File file) {
        if (file.length() >= Integer.MAX_VALUE) {
            com.pepperonas.jbasx.log.Log.e(TAG, "File too large.");
        }

        FileInputStream fileInputStream;
        byte[] data = new byte[(int) file.length()];
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(data);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    public static String toString(InputStream inputStream) {
        java.util.Scanner s = new java.util.Scanner(inputStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }


    private static void write(List<String> records, Writer writer) {
        try {
            for (String record : records) {
                writer.write(record);
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
