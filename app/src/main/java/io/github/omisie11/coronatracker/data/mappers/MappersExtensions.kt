package io.github.omisie11.coronatracker.data.mappers

import io.github.omisie11.coronatracker.data.local.model.Country
import io.github.omisie11.coronatracker.data.local.model.GlobalSummary
import io.github.omisie11.coronatracker.data.local.model.LocalSummary
import io.github.omisie11.coronatracker.data.remote.model.CountriesRemote
import io.github.omisie11.coronatracker.data.remote.model.GlobalSummaryRemote
import io.github.omisie11.coronatracker.data.remote.model.LocalSummaryRemote

fun GlobalSummaryRemote.mapToLocalSummary(): GlobalSummary =
    GlobalSummary(
        confirmed = this.confirmed?.value,
        recovered = this.recovered?.value,
        deaths = this.deaths?.value,
        imageUrl = this.imageUrl ?: "",
        lastUpdate = this.lastUpdate ?: ""
    )

fun LocalSummaryRemote.mapToLocalSummary(): LocalSummary =
    LocalSummary(
        confirmed = this.confirmed?.value,
        recovered = this.recovered?.value,
        deaths = this.deaths?.value,
        lastUpdate = this.lastUpdate ?: ""
    )

fun CountriesRemote.mapToLocalCountry(): List<Country> {
    val result = mutableListOf<Country>()
    if (!this.countries.isNullOrEmpty()) {
        for (item in this.countries) {
            if (item.name != null && item.name.isNotBlank()) {
                result.add(Country(name = item.name, iso2 = item.iso2, iso3 = item.iso3))
            }
        }
    }
    return result
}
