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

package com.pepperonas.jbasx.base;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class TextUtils {

    /**
     * Ensure the content of a {@link CharSequence}.
     *
     * @param charSequence the char sequence
     * @return Whenever the {@link CharSequence} is empty or not.
     */
    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }


    /**
     * Is secret field boolean.
     *
     * @param string the string
     * @return the boolean
     */
    public static boolean isSecretField(String string) {
        return string.toLowerCase().contains("passwor")
               || string.toLowerCase().contains("pin")
               || string.toLowerCase().contains("key")
               || string.toLowerCase().contains("code");
    }

}
