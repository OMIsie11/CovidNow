package io.github.omisie11.coronatracker.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import io.github.omisie11.coronatracker.R
import io.github.omisie11.coronatracker.ui.local.LocalViewModel
import io.github.omisie11.coronatracker.util.PREFS_KEY_CHOSEN_LOCATION
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class SettingsFragment : PreferenceFragmentCompat() {

    private val sharedPrefs: SharedPreferences by inject()
    private lateinit var sharedPrefsListener: SharedPreferences.OnSharedPreferenceChangeListener
    private val countriesViewModel by viewModel<CountriesViewModel>()
    private val localViewModel by sharedViewModel<LocalViewModel>()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val countryPreference = findPreference<ListPreference>(PREFS_KEY_CHOSEN_LOCATION)

        sharedPrefsListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            when (key) {
                PREFS_KEY_CHOSEN_LOCATION -> {
                    localViewModel.refreshLocalSummary(forceRefresh = true)
                    countryPreference?.summary = getChosenLocation()
                }
            }
        }

        countriesViewModel.getCountries().observe(viewLifecycleOwner, Observer { countries ->
            if (countries != null) {
                countryPreference?.apply {
                    entries = countries.toTypedArray()
                    entryValues = countries.toTypedArray()
                }
            } else countriesViewModel.refreshCountriesData(forceRefresh = true)
        })
    }

    override fun onStart() {
        super.onStart()
        sharedPrefs.registerOnSharedPreferenceChangeListener(sharedPrefsListener)
    }

    override fun onResume() {
        super.onResume()
        countriesViewModel.refreshCountriesData(forceRefresh = false)
    }

    override fun onStop() {
        super.onStop()
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(sharedPrefsListener)
    }

    private fun getChosenLocation(): String =
        sharedPrefs.getString(PREFS_KEY_CHOSEN_LOCATION, "Poland") ?: ""
}
