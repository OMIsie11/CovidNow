package io.github.omisie11.coronatracker.di

import android.preference.PreferenceManager
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.github.omisie11.coronatracker.data.local.AppDatabase
import io.github.omisie11.coronatracker.data.remote.ApiService
import io.github.omisie11.coronatracker.data.remote.BASE_URL
import io.github.omisie11.coronatracker.data.repository.CountriesRepository
import io.github.omisie11.coronatracker.data.repository.GlobalSummaryRepository
import io.github.omisie11.coronatracker.data.repository.LocalSummaryRepository
import io.github.omisie11.coronatracker.ui.global.GlobalViewModel
import io.github.omisie11.coronatracker.ui.local.LocalViewModel
import io.github.omisie11.coronatracker.ui.settings.CountriesViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val mainModule = module {

    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single {
        Room.databaseBuilder(
                androidApplication(),
                AppDatabase::class.java,
                "corona_data.db"
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    single { PreferenceManager.getDefaultSharedPreferences(get()) }
}

val networkModule = module {

    // Create Retrofit instance
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    // Create retrofit Service
    single { get<Retrofit>().create(ApiService::class.java) }
}

val globalModule = module {

    single { get<AppDatabase>().globalSummaryDao() }

    single {
        GlobalSummaryRepository(
            get(),
            get(),
            get()
        )
    }

    viewModel { GlobalViewModel(get()) }
}

val localModule = module {

    single { get<AppDatabase>().localSummaryDao() }

    single {
        LocalSummaryRepository(
            get(),
            get(),
            get()
        )
    }

    viewModel { LocalViewModel(get()) }
}

val countriesModule = module {

    single { get<AppDatabase>().countriesDao() }

    single {
        CountriesRepository(
            get(),
            get(),
            get()
        )
    }

    viewModel {
        CountriesViewModel(
            get()
        )
    }
}
