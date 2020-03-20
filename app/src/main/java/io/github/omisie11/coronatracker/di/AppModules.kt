package io.github.omisie11.coronatracker.di

import io.github.omisie11.coronatracker.data.remote.ApiService
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val mainModule = module {
}

val networkModule = module {

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
