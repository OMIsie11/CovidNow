package io.github.omisie11.coronatracker

import android.app.Application
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

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            androidFileProperties()
            modules(listOf(mainModule, networkModule))
        }

        // Logging in Debug build, in release log only crashes
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree()) else
            Timber.plant(CrashReportingTree())
    }
}
