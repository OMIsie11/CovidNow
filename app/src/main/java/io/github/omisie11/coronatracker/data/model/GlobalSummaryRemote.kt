package io.github.omisie11.coronatracker.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GlobalSummaryRemote (
    @field:Json(name = "confirmed") val confirmed: GlobalSummaryRemoteItem?,
    @field:Json(name = "recovered") val recovered: GlobalSummaryRemoteItem?,
    @field:Json(name = "deaths") val deaths: GlobalSummaryRemoteItem?,
    @field:Json(name = "image") val imageUrl: String?,
    @field:Json(name = "lastUpdate") val lastUpdate: String?
) {
    @JsonClass(generateAdapter = true)
    data class GlobalSummaryRemoteItem(
        @field:Json(name = "value") val value: Int?,
        @field:Json(name = "detail") val detail: String?
    )
}