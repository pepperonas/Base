package com.pepperonas.andbasx.interfaces;

import com.pepperonas.andbasx.concurrency.LoaderTaskUtils;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public interface LoaderTaskListener {

    void onLoaderTaskSuccess(LoaderTaskUtils.Action action, String msg);

    void onLoaderTaskFailed(LoaderTaskUtils.Action action, String msg);

}
