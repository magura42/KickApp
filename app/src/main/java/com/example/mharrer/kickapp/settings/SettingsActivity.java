package com.example.mharrer.kickapp.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;

import com.example.mharrer.kickapp.R;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by mharrer on 11.11.14.
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }

    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onResume() {
            super.onResume();
            for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); ++i) {
                Preference preference = getPreferenceScreen().getPreference(i);
                if (preference instanceof PreferenceGroup) {
                    PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
                    for (int j = 0; j < preferenceGroup.getPreferenceCount(); ++j) {
                        updatePreference(preferenceGroup.getPreference(j));
                    }
                } else {
                    updatePreference(preference);
                }
            }
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            updatePreference(findPreference(key));
        }

        private void updatePreference(Preference preference) {
            if (preference instanceof EditTextPreference) {
                EditTextPreference editTextPreference = (EditTextPreference)preference;
                if (editTextPreference.getKey().equals("Benutzername")) {
                    if (StringUtils.isNotBlank(editTextPreference.getText())) {
                        editTextPreference.setSummary(getActivity().getApplicationContext().getString(R.string.settings_user_name_sum) + " " + editTextPreference.getText());
                    } else {
                        editTextPreference.setSummary(R.string.settings_user_name_empty_sum);
                    }
                } else if (editTextPreference.getKey().equals("Passwort")) {
                    if (StringUtils.isNotBlank(editTextPreference.getText())) {
                        editTextPreference.setSummary(getActivity().getApplicationContext().getString(R.string.settings_password_sum));
                    } else {
                        editTextPreference.setSummary(R.string.settings_password_empty_sum);
                    }
                }
            }

        }
    }

}
