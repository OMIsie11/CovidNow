package io.github.omisie11.coronatracker.data.repository

import io.github.omisie11.coronatracker.data.local.dao.LocalSummaryDao
import io.github.omisie11.coronatracker.data.local.model.LocalSummary
import io.github.omisie11.coronatracker.data.mappers.DataMappers
import io.github.omisie11.coronatracker.data.remote.ApiService
import io.github.omisie11.coronatracker.data.remote.model.LocalSummaryRemote
import retrofit2.Response

class LocalSummaryRepository(
    private val apiService: ApiService,
    private val localSummaryDao: LocalSummaryDao,
    private val mappers: DataMappers
) : BaseRepository<LocalSummaryRemote, LocalSummary>() {

    fun getLocalSummaryFlow() = localSummaryDao.getLocalSummaryFlow()

    override suspend fun makeApiCall(): Response<LocalSummaryRemote> = apiService.getLocalSummary()

    override suspend fun saveToDb(data: LocalSummary) {
        localSummaryDao.replace(data)
    }

    override suspend fun mapRemoteModelToLocal(data: LocalSummaryRemote): LocalSummary =
        mappers.mapToLocalSummary(data)
}
