package io.github.omisie11.coronatracker.data.mappers

import io.github.omisie11.coronatracker.data.local.model.Country
import io.github.omisie11.coronatracker.data.local.model.GlobalSummary
import io.github.omisie11.coronatracker.data.local.model.LocalSummary
import io.github.omisie11.coronatracker.data.remote.model.CountriesRemote
import io.github.omisie11.coronatracker.data.remote.model.GlobalSummaryRemote
import io.github.omisie11.coronatracker.data.remote.model.LocalSummaryRemote

class DataMappers {

    fun mapToLocalSummary(data: GlobalSummaryRemote): GlobalSummary =
        GlobalSummary(
            confirmed = data.confirmed?.value,
            recovered = data.recovered?.value,
            deaths = data.deaths?.value,
            imageUrl = data.imageUrl ?: "",
            lastUpdate = data.lastUpdate ?: ""
        )

    fun mapToLocalSummary(data: LocalSummaryRemote): LocalSummary =
        LocalSummary(
            confirmed = data.confirmed?.value,
            recovered = data.recovered?.value,
            deaths = data.deaths?.value,
            lastUpdate = data.lastUpdate ?: ""
        )

    fun mapToLocalCountry(data: CountriesRemote): List<Country> {
        val result = mutableListOf<Country>()
        if (!data.countries.isNullOrEmpty()) {
            for (item in data.countries) {
                if (item.name != null && item.name.isNotBlank()) {
                    result.add(Country(name = item.name, iso2 = item.iso2, iso3 = item.iso3))
                }
            }
        }
        return result
    }
}
