package io.github.omisie11.coronatracker.di

import androidx.room.Room
import io.github.omisie11.coronatracker.data.local.AppDatabase
import io.github.omisie11.coronatracker.data.mappers.DataMappers
import io.github.omisie11.coronatracker.data.remote.ApiService
import io.github.omisie11.coronatracker.data.repository.GlobalSummaryRepository
import io.github.omisie11.coronatracker.data.repository.LocalSummaryRepository
import io.github.omisie11.coronatracker.ui.global.GlobalViewModel
import io.github.omisie11.coronatracker.ui.local.LocalViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val mainModule = module {

    single {
        Room.databaseBuilder(
                androidApplication(),
                AppDatabase::class.java,
                "corona_data.db"
            )
            .fallbackToDestructiveMigration()
            .build()
    }
}

val networkModule = module {

    single { DataMappers() }

    // Create Retrofit instance
    single {
        Retrofit.Builder()
            .baseUrl("https://covid19.mathdro.id/")
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

    single { GlobalViewModel(get()) }
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

    single { LocalViewModel(get()) }
}
