package io.github.omisie11.coronatracker.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GlobalSummaryRemote(
    @Json(name = "confirmed") val confirmed: GlobalSummaryRemoteItem?,
    @Json(name = "recovered") val recovered: GlobalSummaryRemoteItem?,
    @Json(name = "deaths") val deaths: GlobalSummaryRemoteItem?,
    @Json(name = "image") val imageUrl: String?,
    @Json(name = "lastUpdate") val lastUpdate: String?
) {
    @JsonClass(generateAdapter = true)
    data class GlobalSummaryRemoteItem(
        @Json(name = "value") val value: Int?,
        @Json(name = "detail") val detail: String?
    )
}
