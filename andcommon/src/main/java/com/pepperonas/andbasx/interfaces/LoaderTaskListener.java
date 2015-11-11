package com.pepperonas.andbasx.interfaces;

import com.pepperonas.andbasx.concurrency.LoaderTaskUtils;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public interface LoaderTaskListener {

    public void onLoaderTaskSuccess(LoaderTaskUtils.Action action, String msg);

    public void onLoaderTaskFailed(LoaderTaskUtils.Action action, String msg);

}
