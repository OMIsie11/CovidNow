package io.github.omisie11.coronatracker.data.remote

import io.github.omisie11.coronatracker.data.remote.model.CountriesRemote
import io.github.omisie11.coronatracker.data.remote.model.GlobalSummaryRemote
import io.github.omisie11.coronatracker.data.remote.model.LocalSummaryRemote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {

    /**
     * Get global summary info
     */
    @GET(GLOBAL_URL)
    suspend fun getGlobalSummary(): Response<GlobalSummaryRemote>

    /**
     * Pass country name in English to get summary for single country
     * .../api/countries/[country]
     */
    @GET
    suspend fun getLocalSummary(@Url country: String): Response<LocalSummaryRemote>

    @GET(BASE_COUNTRY_URL)
    suspend fun getCountries(): Response<CountriesRemote>
}
