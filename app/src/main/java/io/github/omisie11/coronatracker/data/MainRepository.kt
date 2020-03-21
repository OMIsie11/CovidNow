package io.github.omisie11.coronatracker.data

import io.github.omisie11.coronatracker.data.local.dao.GlobalSummaryDao
import io.github.omisie11.coronatracker.data.mappers.DataMappers
import io.github.omisie11.coronatracker.data.model.GlobalSummary
import io.github.omisie11.coronatracker.data.remote.ApiService
import java.io.IOException
import retrofit2.HttpException
import timber.log.Timber

class MainRepository(
    private val apiService: ApiService,
    private val globalSummaryDao: GlobalSummaryDao,
    private val mappers: DataMappers
) {

    fun getGlobalSummaryFlow() = globalSummaryDao.getGlobalSummaryFlow()

    suspend fun fetchGlobalSummary(): FetchResult {
        try {
            val response = apiService.getGlobalSummary()
            when {
                response.isSuccessful && response.body() != null -> {
                    // Case 1: Success. We got a response with a body.
                    // save data in db
                    response.body()?.let {
                        saveGlobalSummary(mappers.mapToLocalSummary(it))
                    }
                    // save fetch time
                    Timber.d("Fetch success")
                    return FetchResult.SUCCESS
                }
                response.errorBody() != null -> {
                    // Case 2: Failure. We got an error from the backend, deserialize it.
                    Timber.d("Server error")
                    return FetchResult.SERVER_ERROR
                }
                else -> {
                    // Case 3: Failure. Response didn't have a body. Show a vanilla error.
                    Timber.d("Generic error")
                    return FetchResult.GENERIC_ERROR
                }
            }
        } catch (e: Exception) {
            Timber.d("Exception: $e")
            return when (e) {
                is HttpException -> FetchResult.SERVER_ERROR
                is IOException -> FetchResult.NETWORK_ERROR
                else -> FetchResult.GENERIC_ERROR
            }
        }
    }

    private suspend fun saveGlobalSummary(data: GlobalSummary) {
        globalSummaryDao.replace(data)
    }
}

enum class FetchResult { SUCCESS, SERVER_ERROR, GENERIC_ERROR, NETWORK_ERROR }
