package com.pepperonas.andbasx.system;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.pepperonas.andbasx.AndBasx;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class UsabilityUtils {

    private static final String TAG = "UsabilityUtils";


    public static void launchWebIntent(Activity activity, String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        activity.startActivity(i);
    }


    public static boolean launchAppOrPlayStore(String packageName) {
        PackageManager manager = AndBasx.getContext().getPackageManager();
        boolean result = false;
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                i = manager.getLaunchIntentForPackage("play.google.com/store/apps/details?id=" + packageName);
            } else {
                result = true;
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            AndBasx.getContext().startActivity(i);
            return result;
        } catch (Exception e) {
            android.util.Log.e(TAG, e.getMessage());
            return result;
        }
    }


    public static void restartApplication(Class<?> clazz) {
        Intent intent = new Intent(AndBasx.getContext(), clazz);
        int pendingIntentId = 198964;

        PendingIntent pendingIntent = PendingIntent.getActivity                                 (
                AndBasx.getContext(), pendingIntentId, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager)
                AndBasx.getContext().getSystemService(Context.ALARM_SERVICE);

        am.set(AlarmManager.RTC, System.currentTimeMillis() + 500, pendingIntent);
        System.exit(0);
    }


    /**
     * Start an intent to share text information.
     *
     * @param receiver   The addresses which should receive the message.
     * @param intentInfo The intent's description.
     * @param subject    The content's subject.
     * @param msg        The content's message.
     */
    public static void sendShareTextIntent(Context ctx, String[] receiver, String intentInfo, String subject, String msg) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, receiver);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, msg);
        ctx.startActivity(Intent.createChooser(intent, intentInfo));
    }


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
