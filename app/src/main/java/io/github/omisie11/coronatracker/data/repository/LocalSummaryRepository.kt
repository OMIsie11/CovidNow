package io.github.omisie11.coronatracker.data.repository

import android.content.SharedPreferences
import io.github.omisie11.coronatracker.data.local.dao.LocalSummaryDao
import io.github.omisie11.coronatracker.data.local.model.LocalSummary
import io.github.omisie11.coronatracker.data.mappers.DataMappers
import io.github.omisie11.coronatracker.data.remote.ApiService
import io.github.omisie11.coronatracker.data.remote.BASE_COUNTRY_URL
import io.github.omisie11.coronatracker.data.remote.model.LocalSummaryRemote
import io.github.omisie11.coronatracker.util.PREFS_LAST_REFRESH_LOCAL_SUMMARY
import retrofit2.Response

class LocalSummaryRepository(
    private val apiService: ApiService,
    private val localSummaryDao: LocalSummaryDao,
    private val mappers: DataMappers,
    sharedPrefs: SharedPreferences
) : BaseRepository<LocalSummaryRemote, LocalSummary>(sharedPrefs) {

    // Hardcoded value for choosing country
    private val country = "poland"

    override val lastRefreshKey: String = PREFS_LAST_REFRESH_LOCAL_SUMMARY

    fun getLocalSummaryFlow() = localSummaryDao.getLocalSummaryFlow()

    override suspend fun makeApiCall(): Response<LocalSummaryRemote> =
        apiService.getLocalSummary(BASE_COUNTRY_URL + country)

    override suspend fun saveToDb(data: LocalSummary) {
        localSummaryDao.replace(data)
    }

    override suspend fun mapRemoteModelToLocal(data: LocalSummaryRemote): LocalSummary =
        mappers.mapToLocalSummary(data)
}
