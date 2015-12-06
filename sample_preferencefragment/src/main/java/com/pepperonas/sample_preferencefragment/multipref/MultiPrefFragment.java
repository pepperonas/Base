package com.pepperonas.sample_preferencefragment.multipref;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.pepperonas.sample_preferencefragment.R;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class MultiPrefFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.multi_prefs);
    }

    protected boolean isValidFragment(String fragmentName) {
        return MultiPrefFragment.class.getName().equals(fragmentName);
    }

}
