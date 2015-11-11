package com.pepperonas.jbasx.interfaces;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public interface ThreadListener {

    public void onBaseThreadSuccess(String msg);

    public void onBaseThreadFailed(String msg);

}
