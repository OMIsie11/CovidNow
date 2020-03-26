package io.github.omisie11.coronatracker.data.repository

import android.content.SharedPreferences
import io.github.omisie11.coronatracker.data.local.dao.CountriesDao
import io.github.omisie11.coronatracker.data.local.model.Country
import io.github.omisie11.coronatracker.data.mappers.mapToLocalCountry
import io.github.omisie11.coronatracker.data.remote.ApiService
import io.github.omisie11.coronatracker.data.remote.model.CountriesRemote
import io.github.omisie11.coronatracker.util.PREFS_LAST_REFRESH_COUNTRIES
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class CountriesRepository(
    private val apiService: ApiService,
    private val countriesDao: CountriesDao,
    sharedPrefs: SharedPreferences
) : BaseRepository<CountriesRemote, List<Country>>(sharedPrefs) {

    override val lastRefreshKey: String = PREFS_LAST_REFRESH_COUNTRIES

    fun getCountriesNamesFlow(): Flow<List<String>> = countriesDao.getCountriesNamesFlow()

    override suspend fun makeApiCall(): Response<CountriesRemote> = apiService.getCountries()

    override suspend fun mapRemoteModelToLocal(data: CountriesRemote): List<Country> =
        data.mapToLocalCountry()

    override suspend fun saveToDb(data: List<Country>) {
        countriesDao.replace(data)
    }
}
