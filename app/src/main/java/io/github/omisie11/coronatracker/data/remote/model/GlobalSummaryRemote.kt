package io.github.omisie11.coronatracker.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GlobalSummaryRemote(
    @Json(name = "confirmed") val confirmed: SummaryRemoteItem?,
    @Json(name = "recovered") val recovered: SummaryRemoteItem?,
    @Json(name = "deaths") val deaths: SummaryRemoteItem?,
    @Json(name = "image") val imageUrl: String?,
    @Json(name = "lastUpdate") val lastUpdate: String?
)
