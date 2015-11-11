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
import com.pepperonas.jbasx.base.TextUtils;
import com.pepperonas.jbasx.log.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Comparator;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class FileUtils {

    private static final String TAG = "FileUtils";

    private static final int IO_BUFFER_SIZE = 16384;


    public static boolean create(String absPath) {
        return create(absPath, false);
    }


    public static boolean create(String absPath, boolean force) {
        if (TextUtils.isEmpty(absPath)) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.w(TAG, "create - Path is empty.");
            }
            return false;
        }

        if (exists(absPath)) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.w(TAG, "create - File exists (will not override).");
            }
            return true;
        }

        String parentPath = getParent(absPath);
        mkDirs(parentPath, force);

        try {
            File file = new File(absPath);
            boolean success = file.createNewFile();
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.i(TAG, "create - File " + (success ? "successfully created." : "not created."));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean mkDirs(String absPath) {
        return mkDirs(absPath, false);
    }


    public static boolean mkDirs(String absPath, boolean force) {
        File file = new File(absPath);
        if (exists(absPath) && !isFolder(absPath)) {
            if (!force) {
                if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                    Log.w(TAG, "mkDirs - Directory exists (will not override).");
                }
                return false;
            } else {
                delete(file);
            }
        }
        try {
            return file.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean exists(String absPath) {
        if (TextUtils.isEmpty(absPath)) {
            return false;
        }
        File file = new File(absPath);
        return exists(file);
    }


    public static boolean exists(File file) {
        return file != null && file.exists();
    }


    public static boolean childOf(String childPath, String parentPath) {
        if (TextUtils.isEmpty(childPath) || TextUtils.isEmpty(parentPath)) {
            return false;
        }
        childPath = cleanPath(childPath);
        parentPath = cleanPath(parentPath);
        return childPath.startsWith(parentPath + File.separator);
    }


    /**
     * Count the number of {@link File} objects inside a directory.
     *
     * @param absPath The path to count the {@link File} objects.
     * @return The number of {@link File} objects.
     */
    public static int childCount(String absPath) {
        if (!exists(absPath)) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.w(TAG, "childCount - Path does not exist.");
            }
            return 0;
        }
        File file = new File(absPath);
        File[] children = file.listFiles();
        if (children == null) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.w(TAG, "childCount: 0");
            }
            return 0;
        }
        if (Jbasx.mLog == Jbasx.LogMode.ALL) {
            Log.i(TAG, "childCount: " + children.length);
        }
        return children.length;
    }


    public static String cleanPath(String absPath) {
        if (TextUtils.isEmpty(absPath)) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.w(TAG, "cleanPath - Path is empty.");
            }
            return absPath;
        }
        try {
            File file = new File(absPath);
            absPath = file.getCanonicalPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Jbasx.mLog == Jbasx.LogMode.ALL) {
            Log.i(TAG, "cleanPath - Path '" + absPath + "' cleaned.");
        }
        return absPath;
    }


    public static long size(String absPath) {
        if (TextUtils.isEmpty(absPath)) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.w(TAG, "cleanPath - Path is empty.");
            }
            return 0;
        }
        File file = new File(absPath);
        return size(file);
    }


    public static long size(File file) {
        if (!exists(file)) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.w(TAG, "size - File does not exist.");
            }
            return 0;
        }

        long length = 0;
        if (isFile(file)) {
            length = file.length();
            return length;
        }

        File files[] = file.listFiles();
        if (files == null || files.length == 0) {
            return length;
        }

        for (File child : files) {
            length += size(child);
        }
        return length;
    }


    public static boolean copy(File sourceFile, String destPath) {
        return copy(sourceFile, destPath, false);
    }


    public static boolean copy(File sourceFile, String destPath, boolean force) {
        if (sourceFile == null) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.w(TAG, "move / copy - Source-file not found.");
            }
            return false;
        }
        if (destPath == null) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.w(TAG, "move / copy - Destination-file not found.");
            }
            return false;
        }

        File destDir = new File(destPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        } else if (!force) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.w(TAG, "move / copy - Directory exists (will not override).");
            }
            return false;
        }

        try {
            FileChannel fcSource, fcOut;
            fcSource = new FileInputStream(sourceFile).getChannel();
            File destFile = new File(destDir.getPath() + "/" + sourceFile.getName());

            if (destFile.exists() && !force) {
                if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                    Log.w(TAG, "move / copy - File exists (will not override).");
                }
                return false;
            }

            destFile.createNewFile();

            fcOut = new FileOutputStream(destFile).getChannel();
            fcOut.transferFrom(fcSource, 0, fcSource.size());
            fcSource.close();
            fcOut.close();
        } catch (IOException e) {
            e.printStackTrace();
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.w(TAG, "move / copy - File not copied.");
            }
            return false;
        }
        if (Jbasx.mLog == Jbasx.LogMode.ALL) {
            Log.i(TAG, "move / copy - File successfully copied.");
        }
        return true;
    }


    public static boolean move(String sourcePath, String destPath) {
        return copy(new File(sourcePath), destPath, false) && delete(sourcePath);
    }


    public static boolean move(File sourceFile, String destPath) {
        return copy(sourceFile, destPath, false) && delete(sourceFile);
    }


    public static boolean move(String sourcePath, String destPath, boolean force) {
        return copy(new File(sourcePath), destPath, force) && delete(sourcePath);
    }


    public static boolean move(File sourceFile, String destPath, boolean force) {
        return copy(sourceFile, destPath, force) && delete(sourceFile);
    }


    public static boolean delete(String absPath) {
        if (TextUtils.isEmpty(absPath)) {
            return false;
        }

        File file = new File(absPath);
        return delete(file);
    }


    public static boolean delete(File file) {
        return exists(file) && file.delete();
    }


    public static boolean deleteIfEmpty(String absPath) {
        if (TextUtils.isEmpty(absPath)) {
            return false;
        }

        File file = new File(absPath);
        return deleteIfEmpty(file);
    }


    public static boolean deleteIfEmpty(File file) {
        if (!exists(file)) {
            return false;
        }

        if (file.isFile() && (file.listFiles().length > 1)) {
            return file.delete();
        } else {
            Log.w(TAG, "deleteIfEmpty - Failed (directory '" + getName(file.getAbsolutePath()) + "' is not empty).");
            return false;
        }
    }


    public static boolean isFile(String absPath) {
        boolean exists = exists(absPath);
        return exists && new File(absPath).isFile();
    }


    public static boolean isFile(File file) {
        return file != null && file.isFile();
    }


    public static boolean isFolder(String absPath) {
        boolean exists = exists(absPath);
        if (!exists) {
            return false;
        }

        File file = new File(absPath);
        return file.isDirectory();
    }


    public static String getName(File file) {
        if (file == null) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.w(TAG, "getName - File not found.");
            }
            return null;
        } else {
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.i(TAG, "getName - File: '" + getName(file.getAbsolutePath()) + "'");
            }
            return getName(file.getAbsolutePath());
        }
    }


    public static String getName(String absPath) {
        if (TextUtils.isEmpty(absPath)) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.w(TAG, "getName - Path is empty.");
            }
            return absPath;
        }

        int index = absPath.lastIndexOf("/");
        if (index > 0 && index < (absPath.length() - 1)) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.i(TAG, "getName - File: '" + absPath.substring(index + 1, absPath.length()) + "'");
            }
            return absPath.substring(index + 1, absPath.length());
        }
        return null;
    }


    public static String getParent(File file) {
        if (file == null) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.w(TAG, "getParent - File not found.");
            }
            return null;
        } else {
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.i(TAG, "getParent - Parent: " + file.getParent());
            }
            return file.getParent();
        }
    }


    public static String getParent(String absPath) {
        if (TextUtils.isEmpty(absPath)) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.w(TAG, "getParent - Path not found.");
            }
            return null;
        }
        absPath = cleanPath(absPath);
        File file = new File(absPath);
        return getParent(file);
    }


    public static String getStem(File file) {
        if (file == null) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.w(TAG, "getStem - File not found.");
            }
            return null;
        }
        if (Jbasx.mLog == Jbasx.LogMode.ALL) {
            Log.i(TAG, "getStem - Stem: " + getStem(file.getName()));
        }
        return getStem(file.getName());
    }


    public static String getStem(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.w(TAG, "getStem - File name is empty.");
            }
            return null;
        }

        int index = fileName.lastIndexOf(".");
        if (index > 0) {
            return fileName.substring(0, index);
        } else {
            return "";
        }
    }


    public static String getExtension(File file) {
        if (file == null) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.w(TAG, "getExtension - File not found.");
            }
            return null;
        }
        if (Jbasx.mLog == Jbasx.LogMode.ALL) {
            Log.i(TAG, "getExtension - Extension: " + getExtension(file.getName()));
        }
        return getExtension(file.getName());
    }


    public static String getExtension(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.w(TAG, "getExtension - File not found.");
            }
            return "";
        }

        int index = fileName.lastIndexOf('.');
        if (index < 0 || index >= (fileName.length() - 1)) {
            return "";
        }
        if (Jbasx.mLog == Jbasx.LogMode.ALL) {
            Log.i(TAG, "getExtension - Extension: " + fileName.substring(index + 1));
        }
        return fileName.substring(index + 1);
    }


    public static boolean matchesExtension(File file, String... extensions) {
        for (String s : extensions) {
            if (file.getName().endsWith(s)) {
                return true;
            }
        }
        return false;
    }


    public static class FolderSorter implements Comparator<File> {

        public int compare(File lhs, File rhs) {
            return lhs.getName().compareTo(rhs.getName());
        }
    }

}