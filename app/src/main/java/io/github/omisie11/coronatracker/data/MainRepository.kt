package io.github.omisie11.coronatracker.data

import io.github.omisie11.coronatracker.data.remote.ApiService
import timber.log.Timber

class MainRepository(
    private val apiService: ApiService
) {

    suspend fun fetchGlobalSummary() {
        Timber.d("fetchGlobalSummary")
        val response = apiService.getGlobalSummary()
        if (response.isSuccessful) {
            Timber.d("response successful")
            Timber.d(response.body().toString())
        } else Timber.d("Error: ${response.errorBody()}")
    }
}
