package com.fairlight.submission_5.themoviedb.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;

import com.fairlight.submission_5.themoviedb.R;
import com.fairlight.submission_5.themoviedb.receiver.DailyReminderReceiver;
import com.fairlight.submission_5.themoviedb.receiver.ReleaseTodayReminderReceiver;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.pref_main, rootKey);

            final SwitchPreference daily_reminder = findPreference(getString(R.string.key_daily_reminder));

            if (daily_reminder != null) {
                daily_reminder.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        if (daily_reminder.isChecked()) {
                            DailyReminderReceiver.unsetDailyReminder(requireContext());
                            daily_reminder.setChecked(false);
                        } else {
                            DailyReminderReceiver.setDailyReminder(requireContext());
                            daily_reminder.setChecked(true);
                        }
                        return false;
                    }
                });
            }

            final SwitchPreference release_reminder = findPreference(getString(R.string.key_release_reminder));

            if (release_reminder != null) {
                release_reminder.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        if (release_reminder.isChecked()) {
                            ReleaseTodayReminderReceiver.unsetReleaseTodayReminder(requireContext());
                            release_reminder.setChecked(false);
                        } else {
                            ReleaseTodayReminderReceiver.setReleaseTodayReminder(requireContext());
                            release_reminder.setChecked(true);
                        }
                        return false;
                    }
                });
            }

            Preference change_language = findPreference(getString(R.string.key_to_language_settings));

            if (change_language != null) {
                change_language.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        Intent changeLanguageSettings = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                        startActivity(changeLanguageSettings);
                        return true;
                    }
                });
            }
        }
    }
}