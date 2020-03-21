package io.github.omisie11.coronatracker.data.remote

import io.github.omisie11.coronatracker.data.remote.model.GlobalSummaryRemote
import io.github.omisie11.coronatracker.data.remote.model.LocalSummaryRemote
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("api/")
    suspend fun getGlobalSummary(): Response<GlobalSummaryRemote>

    @GET("api/countries/poland")
    suspend fun getLocalSummary(): Response<LocalSummaryRemote>
}
