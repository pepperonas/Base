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

package com.pepperonas.jbasx.system;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class SystemUtils {

    /**
     * Gets os.
     *
     * @return the os
     */
    public static String getOs() {
        return System.getProperty("os.name");
    }


    /**
     * Is linux boolean.
     *
     * @return the boolean
     */
    public static boolean isLinux() {
        return getOs().toLowerCase().contains("linux");
    }

}
