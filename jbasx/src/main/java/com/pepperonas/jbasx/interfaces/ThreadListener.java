package com.pepperonas.jbasx.interfaces;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public interface ThreadListener {

    void onBaseThreadSuccess(String msg);

    void onBaseThreadFailed(String msg);

}
