package com.pepperonas.jbasx.io;

import com.pepperonas.jbasx.Jbasx;
import com.pepperonas.jbasx.log.Log;

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
        BufferedReader bufferedReader;

        StringBuilder builder = new StringBuilder();

        try {
            reader = new FileReader(sourceFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        bufferedReader = new BufferedReader(reader);
        String line;
        try {
            while (((line = bufferedReader.readLine()) != null)) {
                builder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }


    /**
     * Read the content of a ISO-8859-1 encoded file.
     * Use this if German 'Umlaute' are shown messy.
     *
     * @param sourceFile File with data which should be read.
     */
    public static String readFileIso8859_1(File sourceFile) throws IOException {
        FileInputStream fis = null;
        InputStreamReader isr = null;

        StringBuilder builder = new StringBuilder();

        try {
            fis = new FileInputStream(sourceFile);
            Charset inputCharset = Charset.forName("ISO-8859-1");
            isr = new InputStreamReader(fis, inputCharset);
            int i;
            while ((i = isr.read()) != -1) {
                builder.append((char) i);
            }
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) fis.close();
            if (isr != null) isr.close();
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
        BufferedWriter writer = null;

        if (file == null) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL || Jbasx.mLog == Jbasx.LogMode.DEFAULT) {
                Log.e(TAG, "write - " + "failed (File does not exist).");
            }
            return false;
        }

        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
            if (Jbasx.mLog == Jbasx.LogMode.ALL || Jbasx.mLog == Jbasx.LogMode.DEFAULT) {
                Log.e(TAG, "write - " + "failed while writing.");
            }
            return false;
        } finally {
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (Jbasx.mLog == Jbasx.LogMode.ALL || Jbasx.mLog == Jbasx.LogMode.DEFAULT) {
            Log.d(TAG, "write " + "passed.");
        }
        return true;
    }


    public static boolean write(InputStream inputStream, File destFile) {
        OutputStream outputStream = null;

        if (inputStream == null) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL || Jbasx.mLog == Jbasx.LogMode.DEFAULT) {
                Log.d(TAG, "write - " + "failed (Source is null).");
            }
            return false;
        }

        if (destFile == null) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL || Jbasx.mLog == Jbasx.LogMode.DEFAULT) {
                Log.e(TAG, "write - " + "failed (File does not exist).");
            }
            return false;
        }

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
                Log.e(TAG, "write - " + "failed while writing.");
            }
            return false;
        } finally {
            try {
                inputStream.close();
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (Jbasx.mLog == Jbasx.LogMode.ALL || Jbasx.mLog == Jbasx.LogMode.DEFAULT) {
            Log.d(TAG, "write " + "passed.");
        }
        return true;
    }


    public static void writeBuffered(File output, String text, int bufSize) {
        FileWriter writer = null;
        BufferedWriter bufferedWriter = null;

        bufSize = (bufSize == 0 ? 8192 : bufSize);

        try {
            writer = new FileWriter(output);
            bufferedWriter = new BufferedWriter(writer, bufSize);
            List<String> list = new ArrayList<String>();
            list.add(text);
            write(list, bufferedWriter);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void writeBuffered(File output, List<String> records, int bufSize) {
        FileWriter writer = null;
        BufferedWriter bufferedWriter = null;

        bufSize = (bufSize == 0 ? 8192 : bufSize);

        try {
            writer = new FileWriter(output);
            bufferedWriter = new BufferedWriter(writer, bufSize);
            write(records, bufferedWriter);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void write(File output, byte[] bytes) {
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(output.getAbsolutePath());
            fos.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static byte[] toByteArray(File file) {
        FileInputStream fis = null;

        if (file.length() >= Integer.MAX_VALUE) {
            Log.e(TAG, "File too large.");
            return null;
        }

        byte[] data = new byte[(int) file.length()];
        try {
            fis = new FileInputStream(file);
            fis.read(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }


    public static InputStream convertFileToInputStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String convertStreamToString(InputStream inputStream) {
        java.util.Scanner s = new java.util.Scanner(inputStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }


    private static void write(List<String> records, Writer writer) {
        try {
            for (String record : records) {
                writer.write(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
