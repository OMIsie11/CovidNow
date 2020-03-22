package io.github.omisie11.coronatracker.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import io.github.omisie11.coronatracker.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_preferences, rootKey)
    }
}
