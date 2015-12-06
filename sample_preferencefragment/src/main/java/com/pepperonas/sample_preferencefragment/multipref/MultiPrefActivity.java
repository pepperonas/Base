package com.pepperonas.sample_preferencefragment.multipref;

import android.preference.PreferenceActivity;

import com.pepperonas.sample_preferencefragment.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class MultiPrefActivity extends PreferenceActivity {

    private static List<String> fragments = new ArrayList<>();


    @Override
    public void onBuildHeaders(List<Header> target) {
        super.onBuildHeaders(target);
        loadHeadersFromResource(R.xml.multi_headers, target);
        fragments.clear();
        for (Header header : target) {
            fragments.add(header.fragment);
        }
    }


    @Override
    protected boolean isValidFragment(String fragmentName) {
        return fragments.contains(fragmentName);
    }
}
