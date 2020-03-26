package io.github.omisie11.coronatracker.data.repository

import android.content.SharedPreferences
import com.github.mikephil.charting.data.PieEntry
import io.github.omisie11.coronatracker.data.local.dao.LocalSummaryDao
import io.github.omisie11.coronatracker.data.local.model.LocalSummary
import io.github.omisie11.coronatracker.data.mappers.mapToLocalSummary
import io.github.omisie11.coronatracker.data.remote.ApiService
import io.github.omisie11.coronatracker.data.remote.BASE_COUNTRY_URL
import io.github.omisie11.coronatracker.data.remote.model.LocalSummaryRemote
import io.github.omisie11.coronatracker.util.PREFS_KEY_CHOSEN_LOCATION
import io.github.omisie11.coronatracker.util.PREFS_LAST_REFRESH_LOCAL_SUMMARY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response

class LocalSummaryRepository(
    private val apiService: ApiService,
    private val localSummaryDao: LocalSummaryDao,
    private val sharedPrefs: SharedPreferences
) : BaseRepository<LocalSummaryRemote, LocalSummary>(sharedPrefs) {

    override val lastRefreshKey: String = PREFS_LAST_REFRESH_LOCAL_SUMMARY

    fun getLocalSummaryFlow(): Flow<LocalSummary> = localSummaryDao.getLocalSummaryFlow()

    fun getLocalSummaryPieChartDataFlow(): Flow<List<PieEntry>> =
        localSummaryDao.getLocalSummaryFlow()
            .map { summary -> mapLocalSummaryToPieChartEntry(summary) }

    override suspend fun makeApiCall(): Response<LocalSummaryRemote> =
        apiService.getLocalSummary(BASE_COUNTRY_URL + getChosenLocation())

    override suspend fun saveToDb(data: LocalSummary) {
        localSummaryDao.replace(data)
    }

    override suspend fun mapRemoteModelToLocal(data: LocalSummaryRemote): LocalSummary =
        data.mapToLocalSummary()

    private fun getChosenLocation(): String =
        sharedPrefs.getString(PREFS_KEY_CHOSEN_LOCATION, "Poland") ?: "Poland"

    private fun mapLocalSummaryToPieChartEntry(data: LocalSummary?): List<PieEntry> {
        return if (data != null) {
            listOf(
                PieEntry(data.confirmed?.toFloat() ?: 0F, "confirmed"),
                PieEntry(data.recovered?.toFloat() ?: 0F, "recovered"),
                PieEntry(data.deaths?.toFloat() ?: 0F, "deaths")
            )
        } else emptyList()
    }
}
