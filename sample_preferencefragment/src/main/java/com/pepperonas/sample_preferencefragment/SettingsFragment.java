package com.pepperonas.sample_preferencefragment;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTheme(R.style.AppTheme);

        if (getArguments() != null) {
            String page = getArguments().getString("key_value");
            if (page != null)
                switch (page) {
                    case "display":
                        addPreferencesFromResource(R.xml.pref_screen_display);
                        break;
                    case "notification":
                        addPreferencesFromResource(R.xml.pref_screen_notification);
                        break;
                    case "update":
                        addPreferencesFromResource(R.xml.pref_screen_update);
                        break;
                }
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.settings_page, container, false);
        if (layout != null) {
            AppCompatPreferenceActivity activity = (AppCompatPreferenceActivity) getActivity();
            ActionBar bar = activity.getSupportActionBar();
            bar.setHomeButtonEnabled(true);
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowTitleEnabled(true);
            bar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            bar.setTitle(getPreferenceScreen().getTitle());
        }
        return layout;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //        Preference preference = findPreference("update_key_02");
        //        preference.setOnPreferenceClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();

        if (getView() != null) {
            View frame = (View) getView().getParent();
            if (frame != null) {
                frame.setPadding(0, 0, 0, 0);
            }
        }
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals("update_key_02")) {
            Toast.makeText(getActivity(), "Check for updates...", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

}
