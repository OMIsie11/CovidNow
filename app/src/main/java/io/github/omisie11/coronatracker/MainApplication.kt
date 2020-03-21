package io.github.omisie11.coronatracker

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import io.github.omisie11.coronatracker.di.globalModule
import io.github.omisie11.coronatracker.di.localModule
import io.github.omisie11.coronatracker.di.mainModule
import io.github.omisie11.coronatracker.di.networkModule
import io.github.omisie11.coronatracker.util.CrashReportingTree
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
            modules(listOf(mainModule, networkModule, globalModule, localModule))
        }

        // Logging in Debug build, in release log only crashes
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree()) else
            Timber.plant(CrashReportingTree())
    }
}
