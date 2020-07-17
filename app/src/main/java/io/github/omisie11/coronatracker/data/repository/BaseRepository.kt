package io.github.omisie11.coronatracker.data.repository

import android.content.SharedPreferences
import io.github.omisie11.coronatracker.util.PREFS_KEY_REFRESH_INTERVAL
import io.github.omisie11.coronatracker.vo.FetchResult
import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import org.threeten.bp.Instant
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

@ExperimentalCoroutinesApi
abstract class BaseRepository<RemoteModel, LocalModel>(
    private val sharedPrefs: SharedPreferences
) {

    protected abstract val lastRefreshKey: String

    // private val isDataFetching = MutableLiveData(false)
    private val isDataFetching = MutableStateFlow(value = false)

    // private val fetchResult = MutableLiveData(FetchResult.OK)
    private val fetchResult = MutableStateFlow(FetchResult.OK)

    fun getFetchingStatus(): StateFlow<Boolean> = isDataFetching

    fun getFetchResult(): MutableStateFlow<FetchResult> = fetchResult

    suspend fun refreshData(forceRefresh: Boolean = false) {
        if (forceRefresh || isDataRefreshNeeded()) {
            Timber.d("Performing refresh")
            fetchDataFromApi()
        } else Timber.d("Refresh not needed")
    }

    private suspend fun fetchDataFromApi() = withContext(NonCancellable) {
        // isDataFetching.postValue(true)
        isDataFetching.value = true
        try {
            val response = makeApiCall()
            when {
                response.isSuccessful && response.body() != null -> {
                    response.body()?.let { data ->
                        saveToDb(mapRemoteModelToLocal(data))
                    }
                    saveRefreshTime()
                    Timber.d("Fetch success")
                    fetchResult.value = FetchResult.OK
                }
                response.errorBody() != null -> {
                    Timber.d("Server error")
                    fetchResult.value = FetchResult.SERVER_ERROR
                }
                else -> {
                    Timber.d("Response with empty body")
                    fetchResult.value = FetchResult.UNEXPECTED_ERROR
                }
            }
        } catch (e: Exception) {
            Timber.d("Exception: $e")
            when (e) {
                is HttpException -> {
                    Timber.d("Http exception")
                    fetchResult.value = FetchResult.NETWORK_ERROR
                }
                is IOException -> {
                    Timber.d("IO exception")
                    fetchResult.value = FetchResult.NETWORK_ERROR
                }
                else -> {
                    Timber.d("Other exception")
                    fetchResult.value = FetchResult.UNEXPECTED_ERROR
                }
            }
        }
        // isDataFetching.postValue(false)
        isDataFetching.value = false
    }

    protected abstract suspend fun makeApiCall(): Response<RemoteModel>

    protected abstract suspend fun saveToDb(data: LocalModel)

    protected abstract suspend fun mapRemoteModelToLocal(data: RemoteModel): LocalModel

    private fun isDataRefreshNeeded(): Boolean {
        val refreshInterval: Long = getRefreshInterval() * NUMBER_OF_SECONDS_IN_HOUR
        val lastRefresh: Long = getLastRefreshTime()
        val currentTime: Long = Instant.now().epochSecond

        return currentTime - lastRefresh > refreshInterval
    }

    private fun getLastRefreshTime(): Long = sharedPrefs.getLong(lastRefreshKey, 0)

    // Stored as String because of usage of Preference library
    private fun getRefreshInterval(): Long =
        sharedPrefs.getString(PREFS_KEY_REFRESH_INTERVAL, "3")?.toLong() ?: 3L

    /**
     * Saves current time as last refresh time
     */
    private fun saveRefreshTime() {
        with(sharedPrefs.edit()) {
            putLong(lastRefreshKey, Instant.now().epochSecond)
            apply()
        }
    }

    companion object {
        const val NUMBER_OF_SECONDS_IN_HOUR = 3600
    }
}
