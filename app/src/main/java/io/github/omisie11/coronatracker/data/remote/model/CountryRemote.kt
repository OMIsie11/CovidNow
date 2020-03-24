package io.github.omisie11.coronatracker.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryRemote(
    @Json(name = "name") val name: String?,
    @Json(name = "iso2") val iso2: String?,
    @Json(name = "iso3") val iso3: String?
)
