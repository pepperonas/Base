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