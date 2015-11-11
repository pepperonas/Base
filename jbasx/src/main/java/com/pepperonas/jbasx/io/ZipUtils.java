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

package com.pepperonas.jbasx.io;

import com.pepperonas.jbasx.Jbasx;
import com.pepperonas.jbasx.log.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class ZipUtils {

    private static final String TAG = "ZipUtils";

    private final static int BUFFER_SIZE = 8192;


    /**
     * Zip a file.
     *
     * @param filePath The path of the source file.
     * @param zipPath  The path where the zipped file should be stored.
     * @return Whenever the operation was successful.
     */
    public static boolean zip(String filePath, String zipPath) {
        try {
            File file = new File(filePath);
            BufferedInputStream bis;
            FileOutputStream fos = new FileOutputStream(zipPath);
            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos));

            if (file.isDirectory()) {
                int baseLength = file.getParent().length() + 1;
                zipFolder(zos, file, baseLength);
            } else {
                byte data[] = new byte[BUFFER_SIZE];
                FileInputStream fis = new FileInputStream(filePath);
                bis = new BufferedInputStream(fis, BUFFER_SIZE);
                String entryName = file.getName();

                if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                    Log.i(TAG, "Zipping: " + entryName);
                }

                ZipEntry entry = new ZipEntry(entryName);
                zos.putNextEntry(entry);
                int count;
                while ((count = bis.read(data, 0, BUFFER_SIZE)) != -1) {
                    zos.write(data, 0, count);
                }
            }
            zos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * Unzip a file.
     *
     * @param zipPath  The path of the zipped source file.
     * @param unzipDir The directory where the file should be unzipped.
     * @return Whenever the operation was successful.
     */
    public static boolean unzip(String zipPath, String unzipDir) {
        if (!FileUtils.exists(zipPath)) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.e(TAG, "Zip path does not exist!");
            }
            return false;
        }

        if (!FileUtils.mkDirs(unzipDir, true)) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.e(TAG, "Failed to create unzip folder.");
            }
            return false;
        }

        try {
            FileInputStream fin = new FileInputStream(zipPath);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {
                String entryName = ze.getName();
                if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                    Log.d(TAG, "Unzipping: " + entryName);
                }

                String entryPath = unzipDir + File.separator + entryName;
                if (ze.isDirectory()) {
                    FileUtils.mkDirs(entryPath);
                } else {
                    if (!FileUtils.create(entryPath, true)) {
                        continue;
                    }
                    FileOutputStream fos = new FileOutputStream(entryPath);
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int len;
                    while ((len = zin.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                    zin.closeEntry();
                }
            }
            zin.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private static boolean zipFolder(ZipOutputStream zos, File folder, int baseLength) {
        if (zos == null || folder == null) {
            return false;
        }
        File[] fileList = folder.listFiles();

        if (fileList == null || fileList.length == 0) {
            return false;
        }

        for (File file : fileList) {
            if (file.isDirectory()) {
                zipFolder(zos, file, baseLength);
            } else {
                byte data[] = new byte[BUFFER_SIZE];
                String unmodifiedFilePath = file.getPath();
                String realPath = unmodifiedFilePath.substring(baseLength);

                if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                    Log.i(TAG, "Zipping: " + realPath);
                }

                try {
                    FileInputStream fis = new FileInputStream(unmodifiedFilePath);
                    BufferedInputStream bis = new BufferedInputStream(fis, BUFFER_SIZE);
                    ZipEntry entry = new ZipEntry(realPath);
                    zos.putNextEntry(entry);
                    int count;
                    while ((count = bis.read(data, 0, BUFFER_SIZE)) != -1) {
                        zos.write(data, 0, count);
                    }
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }

            }
        }
        return true;
    }

}

