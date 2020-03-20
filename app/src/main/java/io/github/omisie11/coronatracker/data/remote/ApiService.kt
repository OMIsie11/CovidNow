package io.github.omisie11.coronatracker.data.remote

import io.github.omisie11.coronatracker.data.model.GlobalSummaryRemote
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("api/")
    suspend fun getGlobalSummary(): Response<GlobalSummaryRemote>
}
