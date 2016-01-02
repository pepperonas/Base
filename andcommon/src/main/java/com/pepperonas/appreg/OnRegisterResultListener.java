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

package com.pepperonas.appreg;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public interface OnRegisterResultListener {

    /**
     * On user registered.
     *
     * @param message the message
     */
    void onUserRegistered(@NonNull String message);

    /**
     * On user exists.
     *
     * @param message          the message
     * @param registrationDate the registration date
     * @param extraString      the extra string
     * @param extraInt         the extra int
     */
    void onUserExists(@NonNull String message, @Nullable Long registrationDate, @Nullable String extraString, @Nullable Integer extraInt);

    /**
     * On failed.
     *
     * @param status     the status
     * @param httpStatus the http status
     * @param message    the message
     */
    void onFailed(@NonNull AppRegistry.StatusCode status, int httpStatus, @Nullable String message);

}
