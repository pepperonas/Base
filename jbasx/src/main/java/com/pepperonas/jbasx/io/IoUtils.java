/*
 * Copyright (c) 2016 Martin Pfeffer
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


    /**
     * Read utf 8 string.
     *
     * @param sourcePath the source path
     * @return the string
     */
    public static String readUtf8(String sourcePath) {
        return readUtf8(new File(sourcePath));
    }


    /**
     * Read utf 8 string.
     *
     * @param sourceFile the source file
     * @return the string
     */
    public static String readUtf8(File sourceFile) {
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
     * Read utf 8 in list list.
     *
     * @param sourceFile the source file
     * @return the list
     */
    public static List<String> readUtf8InList(File sourceFile) {
        FileReader reader;
        BufferedReader bufferedReader;

        List<String> lines = new ArrayList<>();

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
                lines.add(line);
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
        return lines;
    }


    /**
     * Read the content of a ISO-8859-1 encoded file.
     *
     * @param sourceFile File with data which should be read.
     * @return the string
     * @throws IOException the io exception
     */
    public static String readIso8859(File sourceFile) throws IOException {
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


    /**
     * Write boolean.
     *
     * @param inputStream the input stream
     * @param destFile    the dest file
     * @return the boolean
     */
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


    /**
     * Write buffered.
     *
     * @param output  the output
     * @param text    the text
     * @param bufSize the buf size
     */
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


    /**
     * Write buffered.
     *
     * @param output  the output
     * @param records the records
     * @param bufSize the buf size
     */
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


    /**
     * Write.
     *
     * @param output the output
     * @param bytes  the bytes
     */
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


    /**
     * To byte array byte [ ].
     *
     * @param file the file
     * @return the byte [ ]
     */
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


    /**
     * Convert file to input stream input stream.
     *
     * @param file the file
     * @return the input stream
     */
    public static InputStream convertFileToInputStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Convert stream to string string.
     *
     * @param inputStream the input stream
     * @return the string
     */
    public static String convertStreamToString(InputStream inputStream) {
        java.util.Scanner s = new java.util.Scanner(inputStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }


    /**
     * Write.
     *
     * @param records the records
     * @param writer  the writer
     */
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
