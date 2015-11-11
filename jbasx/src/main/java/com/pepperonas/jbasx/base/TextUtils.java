package com.pepperonas.jbasx.base;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class TextUtils {

    /**
     * Ensure the content of a {@link CharSequence}.
     *
     * @return Whenever the {@link CharSequence} is empty or not.
     */
    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }


    public static boolean isSecretField(String string) {
        return string.toLowerCase().contains("passwor")
               || string.toLowerCase().contains("pin")
               || string.toLowerCase().contains("key")
               || string.toLowerCase().contains("code");
    }

}
