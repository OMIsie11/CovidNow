package io.github.omisie11.coronatracker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.jakewharton.threetenabp.AndroidThreeTen
import io.github.omisie11.coronatracker.di.countriesModule
import io.github.omisie11.coronatracker.di.globalModule
import io.github.omisie11.coronatracker.di.localModule
import io.github.omisie11.coronatracker.di.mainModule
import io.github.omisie11.coronatracker.di.networkModule
import io.github.omisie11.coronatracker.util.CrashReportingTree
import io.github.omisie11.coronatracker.util.PREFS_KEY_APP_THEME
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            androidFileProperties()
            modules(listOf(mainModule, networkModule, globalModule, localModule, countriesModule))
        }

        // Logging in Debug build, in release log only crashes
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree()) else
            Timber.plant(CrashReportingTree())

        val sharedPrefs: SharedPreferences = get()
        AppCompatDelegate.setDefaultNightMode(
            translateValueToDayNightMode(
                sharedPrefs.getBoolean(PREFS_KEY_APP_THEME, false)
            )
        )
    }

    private fun translateValueToDayNightMode(value: Boolean): Int = when (value) {
        true -> AppCompatDelegate.MODE_NIGHT_YES
        false -> AppCompatDelegate.MODE_NIGHT_NO
    }
}
