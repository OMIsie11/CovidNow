package io.github.omisie11.coronatracker.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.omisie11.coronatracker.data.local.dao.GlobalSummaryDao
import io.github.omisie11.coronatracker.data.mappers.DataMappers
import io.github.omisie11.coronatracker.data.model.GlobalSummary
import io.github.omisie11.coronatracker.data.remote.ApiService
import io.github.omisie11.coronatracker.vo.FetchResult
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
    private val fetchResult = MutableLiveData<FetchResult>(FetchResult.OK)

    fun getGlobalSummaryFlow() = globalSummaryDao.getGlobalSummaryFlow()

    fun getFetchingStatus(): LiveData<Boolean> = isDataFetching

    fun getFetchResult(): MutableLiveData<FetchResult> = fetchResult

    suspend fun fetchGlobalSummary() = withContext(Dispatchers.IO) {
        isDataFetching.postValue(true)
        try {
            val response = apiService.getGlobalSummary()
            when {
                response.isSuccessful && response.body() != null -> {
                    // save data in db
                    response.body()?.let {
                        saveGlobalSummary(mappers.mapToLocalSummary(it))
                    }
                    // save fetch time
                    saveFetchTime()
                    Timber.d("Fetch success")
                    fetchResult.postValue(FetchResult.OK)
                }
                response.errorBody() != null -> {
                    Timber.d("Server error")
                    fetchResult.postValue(FetchResult.SERVER_ERROR)
                }
                else -> {
                    Timber.d("Response with empty body")
                    fetchResult.postValue(FetchResult.UNEXPECTED_ERROR)
                }
            }
        } catch (e: Exception) {
            Timber.d("Exception: $e")
            when (e) {
                is HttpException -> {
                    Timber.d("Http exception")
                    fetchResult.postValue(FetchResult.NETWORK_ERROR)
                }
                is IOException -> {
                    Timber.d("IO exception")
                    fetchResult.postValue(FetchResult.NETWORK_ERROR)
                }
                else -> {
                    Timber.d("Other exception")
                    fetchResult.postValue(FetchResult.UNEXPECTED_ERROR)
                }
            }
        }
        isDataFetching.postValue(false)
    }

    private suspend fun saveGlobalSummary(data: GlobalSummary) {
        globalSummaryDao.replace(data)
    }

    private suspend fun saveFetchTime() {
    }
}
