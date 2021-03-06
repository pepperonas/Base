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

package com.pepperonas.jbasx.concurrency;

import com.pepperonas.jbasx.interfaces.ThreadListener;

import java.util.concurrent.Callable;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class ThreadUtils {

    private static final String TAG = "ThreadUtils";


    public static Void runInBackground(final Callable<Void> callable) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    callable.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return null;
    }


    public static Void calculateStringInBackground(final ThreadListener threadListener, final Callable<String> callable) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    threadListener.onBaseThreadSuccess(callable.call());
                } catch (Exception e) {
                    threadListener.onBaseThreadFailed(e.getMessage());
                }
            }
        }).start();
        return null;
    }

}
