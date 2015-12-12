package com.pepperonas.jbasx.io;

import com.pepperonas.jbasx.log.Log;

import org.junit.Test;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class FileSizeUtilsTest {

    private static final String TAG = "FileSizeUtilsTest";


    @Test
    public void testFileSize() throws Exception {
        Log.d(TAG, "testFileSize (KB) " + FileSizeUtils.fileSizeInKB(234123451, 0));
    }


    @Test
    public void testFileSizeShowUnit() throws Exception {
        Log.d(TAG, "testFileSizeShowUnit (KB)  " + FileSizeUtils.formatFileSize(234123451d, 4, FileSizeUtils.KB, true));
    }

}