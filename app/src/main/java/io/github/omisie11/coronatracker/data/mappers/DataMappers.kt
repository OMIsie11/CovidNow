package io.github.omisie11.coronatracker.data.mappers

import io.github.omisie11.coronatracker.data.model.GlobalSummary
import io.github.omisie11.coronatracker.data.model.GlobalSummaryRemote

class DataMappers {

    fun mapToLocalSummary(data: GlobalSummaryRemote): GlobalSummary = GlobalSummary(
        confirmed = data.confirmed?.value,
        recovered = data.recovered?.value,
        deaths = data.deaths?.value,
        imageUrl = data.imageUrl ?: "",
        lastUpdate = data.lastUpdate ?: ""
    )
}
