package io.github.omisie11.coronatracker.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.omisie11.coronatracker.vo.FetchResult
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

abstract class BaseRepository<RemoteModel, LocalModel> {

    private val isDataFetching = MutableLiveData(false)
    private val fetchResult = MutableLiveData(FetchResult.OK)

    fun getFetchingStatus(): LiveData<Boolean> = isDataFetching

    fun getFetchResult(): MutableLiveData<FetchResult> = fetchResult

    suspend fun fetchDataFromApi() = withContext(Dispatchers.IO) {
        isDataFetching.postValue(true)
        try {
            val response = makeApiCall()
            when {
                response.isSuccessful && response.body() != null -> {
                    // save data in db
                    response.body()?.let { data ->
                        saveToDb(mapRemoteModelToLocal(data))
                    }
                    // save fetch time here
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

    protected abstract suspend fun makeApiCall(): Response<RemoteModel>

    protected abstract suspend fun saveToDb(data: LocalModel)

    protected abstract suspend fun mapRemoteModelToLocal(data: RemoteModel): LocalModel
}
