package io.github.omisie11.coronatracker.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.omisie11.coronatracker.data.local.dao.GlobalSummaryDao
import io.github.omisie11.coronatracker.data.mappers.DataMappers
import io.github.omisie11.coronatracker.data.model.GlobalSummary
import io.github.omisie11.coronatracker.data.remote.ApiService
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber

class GlobalSummaryRepository(
    private val apiService: ApiService,
    private val globalSummaryDao: GlobalSummaryDao,
    private val mappers: DataMappers
) {

    private val isDataFetching = MutableLiveData(false)
    private val fetchResult = MutableLiveData<String>()

    fun getGlobalSummaryFlow() = globalSummaryDao.getGlobalSummaryFlow()

    fun getFetchingStatus(): LiveData<Boolean> = isDataFetching

    fun getFetchResult(): MutableLiveData<String> = fetchResult

    suspend fun fetchGlobalSummary() = withContext(Dispatchers.IO) {
        isDataFetching.postValue(true)
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
                    // FetchResult.SUCCESS
                }
                response.errorBody() != null -> {
                    // Case 2: Failure. We got an error from the backend, deserialize it.
                    Timber.d("Server error")
                    // FetchResult.SERVER_ERROR
                    fetchResult.postValue("Server error")
                }
                else -> {
                    // Case 3: Failure. Response didn't have a body. Show a vanilla error.
                    Timber.d("Generic error")
                    // FetchResult.GENERIC_ERROR
                    fetchResult.postValue("Generic error")
                }
            }
        } catch (e: Exception) {
            Timber.d("Exception: $e")
            when (e) {
                is HttpException -> {
                    Timber.d("Http exception")
                    fetchResult.postValue("Network error")
                }
                is IOException -> {
                    Timber.d("IO exception")
                    fetchResult.postValue("Network error")
                }
                else -> {
                    Timber.d("Other exception")
                    fetchResult.postValue("Unexpected error")
                }
            }
        }
        isDataFetching.postValue(false)
    }

    private suspend fun saveGlobalSummary(data: GlobalSummary) {
        globalSummaryDao.replace(data)
    }
}
