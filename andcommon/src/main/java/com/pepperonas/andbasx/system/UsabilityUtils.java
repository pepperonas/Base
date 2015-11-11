package com.pepperonas.andbasx.system;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.pepperonas.andbasx.AndBasx;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class UsabilityUtils {

    private static final String TAG = "UsabilityUtils";


    /**
     * Prevent the keyboard from expanding.
     *
     * @param activity the calling {@link Activity}
     */
    public static void keepKeyboardHidden(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    /**
     * Toggle the current keyboard-state.
     */
    public static void changeKeyboardState() {
        InputMethodManager imm = (InputMethodManager) AndBasx.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

}
