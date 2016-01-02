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
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ZipOutputStream zos = null;

        try {
            File file = new File(filePath);
            FileOutputStream fos = new FileOutputStream(zipPath);
            zos = new ZipOutputStream(new BufferedOutputStream(fos));

            if (file.isDirectory()) {
                int baseLength = file.getParent().length() + 1;
                zipFolder(zos, file, baseLength);
            } else {
                byte data[] = new byte[BUFFER_SIZE];
                fis = new FileInputStream(filePath);
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
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (zos != null) {
                    zos.flush();
                    zos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        FileInputStream fis = null;
        ZipInputStream zin = null;
        FileOutputStream fos = null;

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
            fis = new FileInputStream(zipPath);
            zin = new ZipInputStream(fis);
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
                    fos = new FileOutputStream(entryPath);
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int len;
                    while ((len = zin.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (zin != null) {
                    zin.closeEntry();
                    zin.close();
                }
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    private static boolean zipFolder(ZipOutputStream zos, File folder, int baseLength) {
        FileInputStream fis;
        BufferedInputStream bis = null;

        if (zos == null || folder == null) {
            Log.e(TAG, "zipFolder failed.");
            return false;
        }

        File[] fileList = folder.listFiles();
        if (fileList == null || fileList.length == 0) {
            Log.e(TAG, "zipFolder failed.");
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
                    fis = new FileInputStream(unmodifiedFilePath);
                    bis = new BufferedInputStream(fis, BUFFER_SIZE);
                    ZipEntry entry = new ZipEntry(realPath);
                    zos.putNextEntry(entry);
                    int count;
                    while ((count = bis.read(data, 0, BUFFER_SIZE)) != -1) {
                        zos.write(data, 0, count);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    try {
                        if (bis != null) {
                            bis.close();
                        }
                        zos.flush();
                        zos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

}

