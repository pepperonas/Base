package com.pepperonas.andbasx.interfaces;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public interface LoaderTaskListener {

    public void onLoaderTaskSuccess(String msg);

    public void onLoaderTaskFailed(String msg);

}
