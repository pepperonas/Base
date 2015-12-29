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
