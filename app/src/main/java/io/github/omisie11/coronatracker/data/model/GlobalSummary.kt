package io.github.omisie11.coronatracker.data.model

data class GlobalSummary(
    val confirmed: Int?,
    val recovered: Int?,
    val deaths: Int?,
    val imageUrl: String?,
    val lastUpdate: String?
)
