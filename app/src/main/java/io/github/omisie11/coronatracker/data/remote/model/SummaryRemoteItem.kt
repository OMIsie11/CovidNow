package io.github.omisie11.coronatracker.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SummaryRemoteItem(
    @Json(name = "value") val value: Int?,
    @Json(name = "detail") val detail: String?
)
