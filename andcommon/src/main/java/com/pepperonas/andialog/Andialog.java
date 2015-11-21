package com.pepperonas.andialog;

import android.app.Dialog;
import android.content.Context;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class Andialog extends Dialog {

    public Andialog(Context context) {
        super(context);
    }


    public Andialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    protected Andialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
