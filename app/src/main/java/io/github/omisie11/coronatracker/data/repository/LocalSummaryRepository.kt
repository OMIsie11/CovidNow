package io.github.omisie11.coronatracker.data.repository

import io.github.omisie11.coronatracker.data.local.dao.LocalSummaryDao
import io.github.omisie11.coronatracker.data.local.model.LocalSummary
import io.github.omisie11.coronatracker.data.mappers.DataMappers
import io.github.omisie11.coronatracker.data.remote.ApiService
import io.github.omisie11.coronatracker.data.remote.BASE_COUNTRY_URL
import io.github.omisie11.coronatracker.data.remote.model.LocalSummaryRemote
import retrofit2.Response

class LocalSummaryRepository(
    private val apiService: ApiService,
    private val localSummaryDao: LocalSummaryDao,
    private val mappers: DataMappers
) : BaseRepository<LocalSummaryRemote, LocalSummary>() {

    // Hardcoded value for choosing country
    private val country = "poland"

    fun getLocalSummaryFlow() = localSummaryDao.getLocalSummaryFlow()

    override suspend fun makeApiCall(): Response<LocalSummaryRemote> =
        apiService.getLocalSummary(BASE_COUNTRY_URL + country)

    override suspend fun saveToDb(data: LocalSummary) {
        localSummaryDao.replace(data)
    }

    override suspend fun mapRemoteModelToLocal(data: LocalSummaryRemote): LocalSummary =
        mappers.mapToLocalSummary(data)
}
