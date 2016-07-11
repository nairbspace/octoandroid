package com.nairbspace.octoandroid.ui.printer_settings;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;

import com.nairbspace.octoandroid.R;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

public class PrinterDetailsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    public static PrinterDetailsFragment newInstance() {
        return new PrinterDetailsFragment();
    }

    @Override
    public void onCreatePreferencesFix(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.printer_preferences);

        // For all preferences, attach an OnPreferenceChangeListener so the UI summary can be
        // updated when the preference changes.
        bindPreferenceSummaryToValue(findPreference(getString(R.string.prefs_printer_name_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.prefs_printer_host_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.prefs_printer_api_key_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.prefs_printer_scheme_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.prefs_printer_port_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.prefs_printer_websocket_path_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.prefs_printer_webcam_path_query_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.prefs_printer_upload_location_key)));
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference, PreferenceManager
                .getDefaultSharedPreferences(preference.getContext())
                .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String stringValue = newValue.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }
        return true;
    }
}
