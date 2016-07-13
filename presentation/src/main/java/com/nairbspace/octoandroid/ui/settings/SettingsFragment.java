package com.nairbspace.octoandroid.ui.settings;

import android.app.backup.BackupManager;
import android.os.Bundle;
import android.support.v7.preference.Preference;

import com.nairbspace.octoandroid.R;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    private boolean mWasPrefChanged = false;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferencesFix(Bundle savedInstanceState, String rootKey) {
        // Add 'general' preferences, defined in the XML file
        addPreferencesFromResource(R.xml.preferences);

        // For all preferences, attach an OnPreferenceChangeListener so the UI summary can be
        // updated when the preference changes.

        // Values below are booleans so setting summary from value will not work
        bindPreferenceSummaryToValue(findPreference(getString(R.string.prefs_push_notification_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.prefs_sticky_notification_key)));
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
//        onPreferenceChange(preference,
//                PreferenceManager
//                        .getDefaultSharedPreferences(preference.getContext())
//                        .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        mWasPrefChanged = true;
//        String stringValue = newValue.toString();
//
//        if (preference instanceof ListPreference) {
//            // For list preferences, look up the correct display value in
//            // the preference's 'entries' list (since they have separate labels/values).
//            ListPreference listPreference = (ListPreference) preference;
//            int prefIndex = listPreference.findIndexOfValue(stringValue);
//            if (prefIndex >= 0) {
//                preference.setSummary(listPreference.getEntries()[prefIndex]);
//            }
//        } else {
//            // For other preferences, set the summary to the value's simple string representation.
//            preference.setSummary(stringValue);
//        }
        return true;
    }

    @Override
    public void onDestroy() {
        if (mWasPrefChanged) new BackupManager(getContext()).dataChanged();
        super.onDestroy();
    }
}
