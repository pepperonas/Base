package com.pepperonas.jbasx.interfaces;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public interface ThreadListener {

    /**
     * On base thread success.
     *
     * @param message the message
     */
    void onBaseThreadSuccess(String message);

    /**
     * On base thread failed.
     *
     * @param message the message
     */
    void onBaseThreadFailed(String message);

}
